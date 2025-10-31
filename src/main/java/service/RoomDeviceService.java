	package service;

import dto.DeviceDTO;
import java.util.List;

public interface RoomDeviceService {
	List<DeviceDTO> getDeviceList(int officeId, String officeName);
	boolean controlDevice(int room_id, String device_name, String status);
}