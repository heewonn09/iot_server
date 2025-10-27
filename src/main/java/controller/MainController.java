package controller;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import dto.LoginUserDTO;
import dto.MemberDTO;
//import dto.UserSessionDTO;
//import mqtt.MqttManager;
//import service.MemberService;
//import service.MemberServiceImpl;
import mqtt.MqttManager;
import util.DeviceTypeList;
import service.UserService;
import service.UserServiceImpl;
import controller.AccessController;
import view.MainUI;

public class MainController {
	private MemberDTO currentUser = null; // 현재 로그인한 사용자 정보
    private final MainUI view = new MainUI(); // 화면을 담당할 View 객체
    private MqttManager mqttManager;
    private ArrayList<String> devices = DeviceTypeList.getDevices();

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

        // subscribe 정보 입력받기
        // topic : {officeId}/{deviceType}/{deviceID}/state
        // 주기적으로 받아야 할 센서 데이터들의 토픽을 넣는다.
        for(int i=0;i<4;i++){ // officeId 는 0 ~ 3 범위
            for (String deviceType : devices) {
                mqttManager.subscribe(i+"/"+deviceType+"/+/state"); // 싱글레벨 와일드카드
            }
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
		FireController fireController = new FireController();
		ParkedController adminParkedController = new ParkedController();
		switch(input) {
		HwAdminController adminParkedController = new HwAdminController();
        switch(input) {
			case 1: // 출입
				accessController.handleAccess(currentUser);
				break;
			case 2:
                ElevatorController evController = new ElevatorController(currentUser,mqttManager);
                evController.adminAccess();
				break;
			case 3:
				break;
			case 4:
				adminParkedController.adminParked(currentUser);
				break;
			case 5: // 관리자, 층 관리자 화재 모드 진입
				fireController.handleFireMode(currentUser);
				break;
			case 6:
				break;
		}
	}
	private void userMenu() {
		int input = MainUI.userUI();
		AccessController accessController = new AccessController();
		FireController fireController = new FireController();
		ParkedController userParkedController = new ParkedController();
        HwMainController userParkedController = new HwMainController();
        switch(input) {
			case 1: // 출입
				accessController.handleAccess(currentUser);
				break;
			case 2:
                ElevatorController evController = new ElevatorController(currentUser,mqttManager);
                evController.userAccess();
				break;
			case 3:
				break;
			case 4:
				userParkedController.userhandleAccess(currentUser);
				break;
			case 5: // 일반 사용자용 화재 모드 진입
				fireController.handleFireMode(currentUser);
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