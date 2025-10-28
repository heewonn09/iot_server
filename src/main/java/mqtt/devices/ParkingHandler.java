package mqtt.devices;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dao.ParkingDAO;
import dto.mqttMsg.MqttGateCommandDTO;
import dto.mqttMsg.MqttParkingDTO;
import mqtt.MqttManager;
import mqtt.OnMessageCallback;

public class ParkingHandler implements OnMessageCallback {

    private final ParkingDAO parkingDAO;
    private final MqttManager mqttManager;
    private final Gson gson = new Gson();


 

    
    private static final String GATE_COMMAND_TOPIC = "parking/gate/cmd";

    public ParkingHandler(ParkingDAO parkingDAO, MqttManager mqttManager) {
        this.parkingDAO = parkingDAO;
        this.mqttManager = mqttManager;
    }
    @Override
    public void handle(String topic, String payload) {
        try {
            MqttParkingDTO dto = gson.fromJson(payload, MqttParkingDTO.class);
            if (dto == null || dto.getCarNo() == null) {
                System.out.printf("⚠️ 파싱된 차량 데이터가 없어 무시합니다. topic=%s payload=%s%n", topic, payload);
                return;
            }

           
            boolean authorized = parkingDAO.processVehicleLog(dto.getCarNo(), dto.getAction());
            publishGateCommand(dto.getCarNo(), authorized);
        } catch (JsonSyntaxException ex) {
            System.out.printf("⚠️ 차량 로그 JSON 파싱 실패: %s%n", ex.getMessage());
        }
    }


    private void publishGateCommand(String carNo, boolean authorized) {
        if (mqttManager == null) {
            System.out.println("⚠️ MQTT 매니저가 초기화되지 않아 게이트 명령을 전송하지 못했습니다.");
            return;
        }

        String command = authorized ? "OPEN" : "KEEP_CLOSED";
        MqttGateCommandDTO response = new MqttGateCommandDTO(command, carNo, authorized);
        String message = gson.toJson(response);

        mqttManager.publish(GATE_COMMAND_TOPIC, message);
        System.out.printf("🚦 게이트 명령 전송: %s -> %s%n", carNo, command);
    }
}