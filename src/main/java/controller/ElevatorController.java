package controller;

import dto.MemberDTO;
import mqtt.MqttManager;
import service.ElevatorService;
import service.ElevatorServiceImpl;
import view.ElevatorUI;

public class ElevatorController {
    private MemberDTO loginUser;
    private ElevatorUI view;
    private ElevatorService evService;
    private MqttManager mqttManager;

    public ElevatorController(MemberDTO loginUser, MqttManager mqttManager) {
        this.loginUser = loginUser;
        this.view = new ElevatorUI();
        this.mqttManager = mqttManager;
        this.evService = new ElevatorServiceImpl(loginUser,mqttManager);
    }
    // 관리자 권한으로 로그인 했을 때 사용되는 메서드
    public void adminAccess(){
        while (true){
            int input = view.adminUI();
            // 권한 관리 메뉴
            if(input == 1){
                authController();
            }
            // 조작 메뉴
            else if (input == 2) {
                stateController();
            }
            else{
                System.out.println("페이지를 종료합니다.");
                break;
            }
        }
    }
    // 사용자로 로그인했을 때 실행되는 메서드
    public void userAccess(){
        while(true){
            int input = view.userUI();
            // 엘리베이터 호출
            if(input == 1){

            }
            else{
                System.out.println("페이지를 종료합니다.");
            }
        }
    }

    public void authController() {
        // 엘리베이터의 권한이란 어떤 권한인가?
        // => 결론: access_level: 1-2 인 경우, 해당 office_id 에 대해서 office_id 의 floor 에 대한 엘리베이터 권한을 갖는 것임.
        // 3인 경우, 모든 floor 에 대한 엘리베이터 권한을 가짐.
        int input = view.authUI();
        switch (input){
            case 1: // 권한 목록 조회 (어떻게 보여줄건지?)
                // 층별로 구분해서 보여줄건가?
                // 회원 컬럼 중 어떤 값들을 보여줄건가?
                break;
        }
    }

    public void stateController() {
        int input = view.stateUI();
        switch (input){
            case 1: //엘리베이터 상태 조회
                evService.getEVState();
                //view.stateSELECT(); //통신 후 가져온 데이터를 기반으로 show
                break;
            case 2: //엘리베이터 이용 상태 변경
                String result = view.stateSTOP();
                System.out.println(result);
                if(result.equals("y")){
                    System.out.println("엘리베이터 상태를 이용 가능으로 변경합니다.");
                    evService.setEVEnable(true);
                } else if (result.equals("n")) {
                    System.out.println("엘리베이터 상태를 이용 불가로 변경합니다.");
                    System.out.println("이제 엘리베이터를 이용할 수 없습니다.");
                    evService.setEVEnable(false);
                }
                break;
            case 3: //엘리베이터 위치 제어(원격 제어)  //1-3 로직은 모두 MQTT 통신과 관련된 기능
                int floor = view.floorControl();
                evService.setEVFloor(1,floor);
                break;
            case 4: //엘리베이터 통계 리포트 (DB로그 확인)

                break;
        }
    }
}
