package service;

import java.util.Scanner;

import dao.AccessDAO;
import dao.AccessDAOImpl;
import dto.AccessLogDTO;
import dto.MemberDTO;
import mqtt.MqttManager;

public class AccessServiceImpl implements AccessService {

    private final AccessDAO dao = new AccessDAOImpl();
    private MqttManager mqttManager;

    public AccessServiceImpl(MqttManager mqttManager) {
        this.mqttManager = mqttManager;
    }

    @Override
	public void tryAccessDoor(MemberDTO user, int targetOfficeId) {
		boolean allowed = dao.checkPermission(user.getUserId(), targetOfficeId, user.getAccess_level());
        // deviceId officeId에 맞는 deviceId를 넣어줘야 함!
        String type = "RFID"; //devices 테이블에 어떤타입으로 출입문 디바이스가 들어가는지?
        int getOfficeDeviceId = dao.getOfficeAccess(targetOfficeId, type);

        if (allowed) {
            System.out.println("✅ 출입 승인됨: 문이 열립니다.");
            dao.recordAccessEvent(new AccessLogDTO(
                    getOfficeDeviceId, user.getUserId(), targetOfficeId, "ACCESS", "ALLOWED", "RFID 인증 성공", "정상 출입 허용"));
            // Mqtt publish 구현
            String topic = targetOfficeId+"/door/"+getOfficeDeviceId+"/cmd";

            String content = """ 
                    {'action': 'open'}
                    """;
            mqttManager.publish(topic,content);
            Scanner sc = new Scanner(System.in);
            sc.nextLine();
        } else {
            System.out.println("❌ 출입 거부: 권한이 없습니다.");
            dao.recordAccessEvent(new AccessLogDTO(
                    getOfficeDeviceId, user.getUserId(), targetOfficeId, "ACCESS", "DENIED", "RFID 인증 실패", "출입 권한 없음"));
            Scanner sc = new Scanner(System.in);
            sc.nextLine();
        }		
	}

//    @Override
//    public boolean handleAccess(MemberDTO user, int deviceId) {
//        boolean allowed = dao.hasAccessPermission(user.getUserId(), deviceId);
//        String action = allowed ? "GRANTED" : "DENIED";
//        String note = allowed ? "출입 허가됨" : "출입 거부됨";
//        dao.insertAccessLog(new AccessLogDTO(user.getUserId(), deviceId, user.getOfficeId(), action, note));
//        return allowed;
//    }
//
//    @Override
//    public void showLogsByUser(int userId) { dao.showLogsByUser(userId); }
//
//    @Override
//    public void showLogsByOffice(int officeId) { dao.showLogsByOffice(officeId); }
//
//    @Override
//    public void showAllLogs() { dao.showAllLogs(); }
//
//    @Override
//    public void listDevicesByOffice(int officeId) { dao.listDevicesByOffice(officeId); }
//
//    @Override
//    public void listAllDevices() { dao.listAllDevices(); }
}