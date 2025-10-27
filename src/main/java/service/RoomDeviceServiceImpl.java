package service;

import dao.RoomDeviceDAO;
import dao.RoomDeviceDAOImpl;
import dto.RoomDeviceDTO;
import mqtt.MqttManager;
import mqtt.SensorSubscriber;
import java.util.List;

public class RoomDeviceServiceImpl implements RoomDeviceService {

	private RoomDeviceDAO dao = new RoomDeviceDAOImpl();
	private static boolean mqttInitialized = false;

	public RoomDeviceServiceImpl() {
		if (!mqttInitialized) {
			initializeMqtt();
			mqttInitialized = true;
		}
	}

	private void initializeMqtt() {
		try {
			System.out.println("🚀 MQTT 초기화 중...");
			MqttManager.connect();
			SensorSubscriber sensorListener = new SensorSubscriber();
			MqttManager.subscribeSensorData(sensorListener);
			System.out.println("✅ MQTT 초기화 완료");
		} catch (Exception e) {
			System.err.println("❌ MQTT 초기화 오류: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public List<RoomDeviceDTO> getDeviceList(String room_name) {
		return dao.selectByRoom(room_name);
	}

	@Override
	public boolean controlDevice(int room_id, String device_name, String status) {
		int result = dao.updateStatus(room_id, device_name, status);
		return result > 0;
	}
}