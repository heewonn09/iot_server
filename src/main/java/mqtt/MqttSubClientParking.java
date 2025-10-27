package mqtt;

import java.util.Arrays;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubClientParking {

    private MqttClient client;
    private final String broker = "tcp://192.168.14.56:1883";
    private final String[] topics = {
        "1/parking/01/car",    // 차량 감지
        "1/door/05/state"  		// 차단기 상태
        
    };


    public void start() { 
        try {
            client = new MqttClient(broker, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);

            System.out.println("✅ MQTT 연결 성공 (주차 게이트 통신 시작)");

            // 구독 설정
            for (String t : topics) {
                client.subscribe(t);
                System.out.println("📡 구독 시작 → " + t);
            }

            // 콜백 정의
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("⚠️ 연결 끊김: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    System.out.println(" [수신] " + topic + " → " + payload);

                   
                    if (topic.equals("1/parking/01/car")) {
                        String carNo = parseValue(payload, "car_no");
                        System.out.println("차량번호 감지됨 → " + carNo);

                        boolean authorized = checkCarRegistered(carNo);
                        String status = authorized ? "authorized" : "unauthorized";

                        String resultMsg = "{\"status\":\"" + status + "\"}";
                        publish("1/parking/01/auth", resultMsg);

                        System.out.println(" 차량 인증 결과 전송 → " + status);
                    }

                    // 2️⃣ 차단기 상태 수신
                    else if (topic.equals("1/door/05/state")) {
                        String state = parseValue(payload, "state");
                        System.out.println("🚪 차단기 상태 수신 → " + state.toUpperCase());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {}
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
    private String parseValue(String payload, String key) {
        try {
            int start = payload.indexOf(key);
            if (start == -1) return "unknown";
            int colon = payload.indexOf(':', start);
            int firstQuote = payload.indexOf('"', colon + 1);
            int secondQuote = payload.indexOf('"', firstQuote + 1);
            return payload.substring(firstQuote + 1, secondQuote);
        } catch (Exception e) {
            return "unknown";
        }
    }

    // ✅ 등록 차량 임시 목록 (나중에 DB 연동 가능)
    private boolean checkCarRegistered(String carNo) {
        List<String> registeredCars = Arrays.asList("397로1075", "222나2222", "333다3333", "111가1111", "123가1234");
        return registeredCars.contains(carNo);
    }

    // ✅ MQTT Publish 메서드
    private void publish(String topic, String msg) {
        try {
            MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
            mqttMessage.setQos(0);
            client.publish(topic, mqttMessage);
        } catch (Exception e) {
            System.out.println("⚠️ Publish 실패: " + e.getMessage());
        }
    }
}
