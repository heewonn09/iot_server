package dao;

import java.util.List;

import dto.ParkingDashboardDTO;
import dto.ParkingSpaceDTO;
import dto.ParkingSummaryDTO;

public interface AdminParkingDao {
	ParkingDashboardDTO getSystem();
	List<ParkingSpaceDTO> getAllSpace();
	List<ParkingSummaryDTO> getUserParkingSummary();
}
