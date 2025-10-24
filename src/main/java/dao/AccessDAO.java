package dao;

import dto.AccessLogDTO;

public interface AccessDAO {
    boolean checkPermission(int userId, int officeId, int accessLevel);
    boolean recordAccessEvent(AccessLogDTO log);
    void showLogsByUser(int userId);
    void showLogsByOffice(int officeId);
    void showAllLogs();
    void listDevicesByOffice(int officeId);
    void listAllDevices();
}