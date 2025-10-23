package service;

import dto.ParkingLogDTO;

public interface UserService {
    int login(String id, String pw);


	ParkingLogDTO getMyParkingStatus(String vehicleNo);
	
}
