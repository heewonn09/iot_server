package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttManager {
	private static MqttClient mqttClient;
	private static final String BROKER_URL = "tcp://192.168.137.71:1883";
	private static final String CLIENT_ID = "SmartBuilding_Java";
	private static final int QOS = 1;
	private static boolean connected = false;

	// MQTT 브로커에 연결
	public static void connect() {
		try {
			if (mqttClient != null && mqttClient.isConnected()) {
				System.out.println("✅ MQTT 이미 연결됨");
				connected = true;
				return;
			}

			System.out.println("🔌 MQTT 브로커 연결 시도...");
			mqttClient = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setAutomaticReconnect(true);
			options.setConnectionTimeout(10);
			options.setKeepAliveInterval(31);
			options.setMaxInflight(100);
			
			mqttClient.connect(options);
			connected = true;
			
			System.out.println("✅ MQTT 브로커 연결 성공: " + BROKER_URL);
			System.out.println("📍 Client ID: " + CLIENT_ID);
			
		} catch (MqttException e) {
			connected = false;
			System.err.println("⚠️ MQTT 연결 실패: " + e.getMessage());
			System.err.println("⚠️ 에러 코드: " + e.getReasonCode());
			System.out.println("💡 MQTT 브로커(라즈베리파이)가 실행 중인지 확인하세요!");
			System.out.println("💡 라즈베리파이 터미널에서: mosquitto -v");
			e.printStackTrace();
		}
	}

	// LED 제어 발행
	public static void publishLedControl(String officeRoom, String action) {
		try {
			if (!isConnected()) {
				System.out.println("⚠️ MQTT 미연결 - 재연결 시도 중...");
				connect();
				if (!isConnected()) {
					System.out.println("❌ MQTT 연결 실패 - DB만 업데이트됩니다");
					return;
				}
			}

			String topic = "office/" + officeRoom + "/led";
			String message = "{\"action\":\"" + action.toUpperCase() + "\",\"device_id\":1}";
			MqttMessage mqttMessage = new MqttMessage(message.getBytes());
			mqttMessage.setQos(QOS);
			mqttMessage.setRetained(false);
			mqttClient.publish(topic, mqttMessage);
			System.out.println("📤 MQTT 발행 (LED): " + topic);
			System.out.println("   Payload: " + message);
		} catch (MqttException e) {
			System.err.println("⚠️ MQTT 발행 실패: " + e.getMessage());
		}
	}

	// AC(환풍펜) 제어 발행
	public static void publishAcControl(String officeRoom, String action) {
		try {
			if (!isConnected()) {
				System.out.println("⚠️ MQTT 미연결 - 재연결 시도 중...");
				connect();
				if (!isConnected()) {
					System.out.println("❌ MQTT 연결 실패 - DB만 업데이트됩니다");
					return;
				}
			}

			String topic = "office/" + officeRoom + "/ac";
			String message = "{\"action\":\"" + action.toUpperCase() + "\",\"device_id\":2}";
			MqttMessage mqttMessage = new MqttMessage(message.getBytes());
			mqttMessage.setQos(QOS);
			mqttMessage.setRetained(false);
			mqttClient.publish(topic, mqttMessage);
			System.out.println("📤 MQTT 발행 (AC): " + topic);
			System.out.println("   Payload: " + message);
		} catch (MqttException e) {
			System.err.println("⚠️ MQTT 발행 실패: " + e.getMessage());
		}
	}

	// FAN(쿨링팬) 제어 발행
	public static void publishFanControl(String officeRoom, String action) {
		try {
			if (!isConnected()) {
				System.out.println("⚠️ MQTT 미연결 - 재연결 시도 중...");
				connect();
				if (!isConnected()) {
					System.out.println("❌ MQTT 연결 실패 - DB만 업데이트됩니다");
					return;
				}
			}

			String topic = "office/" + officeRoom + "/fan";
			String message = "{\"action\":\"" + action.toUpperCase() + "\",\"device_id\":3}";
			MqttMessage mqttMessage = new MqttMessage(message.getBytes());
			mqttMessage.setQos(QOS);
			mqttMessage.setRetained(false);
			mqttClient.publish(topic, mqttMessage);
			System.out.println("📤 MQTT 발행 (FAN): " + topic);
			System.out.println("   Payload: " + message);
		} catch (MqttException e) {
			System.err.println("⚠️ MQTT 발행 실패: " + e.getMessage());
		}
	}

	// 센서 데이터 구독
	public static void subscribeSensorData(IMqttMessageListener callback) {
		try {
			if (!isConnected()) {
				System.out.println("🔌 MQTT 재연결 시도...");
				connect();
			}

			if (isConnected()) {
				String sensorTopic = "office/+/sensor_data";
				mqttClient.subscribe(sensorTopic, QOS, callback);
				System.out.println("📥 구독: " + sensorTopic);
			}
		} catch (MqttException e) {
			System.err.println("❌ MQTT 구독 실패: " + e.getMessage());
		}
	}

	// 연결 해제
	public static void disconnect() {
		try {
			if (mqttClient != null && mqttClient.isConnected()) {
				mqttClient.disconnect();
				mqttClient.close();
				connected = false;
				System.out.println("✅ MQTT 연결 종료");
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	// 연결 상태 확인
	public static boolean isConnected() {
		return mqttClient != null && mqttClient.isConnected() && connected;
	}
}


