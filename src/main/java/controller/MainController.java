package controller;

import static util.ColorUtil.*;
import dto.MemberDTO;
import mqtt.MqttManager;
import view.MainUI;

public class MainController {

	private MemberDTO currentUser; // 현재 로그인한 사용자 정보
    private final MainUI view;
    private ElevatorController evController = null; // 엘리베이터 기능을 담당

    // --- 의존성 주입 ---
    private final AuthController authController;
    private final AccessController accessController;
    private final FireController fireController;
    private final ParkedController parkedController;
    private final RoomDeviceController roomDeviceController;
    private final MqttManager mqttManager;

    public MainController(AuthController auth, AccessController access, FireController fire, ParkedController park, RoomDeviceController roomDevice, MqttManager mqtt) {
        this.currentUser = null;
        this.view = new MainUI();
        this.authController = auth;
        this.accessController = access;
        this.fireController = fire;
        this.parkedController = park;
        this.roomDeviceController = roomDevice;
        this.mqttManager = mqtt;

        settingDevice();
    }

    // Mqtt 브로커 서버와 연결
    public void settingDevice(){
        Thread mqttThread = new Thread(mqttManager);
        mqttThread.start();
        System.out.println("🚀 Main thread started MQTT connection thread.");

        // 메인 스레드가 바로 종료되는 것을 방지하기 위해 잠시 대기
        try {
            // 스레드가 연결될 시간을 잠시 줍니다.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while (true) {
            if (currentUser == null) {
                // 로그인되지 않았을 때의 로직 처리
            	loginOrRegisterMenu();
            } else {
                // 로그인된 후의 로직 처리
                handleMainMenu();
            }
        }
    }
    private void loginOrRegisterMenu() {
    	int sel = view.loginRegisterUI();
        switch (sel) {
            case 1 -> {
                currentUser = authController.loginMenu();
            }
            case 2 -> authController.registerMenu();
            default -> exitProgram();

        }
    }
    private void handleMainMenu() {
        // Python -> Java 로 토픽 받을 디바이스에 관련된 topic을 subscribe하는 작업
        if(evController == null){
            int officeId = 1; //아직 엘리베이터가 프로젝트 내에 한 대로만 운영 중임
            int deviceId = 14; // DB에 연동된 엘리베이터 deviceId, officeId를 숫자로 적어내는 임시방편을 사용함
            evController = new ElevatorController(currentUser, mqttManager,officeId,deviceId);
        }
		int role = currentUser.getAccess_level();
		switch (role) {
		case 3:
		case 2:
			adminMenu(); // 관리자 페이지 이동
			break;
		case 1:
			userMenu(); // 사용자 페이지 이동
			break;
		default:
			System.out.println("error");
			break;
		}
	}
    private void adminMenu() {
		int input = view.adminUI();
		switch (input) {
            case 1: // 출입
                accessController.handleAccess(currentUser);
                break;
            case 2:
                evController.adminAccess();
                break;
            case 3:
                roomDeviceController.handleRoomDeviceAdmin();
                break;
            case 4:
                parkedController.adminParked(currentUser);
                break;
            case 5: // 관리자, 층 관리자 화재 모드 진입
                fireController.handleFireMode(currentUser);
                break;
            case 6:
                logout();
                break;
		}
	}
	private void userMenu() {
		int input = view.userUI();
		switch (input) {
		case 1: // 출입
			accessController.handleAccess(currentUser);
			break;
		case 2:
			evController.userAccess();
			break;
		case 3:
			roomDeviceController.handleRoomDeviceUser(currentUser);
			break;
		case 4:
			parkedController.userhandleAccess(currentUser);
			break;
		case 5: // 일반 사용자용 화재 모드 진입
			fireController.handleFireMode(currentUser);
			break;
		case 6:
			logout();
			break;
		}
	}
	private void logout() {
		currentUser = null;
		evController= null;
        System.out.println("로그아웃합니다.");
	}
	private void exitProgram() {
        System.out.println(RED + "⚠️ 잘못된 입력입니다. 프로그램을 종료합니다." + RESET);
		System.exit(0);
	}
}