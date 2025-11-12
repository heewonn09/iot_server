package dao;

import dto.DeviceDTO;
import java.util.List;

public interface RoomDeviceDAO {
	List<DeviceDTO> getDeviceListByOffice(int officeId, String officeName);
    int updateStatus(int id,int id2, String sta);
}