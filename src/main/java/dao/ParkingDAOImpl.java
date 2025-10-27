package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import util.DBUtil;
import dto.ParkingLogDTO;
import dto.ParkingSpaceDTO;

public class ParkingDAOImpl implements ParkingDAO {

	@Override
	public ParkingLogDTO getCurrentParkingStatus(int userId) {
		String sql = """
				select p.*, s.location
				from parking_log p
				join parking_space s on p.space_id = s.space_id
				where p.user_id = ?
				order by p.in_time desc
				limit 1
				""";
		Connection con = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		ParkingLogDTO dto = null;
		try {
			
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
            ptmt.setInt(1, userId);
            rs = ptmt.executeQuery();
            if(rs.next()) {
			dto = new ParkingLogDTO();
			dto.setParkingId(rs.getInt("parking_id"));
            dto.setUserId(rs.getInt("user_id"));
            dto.setSpaceId(rs.getInt("space_id"));
            dto.setInTime(rs.getTimestamp("in_time"));
            dto.setOutTime(rs.getTimestamp("out_time"));
            dto.setAction(rs.getString("action"));
            dto.setNote(rs.getString("note"));
            dto.setLocation(rs.getString("location"));
            
		
		}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, ptmt, con);
		}
		return dto;
	}

	@Override
	public List<ParkingLogDTO> getParkingLogsByUser(int userId) {
		
		return null;
	}

	@Override
	public List<ParkingSpaceDTO> getAllSpaces() {
		
		return null;
	}


}