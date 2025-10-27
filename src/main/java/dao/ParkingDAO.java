package dao;

import dto.ParkingLogDTO;
import dto.ParkingSpaceDTO;

import java.util.List;

public interface ParkingDAO {
    public ParkingLogDTO getCurrentParkingStatus(int userId);
    public List<ParkingLogDTO> getParkingLogsByUser(int userId);
    public List<ParkingSpaceDTO> getAllSpaces();
    /**
     * 차량 로그를 처리하고 DB 내 등록 여부를 확인합니다.
     *
     * @param carNo  차량 번호
     * @param action 차량 이동 액션 (IN/OUT)
     * @return 차량이 등록되어 있어 게이트를 열어도 되는 경우 {@code true},
     *         그렇지 않으면 {@code false}
     */
    boolean processVehicleLog(String carNo, String action);
}
