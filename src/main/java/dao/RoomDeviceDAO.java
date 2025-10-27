package dao;

import dto.RoomDeviceDTO;
import java.util.List;

public interface RoomDeviceDAO {
	List<RoomDeviceDTO> selectByRoom(String room_name);
	int updateStatus(int room_id, String device_name, String status);
}