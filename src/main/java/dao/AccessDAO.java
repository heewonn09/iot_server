package dao;

import dto.AccessLogDTO;

public interface AccessDAO {
    boolean checkPermission(int userId, int deviceId);
    void insertAccessLog(AccessLogDTO dto);
    void showLogsByUser(int userId);
    void showLogsByOffice(int officeId);
    void showAllLogs();
    void listDevicesByOffice(int officeId);
    void listAllDevices();
}