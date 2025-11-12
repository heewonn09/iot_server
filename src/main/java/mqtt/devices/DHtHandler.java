package mqtt.devices;// DhtHandler.java
import mqtt.OnMessageCallback;

public class DHtHandler implements OnMessageCallback {

    private int officeId;
    public DHtHandler(int officeId) {
        this.officeId = officeId;
    }

    @Override
    public void handle(String topic, String payload) {
        // 이 핸들러는 DHT 관련 토픽만 받아서 처리한다.
        System.out.printf("[DHT Handler for Office %d] 데이터 수신: %s -> %s\n", officeId, topic, payload);

        // 아래 코드에서 topic에 따라 로직을 구현해야된다.
        // 여기서 JSON 파싱, DB 저장, View 업데이트 등의 로직을 수행합니다.
        // ex) DhtData data = new Gson().fromJson(payload, DhtData.class);
        //     dhtService.save(data);
    }
}