package service;

import java.util.List;

import dao.RoomDeviceDAO;
import dao.RoomDeviceDAOImpl;
import dto.RoomDeviceDTO;
import mqtt.MqttManager;
import mqtt.SensorSubscriber;

public class RoomDeviceServiceImpl implements RoomDeviceService {
	private MqttManager mqttManager;
	private RoomDeviceDAO dao;

	public RoomDeviceServiceImpl(MqttManager mqttManager) {
		this.mqttManager = mqttManager;
		dao = new RoomDeviceDAOImpl(this.mqttManager);
		SensorSubscriber sensorListener = new SensorSubscriber();
		this.mqttManager.subscribeSensorData(sensorListener);
	}


	@Override
	public List<RoomDeviceDTO> getDeviceList(int officeId,String officeName) {
		return dao.selectByRoom(officeId,officeName);
	}

	@Override
	public boolean controlDevice(int room_id, String device_name, String status) {
		int result = dao.updateStatus(room_id, device_name, status);
		return result > 0;
	}
}