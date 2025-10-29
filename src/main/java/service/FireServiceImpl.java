package service;

import java.util.List;
import java.util.Map;

import dao.FireDAO;
import dao.FireDAOImpl;
import dto.EnvironmentDTO;
import dto.FireEventDTO;
import mqtt.MqttManager;

public class FireServiceImpl implements FireService {
    private final FireDAO dao = new FireDAOImpl();
    private MqttManager mqttManager;
    
    public FireServiceImpl(MqttManager mqttManager) {
        this.mqttManager = mqttManager;
    }

    @Override
    public List<EnvironmentDTO> getRecentData(int officeId) {
        return dao.getRecentEnvironmentData(officeId);
    }

    @Override
    public Map<Integer, List<EnvironmentDTO>> getAllOfficeData() {
        return dao.getAllOfficesEnvironmentData();
    }

    @Override
    public void recordFireEvent(FireEventDTO event) {
        dao.insertFireEvent(event);
    }

    @Override
    public List<FireEventDTO> getFireLogs() {
        return dao.getRecentFireLogs();
    }

    @Override
    public List<FireEventDTO> getFireLogsByOffice(int officeId) {
        return dao.getFireLogsByOffice(officeId);
    }
    
    @Override
    public EnvironmentDTO getLatestData() {
        // 임시로 officeId=1 기준 데이터 1개 가져오기
    	return dao.getLatestByOffice(1);
    }

    @Override
    public void logEvent(int userId, int deviceId, String type, String action, String note) {
        FireEventDTO event = new FireEventDTO();
        event.setUserId(userId);
        event.setDeviceId(deviceId);
        event.setEventType(type);
        event.setEventAction(action);
        event.setNote(note);
        event.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
        dao.insertFireEvent(event);
        
        //String msg = String.format("🔥 화재 경보 발생 (user=%d, device=%d)", userId, deviceId);
        //System.out.printf("✅ Fire Event Logged: device_id=%d → action=%s%n", event.getDeviceId(), action);

		String topic;
		String msg;
		
		// ✅ 경보 발생/해제에 따라 다른 MQTT 토픽 발행
		if ("MANUAL_TRIGGER".equals(action)) {
		  topic = "building/fire";
		  msg = "🔥 화재 경보 발생 (user=" + userId + ", device=" + deviceId + ")";
		} else if ("RESET".equals(action)) {
		  topic = "building/reset";
		  msg = "✅ 경보 해제 (user=" + userId + ")";
		} else {
		  return; // 기타 이벤트는 MQTT 전송 안 함
		}
		
		mqttManager.publish(topic, msg);
    }
}