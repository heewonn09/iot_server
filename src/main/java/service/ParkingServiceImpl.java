package service;

import java.util.List;
import dao.ParkingDAO;
import dao.ParkingDAOImpl;
import dto.ParkingLogDTO;
import dto.ParkingSpaceDTO;
import dto.UserDTO;

public class ParkingServiceImpl implements ParkingService {

    private ParkingDAO dao;

    public ParkingServiceImpl() {
        dao = new ParkingDAOImpl(); // DAO 객체 생성
    }

    @Override
    public List<ParkingSpaceDTO> getAllSpaces() {
        return dao.getAllSpaces();
    }

    @Override
    public int getTotalSpaces() {
        return dao.getTotalSpaces();
    }

    @Override
    public int getOccupiedCount() {
        return dao.getOccupiedCount();
    }

    @Override
    public List<ParkingLogDTO> getParkingLogs() {
        return dao.getParkingLogs();
    }

    @Override
    public UserDTO searchVehicle(String vehicleNo) {
        return dao.searchVehicle(vehicleNo);
    }
}
