package mqtt.devices;// DhtHandler.java
import java.util.concurrent.BlockingQueue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import mqtt.OnMessageCallback;

public class ELVHandler implements OnMessageCallback {

    private final BlockingQueue<String> queue;

    public ELVHandler(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void handle(String topic, String payload) {
        try {
            // View를 직접 호출하지 않고, 큐에 데이터만 넣고 끝냅니다.
            JsonObject obj = new Gson().fromJson(payload, JsonObject.class);
            String action = obj.get("action").getAsString();
            switch (action){
                case "enable":
                case "disable":
                case "arrive":
                  
                    queue.put(payload);
                    break;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("데이터를 큐에 추가하는 중 오류 발생");
        }
    }
}