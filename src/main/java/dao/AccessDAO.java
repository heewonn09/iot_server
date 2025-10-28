package dao;

import dto.AccessLogDTO;

public interface AccessDAO {
    public int getOfficeFloor(int OfficeId);
    boolean checkPermission(int userId, int officeId, int accessLevel);
    boolean recordAccessEvent(AccessLogDTO log);
    void showLogsByUser(int userId);
    void showLogsByOffice(int officeId);
    void showAllLogs();
    void listDevicesByOffice(int officeId);
    void listAllDevices();
    public int getOfficeAccess(int officeId, String type);
}