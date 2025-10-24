package dao;

import java.util.List;

import dto.ParkingLogDTO;
import dto.ParkingSpaceDTO;

public interface ParkingDAO {

	 // 특정 사용자의 가장 최근 주차 상태
    ParkingLogDTO getCurrentParkingStatus(int userId);

    // 특정 사용자의 전체 입출차 기록
    List<ParkingLogDTO> getParkingLogsByUser(int userId);

    // 전체 주차 공간 조회 (관리자용)
    List<ParkingSpaceDTO> getAllSpaces();
	

}
