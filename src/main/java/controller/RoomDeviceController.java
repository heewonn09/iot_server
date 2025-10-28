package controller;

import service.RoomDeviceService;
import service.RoomDeviceServiceImpl;
import dto.RoomDeviceDTO;
import mqtt.MqttManager;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RoomDeviceController {
	private Scanner sc = new Scanner(System.in);
	private MqttManager mqttManager;
	private RoomDeviceService service;
	public RoomDeviceController(MqttManager mqttManager) {
		service = new RoomDeviceServiceImpl(mqttManager);
	}

	// 관리자용: 제어 가능
	public void handleRoomDeviceAdmin() {
		System.out.print("호실명 입력 (예: 101A): ");
		String room_name = sc.nextLine();

		List<RoomDeviceDTO> devices = service.getDeviceList(room_name);
		
		if (devices.isEmpty()) {
			System.out.println("❌ 해당 호실 기기 없음");
			return;
		}

		System.out.println("\n=== " + room_name + " 호실 기기 목록 (관리자) ===");
		int idx = 1;
		
		for (RoomDeviceDTO d : devices) {
			// ✅ 기기 타입별로 한줄씩 깔끔하게 표시
			if (d.getDevice_type().equals("DHT")) {
				System.out.printf("%d. %s (%s) - 온도: %.1f°C, 습도: %.1f%%\n",
					idx++, d.getDevice_name(), d.getDevice_type(), 
					d.getTemperature(), d.getHumidity());
			} else if (d.getDevice_type().equals("FAN")) {
				System.out.printf("%d. %s (%s) - 상태: %s 🌀\n",
					idx++, d.getDevice_name(), d.getDevice_type(), 
					d.getStatus());
			} else if (d.getDevice_type().equals("LED")) {
				System.out.printf("%d. %s (%s) - 상태: %s 💡\n",
					idx++, d.getDevice_name(), d.getDevice_type(), 
					d.getStatus());
			} else {
				System.out.printf("%d. %s (%s) - 상태: %s\n",
					idx++, d.getDevice_name(), d.getDevice_type(), 
					d.getStatus());
			}
		}

		System.out.print("\n제어할 기기번호 선택: ");
		int choice = sc.nextInt();
		sc.nextLine();

		if (choice < 1 || choice > devices.size()) {
			System.out.println("❌ 잘못된 선택");
			return;
		}

		RoomDeviceDTO selected = devices.get(choice - 1);

		// ✅ FAN 타입 추가! LED, AC, FAN만 제어 가능
		if (!selected.getDevice_type().equals("LED") && 
		    !selected.getDevice_type().equals("AC") && 
		    !selected.getDevice_type().equals("FAN")) {
			System.out.println("❌ 제어 불가능한 기기입니다. (센서는 조회만 가능)");
			return;
		}

		System.out.printf("%s 켜시겠습니까? (y/n): ", selected.getDevice_name());
		String cmd = sc.nextLine();
		String newStatus = cmd.equalsIgnoreCase("y") ? "ON" : "OFF";

		if (service.controlDevice(selected.getRoom_id(), selected.getDevice_name(), newStatus)) {
			System.out.println("✅ 상태 변경 완료!");
			System.out.println("📡 MQTT 발행: " + selected.getDevice_name() + " → " + newStatus);
		} else {
			System.out.println("❌ 상태 변경 실패!");
		}
	}

	// 사용자용: 조회만 가능
	public void handleRoomDeviceUser() {
		System.out.print("호실명 입력 (예: 101A): ");
		String room_name = sc.nextLine();

		List<RoomDeviceDTO> devices = service.getDeviceList(room_name);
		
		if (devices.isEmpty()) {
			System.out.println("❌ 해당 호실 기기 없음");
			return;
		}

		System.out.println("\n=== " + room_name + " 호실 기기 현황 (사용자) ===");
		int idx = 1;
		
		for (RoomDeviceDTO d : devices) {
			if (d.getDevice_type().equals("DHT")) {
				System.out.printf("%d. %s (%s) - 온도: %.1f°C, 습도: %.1f%%\n",
					idx++, d.getDevice_name(), d.getDevice_type(), 
					d.getTemperature(), d.getHumidity());
			} else {
				System.out.printf("%d. %s (%s) - 상태: %s\n",
					idx++, d.getDevice_name(), d.getDevice_type(), 
					d.getStatus());
			}
		}

		System.out.println("\n기기를 선택해서 현황을 확인하세요:");
		System.out.print("선택 (1~" + devices.size() + "): ");
		int choice = sc.nextInt();
		sc.nextLine();

		if (choice < 1 || choice > devices.size()) {
			System.out.println("❌ 잘못된 선택");
			return;
		}

		RoomDeviceDTO selected = devices.get(choice - 1);
		
		System.out.println("\n=== " + selected.getDevice_name() + " 상세정보 ===");
		System.out.println("기기명: " + selected.getDevice_name());
		System.out.println("종류: " + selected.getDevice_type());
		
		if (selected.getDevice_type().equals("DHT")) {
			System.out.println("온도: " + selected.getTemperature() + "°C");
			System.out.println("습도: " + selected.getHumidity() + "%");
		} else if (selected.getDevice_type().equals("FAN")) {
			System.out.println("현재상태: " + selected.getStatus());
			System.out.println("📌 팬 자동 제어: 온도 ≥28°C 또는 습도 ≥50%일 때 자동 작동");
		} else {
			System.out.println("현재상태: " + selected.getStatus());
		}
		
		System.out.println("\n✅ 조회 완료!");
	}
}
