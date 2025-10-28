package mqtt;

import com.google.gson.Gson;

public class MqttSubClientParking {

    private final MqttManager mqttManager;
    private final Gson gson = new Gson();

    public MqttSubClientParking() {
        this.mqttManager = new MqttManager();
    }

    public void start() {
        // ✅ MQTT 연결 스레드
        Thread mqttThread = new Thread(mqttManager);
        mqttThread.setDaemon(true);
        mqttThread.start();

        System.out.println("⏳ MQTT 브로커 연결 대기 중...");
        for (int i = 0; i < 10; i++) {
            if (mqttManager.isConnected()) {
                System.out.println("✅ MQTT 연결 완료. 리스너 등록 시작...");
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!mqttManager.isConnected()) {
            System.out.println("❌ MQTT 연결 실패로 리스너 등록 불가. 프로그램 종료");
            return;
        }

        // ✅ 차량 감지 로그 리스너 등록
        mqttManager.addListener("1/parking/01/car", (topic, message) -> handleCarDetected(message));
        System.out.println("📡 구독 완료 → 1/parking/01/car");
    }

    // 🚗 차량 감지 메시지 처리 (로그만 출력)
    private void handleCarDetected(String payload) {
        try {
            var dto = gson.fromJson(payload, dto.mqttMsg.MqttParkingDTO.class);
            System.out.println("🚗 차량 감지됨 → 번호: " + dto.getCarNo());
        } catch (Exception e) {
            System.out.println("⚠️ 차량 감지 처리 중 오류: " + e.getMessage());
        }
    }
}
