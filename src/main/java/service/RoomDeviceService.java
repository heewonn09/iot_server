	package service;

import dto.RoomDeviceDTO;
import java.util.List;

public interface RoomDeviceService {
	List<RoomDeviceDTO> getDeviceList(int officeId, String officeName);
	boolean controlDevice(int room_id, String device_name, String status);
}