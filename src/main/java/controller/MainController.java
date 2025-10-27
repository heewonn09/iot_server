package controller;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import dto.LoginUserDTO;
import dto.MemberDTO;
import mqtt.MqttManager;
import mqtt.devices.DHtHandler;
import mqtt.devices.ELVHandler;
import service.UserService;
import service.UserServiceImpl;
import view.MainUI;

public class MainController {
	private MemberDTO currentUser = null; // 현재 로그인한 사용자 정보
    private final MainUI view = new MainUI(); // 화면을 담당할 View 객체
    private MqttManager mqttManager;
    private ElevatorController evController;


    public MainController() {
        currentUser = null;
        mqttManager = new MqttManager();
    }

    // 브로커 서버와 연결, subscribe topic 설정
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
        // 2. ✅ 각 전문 컨트롤러들을 생성하여 필요한 MqttManager를 주입 (의존성 주입)
        evController = new ElevatorController(currentUser, mqttManager);
        // DHtController dhtController = new DhtController(mqttManager); // 예시
        // LedController ledController = new LedController(mqttManager); // 예시

        System.out.println("✅ All device controllers have been initialized and listeners are set.");
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
        Scanner sc = new Scanner(System.in);
        System.out.println("=== 스마트 빌딩 시스템 ===");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.print(">>>> 선택 : ");
        int sel = sc.nextInt();
        sc.nextLine(); // flush

        switch (sel) {
            case 1 -> loginMenu();
            case 2 -> registerMenu();
            default -> System.out.println("잘못된 입력입니다.");
        }
    }
    
    private void loginMenu() {
    	LoginUserDTO loginInfo = view.loginUI();
    	UserService serv = new UserServiceImpl();
    	currentUser = serv.login(loginInfo.getId(), loginInfo.getPw());
    	if (currentUser == null) {
            System.out.println("❌ 로그인 실패. 아이디 혹은 비밀번호를 확인하세요.");
        } else {
            System.out.printf("✅ 로그인 성공 (%s님, 등급:%d)%n", currentUser.getName(), currentUser.getAccess_level());
        }
    }
    private void registerMenu() {
        String[] info = view.registerUI();
        UserService serv = new UserServiceImpl();
        boolean result = serv.register(info[0], info[1], info[2]);
        if (result) {
            System.out.println("✅ 회원가입 완료! 로그인 후 이용해주세요.");
        } else {
            System.out.println("❌ 회원가입 실패. 아이디 중복 또는 DB 오류입니다.");
        }
    }
    
	private void handleMainMenu() {
        settingDevice(); // 이걸 어디다 배치를 해야지??
		int role = currentUser.getAccess_level();
		switch (role){
	        case 3:
	        case 2:
	            adminMenu(); //관리자 페이지 이동
	            break;
	        case 1:
	            userMenu(); //사용자 페이지 이동
	            break;
	        default:
	            System.out.println("error");
	            break;
	    }
    }
	private void adminMenu() {
		int input = MainUI.adminUI();
		AccessController accessController = new AccessController();
		ParkedController adminParkedController = new ParkedController();
		switch(input) {
			case 1: // 출입
				accessController.handleAccess(currentUser);
				break;
			case 2:
                evController.adminAccess();
				break;
			case 3:
				break;
			case 4:
				adminParkedController.adminParked(currentUser);
				break;
			case 5:
				break;
			case 6:
				break;
		}
	}
	private void userMenu() {
		int input = MainUI.userUI();
		AccessController accessController = new AccessController();
		ParkedController userParkedController = new ParkedController();
		switch(input) {
			case 1: // 출입
				accessController.handleAccess(currentUser);
				break;
			case 2:
                evController.userAccess();
				break;
			case 3:
				break;
			case 4:
				userParkedController.userhandleAccess(currentUser);
				break;
			case 5:
				break;
			case 6:
				break;
		}
	}
	
	private void logout() {
		// TODO Auto-generated method stub
		
	}
	private void exitProgram() {
		// TODO Auto-generated method stub
		
	}
}