package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.MemberDTO;
import dto.ParkingLogDTO;

public class UserServiceImpl implements UserService {
    private UserDAO dao = new UserDAOImpl(); // ✅ 생성자에서 미리 초기화

    @Override

    public MemberDTO login(String id, String pw) {
        dao = new UserDAOImpl();
        MemberDTO user = dao.login(id,pw);
        return user;
    }
    @Override
    public boolean register(String id, String pw, String name, int officeId) {
    	dao = new UserDAOImpl();
    	boolean register = dao.register(id, pw, name, officeId);
        return register;

    }

    @Override
    public MemberDTO getUserInfo(String userId) {
      
        return null;
    }

    @Override
    public boolean registerVehicle(String userId, String vehicleNo) {
       
        return dao.updateVehicle(userId, vehicleNo);
    }

    @Override
    public ParkingLogDTO getMyParkingStatus(String vehicleNo) {
        
        return null;
    }
}
