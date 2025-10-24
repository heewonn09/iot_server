package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.MemberDTO;
import dto.ParkingLogDTO;

public class UserServiceImpl implements UserService {
    private UserDAO dao = new UserDAOImpl(); // ✅ 생성자에서 미리 초기화

    @Override

    public int login(String id, String pw) {
        MemberDTO user = dao.login(id, pw);
        if (user == null) {
            System.out.println("로그인 정보가 없습니다.");
            return 0;
        }
        return user.getAccess_level();
    }
    @Override
    public boolean register(String id, String pw, String name) {
    	dao = new UserDAOImpl();
    	boolean register = dao.register(id, pw, name);
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
