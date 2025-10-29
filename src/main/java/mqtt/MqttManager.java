package mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MqttManager implements MqttCallback, Runnable {
    // ---- config 파일에서 가져온 BROKER 정보를 Properties로 가져오는 작업 ---- //
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
    // ✅ 토픽별로 리스너 목록을 저장할 Map 선언
    private final Map<String, List<OnMessageCallback>> topicListeners = new ConcurrentHashMap<>();

    public MqttManager() {
        // 생성자에서 broker server 변수 설정
        String broker_ip = props.getProperty("broker.ip");
        String broker_port = props.getProperty("broker.port");
        this.broker = "tcp://"+broker_ip+":"+broker_port;
        this.clientId = "java" + UUID.randomUUID().toString();
    }

    // Thread 단위로 Mqtt Connect를 하는 작업, 메인 Thread와 구분됨
    @Override
    public void run() {
        try {
            this.client = new MqttClient(this.broker, this.clientId, new MemoryPersistence());
            //System.out.println("Connecting to broker server: " + this.broker);
            // 연결 옵션 설정
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // MqttCallback 인터페이스를 현재 클래스가 구현했으므로 this로 설정
            this.client.setCallback(this);
            this.client.connect(connOpts);
            this.isConnected = true;
            //System.out.println("✅ Broker Connected. Listening for messages...");
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
            //System.out.println("Subscribed to topic: " + subTopic);
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
            //System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(1); // QoS Level 1
            this.client.publish(topic, message);
            System.out.println("Message published.");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
    //
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
    
 // 센서 데이터 구독
 	public void subscribeSensorData(IMqttMessageListener callback) {
 		try {
 			if (!isConnected) {
 				System.out.println("🔌 MQTT 재연결 시도...");
 				run();
 			}

 			if (isConnected) {
 				String sensorTopic = "office/+/sensor_data";
 				this.client.subscribe(sensorTopic, 1, callback);
 				System.out.println("📥 구독: " + sensorTopic);
 			}
 		} catch (MqttException e) {
 			System.err.println("❌ MQTT 구독 실패: " + e.getMessage());
 		}
 	}

    /**
     * ✅ 특정 토픽에 대한 리스너(콜백)를 '추가'하는 메서드
     * @param topic 수신할 토픽
     * @param callback 해당 토픽의 메시지를 처리할 콜백
     */
    public void addListener(String topic, OnMessageCallback callback) {
        // computeIfAbsent: Map에 topic 키가 없으면 새 리스트를 만들고, 있으면 기존 리스트 반환
        topicListeners.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(callback);
        // 리스너가 등록되면 해당 토픽을 자동으로 구독합니다.
        this.subscribe(topic);
    }

    /**
     * MQTT 토픽이 와일드카드 패턴과 일치하는지 확인하는 헬퍼 메서드
     * @param pattern 구독 패턴 (예: "office/+/sensor")
     * @param topic 실제 발행된 토픽 (예: "office/1/sensor")
     * @return 일치하면 true
     */
    private boolean topicMatches(String pattern, String topic) {
        // 와일드카드 문자를 정규식(Regex)으로 변환하여 매칭
        String regex = pattern.replace("+", "[^/]+").replace("#", ".+");
        return topic.matches(regex);
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

        // ✅ 등록된 모든 리스너 패턴(Key)을 순회하며 매칭되는지 확인
        for (String topicPattern : topicListeners.keySet()) {
            if (topicMatches(topicPattern, topic)) {
                System.out.printf("  -> Pattern '%s' matched. Executing listeners...\n", topicPattern);
                // 매칭되는 패턴에 등록된 모든 리스너를 실행
                for (OnMessageCallback listener : topicListeners.get(topicPattern)) {
                    listener.handle(topic, msg);
                }
            }
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
         // System.out.println("Delivery complete.");
    }
}
