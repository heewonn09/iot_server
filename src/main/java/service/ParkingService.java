package service;

import java.util.List;
import dto.ParkingLogDTO;
import dto.ParkingSpaceDTO;
import dto.UserDTO;

public interface ParkingService {

    // 전체 주차공간 조회
    List<ParkingSpaceDTO> getAllSpaces();

    // 총 주차공간 수
    int getTotalSpaces();

    // 현재 주차중인 차량 수
    int getOccupiedCount();

    // 전체 주차 로그
    List<ParkingLogDTO> getParkingLogs();

    // 차량번호로 사용자 검색
    UserDTO searchVehicle(String vehicleNo);
}
