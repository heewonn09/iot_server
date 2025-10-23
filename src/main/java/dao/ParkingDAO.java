package dao;

import java.util.List;
import dto.ParkingSpaceDTO;
import dto.ParkingLogDTO;
import dto.UserDTO;

public interface ParkingDAO {

	    // 1. 전체 주차공간 목록
	    List<ParkingSpaceDTO> getAllSpaces();

	    // 2. 전체 자리수 / 사용중 자리수
	    int getTotalSpaces();
	    int getOccupiedCount();

	    // 3. 차량 입출 이력 조회
	    List<ParkingLogDTO> getParkingLogs();

	    // 4. 특정 차량 검색
	    UserDTO searchVehicle(String vehicleNo);
	    
	

}
