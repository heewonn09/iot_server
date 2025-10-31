package controller;

import dao.OfficeDAO;
import dto.MemberDTO;
import dto.OfficeDTO;
import service.RoomDeviceService;
import service.RoomDeviceServiceImpl;
import dto.DeviceDTO;
import mqtt.MqttManager;
import view.DeviceUI;
import view.MainUI;

import java.util.List;
import java.util.Scanner;

public class RoomDeviceController {
	private final RoomDeviceService service;
    private final DeviceUI view = new DeviceUI();


	public RoomDeviceController(MqttManager mqttManager) {
		service = new RoomDeviceServiceImpl(mqttManager);
	}
    // 특정 Office Info 출력
    public OfficeDTO getOfficeInfo(){
        Scanner sc = new Scanner(System.in);
        MainUI view = new MainUI();
        OfficeDAO dao = new OfficeDAO();
        List<OfficeDTO> list = dao.getAllOfficeInfo();
        view.showOfficeUI(list);
        System.out.print("이용하려는 Office를 선택하세요("+1+"-"+list.size()+"): ");
        int select = sc.nextInt();
        OfficeDTO dto = list.get(select - 1);
        return dto;
    }

    // office가 가진 device 리스트 출력
    public List<DeviceDTO> getDeviceList(int officeId, String officeName){
        List<DeviceDTO> devices = service.getDeviceList(officeId,officeName);

        if (devices.isEmpty()) {
            System.out.println("❌ 해당 호실 기기 없음");
            return null;
        }
        return devices;
    }

	// 관리자용: 제어 가능
	public void handleRoomDeviceAdmin() {
        Scanner sc = new Scanner(System.in);
        OfficeDTO dto = getOfficeInfo();
        List<DeviceDTO> devices = getDeviceList(dto.getOfficeId(),dto.getName());

        System.out.println("\n=== " + dto.getOfficeId() + " 호실 기기 목록 (관리자, 제어용) ===");
        view.printDevice(devices);

		System.out.print("\n기기번호 선택: ");
		int choice = sc.nextInt();
		sc.nextLine();

		if (choice < 1 || choice > devices.size()) {
			System.out.println("❌ 잘못된 선택");
			return;
		}

		DeviceDTO selected = devices.get(choice - 1);

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
	public void handleRoomDeviceUser(MemberDTO user) {
        Scanner sc = new Scanner(System.in);
        OfficeDTO dto = getOfficeInfo();

        List<DeviceDTO> devices = getDeviceList(dto.getOfficeId(),dto.getName());

		System.out.println("\n=== " + dto.getName() + " 호실 기기 현황 (사용자) ===");
        view.printDevice(devices);
		System.out.println("\n기기를 선택해서 현황을 확인하세요:");
		System.out.print("선택 (1~" + devices.size() + "): ");
		int choice = sc.nextInt();
		sc.nextLine();

		if (choice < 1 || choice > devices.size()) {
			System.out.println("❌ 잘못된 선택");
			return;
		}

		DeviceDTO selected = devices.get(choice - 1);
		
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
