package dao;

import dto.ParkingLogDTO;
import dto.ParkingSpaceDTO;

import java.util.List;

public interface ParkingDAO {
    public ParkingLogDTO getCurrentParkingStatus(int userId);
    public List<ParkingLogDTO> getParkingLogsByUser(int userId);
    public List<ParkingSpaceDTO> getAllSpaces();
}
