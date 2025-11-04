package service;

import java.util.List;

import dao.RoomDeviceDAO;
import dao.RoomDeviceDAOImpl;
import dto.DeviceDTO;
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
	public List<DeviceDTO> getDeviceList(int officeId, String officeName) {
		return dao.getDeviceListByOffice(officeId,officeName);
	}

	@Override
	public boolean controlDevice(int room_id, int device_id, String status) {
		int result = dao.updateStatus(room_id, device_id, status);
		return result > 0;
	}
}