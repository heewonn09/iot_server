package dao;

import dto.DeviceDTO;
import java.util.List;

public interface RoomDeviceDAO {
	List<DeviceDTO> getDeviceListByOffice(int officeId, String officeName);
	int updateStatus(int room_id, String device_name, String status);
}