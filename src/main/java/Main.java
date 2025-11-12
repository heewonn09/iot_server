import controller.*;
import mqtt.MqttManager;
import service.UserService;
import service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        // 1. 애플리케이션에 필요한 모든 객체들 생성합니다.
        MqttManager mqttManager = new MqttManager();
        UserService userService = new UserServiceImpl();

        // 2. 각 기능별 컨트롤러들을 생성 및 매개변수 주입
        AccessController accessController = new AccessController(mqttManager); //출입문 컨트롤러
        FireController fireController = new FireController(mqttManager); //화재감지 컨트롤러
        ParkedController parkedController = new ParkedController(mqttManager); //주차관리 컨트롤러
        RoomDeviceController roomDeviceController = new RoomDeviceController(mqttManager); //디바이스 컨트롤러
        AuthController authController = new AuthController(userService); //권한 컨트롤러

        // 4. 메인 컨트롤러를 생성하고, 관련된 매개변수 객체 주입
        MainController mainController = new MainController(
                authController,
                accessController,
                fireController,
                parkedController,
                roomDeviceController,
                mqttManager
        );

        // 5. 애플리케이션 실행
        mainController.run();
    }
}