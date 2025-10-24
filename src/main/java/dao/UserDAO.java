package dao;

import dto.MemberDTO;

public interface UserDAO {
    MemberDTO login(String id, String pw);

	MemberDTO getUserInfo(String userId);

	boolean updateVehicle(String userId, String vehicleNo);

	
	
    
}
