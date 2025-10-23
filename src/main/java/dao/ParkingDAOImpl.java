package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import DBUtil.DBUtil;
import dto.ParkingSpaceDTO;

public class ParkingDAOImpl implements ParkingDAO {

    @Override
    public List<ParkingSpaceDTO> getAllSpaces() {
        String sql = """
                SELECT space_id, location, is_occupied, last_update
                FROM parking_space
                ORDER BY space_id;
                """;

        Connection con = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        List<ParkingSpaceDTO> list = new ArrayList<>();

        try {
            con = DBUtil.getConnect();
            ptmt = con.prepareStatement(sql);
            rs = ptmt.executeQuery();

            while (rs.next()) {
                ParkingSpaceDTO dto = new ParkingSpaceDTO();
                dto.setSpaceId(rs.getInt("space_id"));
                dto.setLocation(rs.getString("location"));
                dto.setOccupied(rs.getBoolean("is_occupied"));
                dto.setLastUpdate(rs.getTimestamp("last_update"));

                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ptmt, con);
        }

        return list;
    }

    @Override
    public int getTotalSpaces() {
        String sql = "SELECT COUNT(*) FROM parking_space";
        Connection con = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            con = DBUtil.getConnect();
            ptmt = con.prepareStatement(sql);
            rs = ptmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ptmt, con);
        }

        return count;
    }

    @Override
    public int getOccupiedCount() {
        String sql = "SELECT COUNT(*) FROM parking_space WHERE is_occupied = TRUE";
        Connection con = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            con = DBUtil.getConnect();
            ptmt = con.prepareStatement(sql);
            rs = ptmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ptmt, con);
        }

        return count;
    }

    @Override
    public List<dto.ParkingLogDTO> getParkingLogs() {
        return null; // 나중에 로그 조회 구현
    }

    @Override
    public dto.UserDTO searchVehicle(String vehicleNo) {
        return null; // 나중에 차량 번호 검색 구현
    }
}
