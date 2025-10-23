package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.MemberDTO;
import dto.ParkingLogDTO;

public class UserServiceImpl implements UserService{
    private UserDAO dao;
    @Override
    public int login(String id, String pw) {
        dao = new UserDAOImpl();
        MemberDTO user = dao.login(id,pw);
        if(user==null){
            System.out.println("로그인 정보가 없습니다.");
            return 0;
        }
        return user.getAccess_level();
    }
	@Override
	public ParkingLogDTO getMyParkingStatus(String vehicleNo) {
		
		return null;
	}
    
    
}
