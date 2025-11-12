package service;

import dto.MemberDTO;

public interface AccessService {
	void tryAccessDoor(MemberDTO user, int targetOfficeId);
	
//    boolean handleAccess(MemberDTO userId, int deviceId);
//    void showLogsByUser(int userId);
//    void showLogsByOffice(int officeId);
//    void showAllLogs();
//    void listDevicesByOffice(int officeId);
//    void listAllDevices();
}