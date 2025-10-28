package service;

import dto.MemberDTO;
import dto.ParkingLogDTO;

public interface UserService {
	   
	
	MemberDTO login(String id, String pw);
	
    // 내 정보 보기
    MemberDTO getUserInfo(String userId);

    // ✅ 차량 등록
    boolean registerVehicle(String userId, String vehicleNo);

    // ✅ 주차 상태 확인 (이미 선언되어 있음)
    ParkingLogDTO getMyParkingStatus(String vehicleNo);


    boolean register(String id, String pw, String name);
}

