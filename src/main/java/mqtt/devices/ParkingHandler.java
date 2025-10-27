package mqtt.devices;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dao.ParkingDAO;
import dto.mqttMsg.MqttParkingDTO;
import mqtt.OnMessageCallback;

public class ParkingHandler implements OnMessageCallback {

    private final ParkingDAO parkingDAO;
    private final Gson gson = new Gson();

    public ParkingHandler(ParkingDAO parkingDAO) {
        this.parkingDAO = parkingDAO;
    }

    @Override
    public void handle(String topic, String payload) {
        try {
            MqttParkingDTO dto = gson.fromJson(payload, MqttParkingDTO.class);
            if (dto == null || dto.getCarNo() == null) {
                System.out.printf("⚠️ 파싱된 차량 데이터가 없어 무시합니다. topic=%s payload=%s%n", topic, payload);
                return;
            }

            parkingDAO.processVehicleLog(dto.getCarNo(), dto.getAction());
        } catch (JsonSyntaxException ex) {
            System.out.printf("⚠️ 차량 로그 JSON 파싱 실패: %s%n", ex.getMessage());
        }
    }
}

