package controller;

import dao.OfficeDAO;
import dto.OfficeDTO;
import service.RoomDeviceService;
import service.RoomDeviceServiceImpl;
import dto.RoomDeviceDTO;
import mqtt.MqttManager;
import view.MainUI;

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
    public OfficeDTO findRoomList(){
        MainUI view = new MainUI();
        OfficeDAO dao = new OfficeDAO();
        List<OfficeDTO> list = dao.getAllOfficeInfo();
        view.showOfficeUI(list);
        System.out.print("이용하려는 Office를 선택하세요("+1+"-"+list.size()+"): ");
        int select = sc.nextInt();
        OfficeDTO dto = list.get(select - 1);
        return dto;
    }
    public List<RoomDeviceDTO> findDeviceList(int id,String name){

        List<RoomDeviceDTO> devices = service.getDeviceList(id,name);

        if (devices.isEmpty()) {
            System.out.println("❌ 해당 호실 기기 없음");
            return null;
        }
        return devices;
    }
    public void printDevice(List<RoomDeviceDTO> devices){
        int idx = 1;
        for (RoomDeviceDTO d : devices) {
            if (d.getDevice_type().equals("DHT")) {
                System.out.printf("%d. %s (%s) - 온도: %.1f°C, 습도: %.1f%%\n",
                        idx++, d.getDevice_name(), d.getDevice_type(),
                        d.getTemperature(), d.getHumidity());
            } else if (d.getDevice_type().equals("HVAC")) {
                System.out.printf("%d. %s (%s) - 상태: %s 🌀\n",
                        idx++, d.getDevice_name(), d.getDevice_type(),
                        d.getStatus());
            } else {
                System.out.printf("%d. %s (%s) - 상태: %s\n",
                        idx++, d.getDevice_name(), d.getDevice_type(),
                        d.getStatus());
            }
        }
    }

	// 관리자용: 제어 가능
	public void handleRoomDeviceAdmin() {
        OfficeDTO dto = findRoomList();
        List<RoomDeviceDTO> devices = findDeviceList(dto.getOfficeId(),dto.getName());

        System.out.println("\n=== " + dto.getOfficeId() + " 호실 기기 목록 (관리자, 제어용) ===");
        printDevice(devices);

		System.out.print("\n기기번호 선택: ");
		int choice = sc.nextInt();
		sc.nextLine();

		if (choice < 1 || choice > devices.size()) {
			System.out.println("❌ 잘못된 선택");
			return;
		}

		RoomDeviceDTO selected = devices.get(choice - 1);

		// ✅ LED, HVAC만 제어 가능
		if (!selected.getDevice_type().equals("LED") &&
		    !selected.getDevice_type().equals("HVAC")) {
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
    //
	public void handleRoomDeviceUser() {
        OfficeDTO dto = findRoomList();
        List<RoomDeviceDTO> devices = findDeviceList(dto.getOfficeId(),dto.getName());

		System.out.println("\n=== " + dto.getName() + " 호실 기기 현황 (사용자) ===");
        printDevice(devices);
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
		} else if (selected.getDevice_type().equals("HVAC")) {
			System.out.println("현재상태: " + selected.getStatus());
			System.out.println("📌 팬 자동 제어: 온도 ≥28°C 또는 습도 ≥50%일 때 자동 작동");
		} else {
			System.out.println("현재상태: " + selected.getStatus());
		}
		
		System.out.println("\n✅ 조회 완료!");
	}
}
