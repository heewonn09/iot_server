package mqtt;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MqttManager implements MqttCallback, Runnable { // MqttCallback을 직접 구현
    private static final String PROPERTIES_FILE = "src/main/java/config/broker.properties";
    private static Properties props;
    static {
        try {
            FileInputStream fis = new FileInputStream(PROPERTIES_FILE);

            props = new Properties();
            props.load(fis);

            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	private String id;
    private MqttClient client;
    private final String broker;
    private final String clientId;
    private volatile boolean isConnected = false;

    public MqttManager() {
        // broker server 변수 설정
        String broker_ip = props.getProperty("broker.ip");
        String broker_port = props.getProperty("broker.port");
        this.broker = "tcp://"+broker_ip+":"+broker_port;
        this.clientId = "java" + UUID.randomUUID().toString();
    }

    // ✅ 1. 리스너들을 저장할 Map 추가 (Thread Safe 자료구조 -> ConcurrentHashMap 사용)
    // Key: 토픽(Str), Value: 해당 토픽을 구독하는 리스너 리스트
    private final Map<String, List<MqttTopicListener>> topicListeners = new ConcurrentHashMap<>();

    @Override
    public void run() {
        try {
            this.client = new MqttClient(this.broker, this.clientId, new MemoryPersistence());
            System.out.println("Connecting to broker: " + this.broker);
            System.out.println("clientId: "+this.clientId);
            // 연결 옵션 설정
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // MqttCallback 인터페이스를 현재 클래스가 구현했으므로 this로 설정
            this.client.setCallback(this);
            this.client.connect(connOpts);
            this.isConnected = true;
            System.out.println("✅ Broker Connected. Listening for messages...");
        } catch (MqttException e) {
            System.err.println("MQTT Connection Error!");
            this.isConnected = false;
            throw new RuntimeException(e);
        }
    }
    // 구독을 처리하는 메소드
    public void subscribe(String subTopic) {
        try {
            this.client.subscribe(subTopic);
            System.out.println("Subscribed to topic: " + subTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    
    // 발행을 처리하는 메소드
    public void publish(String topic, String content) {
        if (!isConnected || this.client == null) {
            System.err.println("MQTT client is not connected. Cannot publish message.");
            return; // 연결 안 됐으면 그냥 리턴
        }
        try {
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(1); // QoS Level 1
            this.client.publish(topic, message);
            System.out.println("Message published.");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
    
    // 연결을 종료하는 메소드
    public void close() {
        try {
            this.client.disconnect();
            this.client.close();
            System.out.println("Disconnected.");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // ---- MqttTopicListener 인터페이스 관련 메서드들 ---- //
    // ✅ 2. 리스너 등록 메서드
    public void addListener(String topic, MqttTopicListener listener) {
        // computeIfAbsent: topic 키가 없으면 새 리스트를 만들고, 있으면 기존 리스트를 반환
        topicListeners.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(listener);
        this.subscribe(topic); // 리스너가 등록되면 해당 토픽을 자동으로 구독
        System.out.println("Listener added for topic: " + topic);
    }

    // ✅ 3. 리스너 삭제 메서드
    public void removeListener(String topic, MqttTopicListener listener) {
        if (topicListeners.containsKey(topic)) {
            topicListeners.get(topic).remove(listener);
        }
    }

    // ---- MqttCallback 인터페이스의 메소드들 ---- //

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // 이 메소드는 메시지가 도착할 때마다 Paho 라이브러리에 의해 자동으로 호출됩니다.
        String msg = new String(message.getPayload());
        System.out.println("\n=============== MESSAGE ARRIVED ===============");
        System.out.println(" Topic: " + topic);
        System.out.println(" Message: " + msg);
        System.out.println("=============================================");

        // ✅ 4. 더 이상 직접 처리하지 않음!
        // 이 토픽에 등록된 리스너가 있는지 확인
        if (topicListeners.containsKey(topic)) {
            // 해당 토픽의 모든 리스너에게 메시지를 전달
            for (MqttTopicListener listener : topicListeners.get(topic)) {
                try {
                    listener.onMessageReceived(topic, msg);
                } catch (Exception e) {
                    System.err.println("Error processing message in listener: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No listener registered for topic: " + topic);
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
         System.out.println("Delivery complete.");
    }
}