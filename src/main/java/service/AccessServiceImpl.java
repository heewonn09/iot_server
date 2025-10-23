package service;

import dao.AccessDAO;
import dao.AccessDAOImpl;
import dto.AccessLogDTO;
import dto.MemberDTO;

public class AccessServiceImpl implements AccessService {

    private AccessDAO dao = new AccessDAOImpl();

    @Override
    public boolean handleAccess(MemberDTO user, int deviceId) {
        boolean allowed = dao.checkPermission(user.getUserId(), deviceId);
        String action = allowed ? "GRANTED" : "DENIED";
        String note = allowed ? "출입 허가됨" : "출입 거부됨";
        dao.insertAccessLog(new AccessLogDTO(user.getUserId(), deviceId, user.getOfficeId(), action, note));
        return allowed;
    }

    @Override
    public void showLogsByUser(int userId) { dao.showLogsByUser(userId); }

    @Override
    public void showLogsByOffice(int officeId) { dao.showLogsByOffice(officeId); }

    @Override
    public void showAllLogs() { dao.showAllLogs(); }

    @Override
    public void listDevicesByOffice(int officeId) { dao.listDevicesByOffice(officeId); }

    @Override
    public void listAllDevices() { dao.listAllDevices(); }
}