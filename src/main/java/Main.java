import controller.*;
import mqtt.MqttManager;
import service.UserService;
import service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        // 1. 애플리케이션에 필요한 모든 부품(객체)들을 생성합니다.
        MqttManager mqttManager = new MqttManager();
        UserService userService = new UserServiceImpl();
        // ... 필요한 다른 DAO, Service 객체들 ...

        // 2. 각 기능별 컨트롤러들을 생성합니다. (MQTT 매니저 등을 주입)
        AccessController accessController = new AccessController(mqttManager);
        FireController fireController = new FireController(mqttManager);
        ParkedController parkedController = new ParkedController(mqttManager);
        RoomDeviceController roomDeviceController = new RoomDeviceController(mqttManager);

        // 3. 인증을 담당할 AuthController 생성 (UserService 주입)
        AuthController authController = new AuthController(userService);

        // 4. 메인 컨트롤러를 생성하고, 필요한 모든 부품들을 주입해줍니다.
        MainController mainController = new MainController(
                authController,
                accessController,
                fireController,
                parkedController,
                roomDeviceController,
                mqttManager
        );

        // 5. 애플리케이션 실행!
        mainController.run();
    }
}