package service;

import dto.RoomDeviceDTO;
import java.util.List;

public interface RoomDeviceService {
	List<RoomDeviceDTO> getDeviceList(String room_name);
	boolean controlDevice(int room_id, String device_name, String status);
}