package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.OfficeDTO;
import util.DBUtil;

public class OfficeDAO {
    // Offices 테이블에 저장된 모든 Office 정보를 리턴하는 함수
    public List<OfficeDTO> getAllOfficeInfo() {
        String sql = "SELECT * FROM offices";
        List<OfficeDTO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OfficeDTO dto = new OfficeDTO(
                        rs.getInt("office_id"),
                        rs.getString("name"),
                        rs.getInt("floor_no"),
                        rs.getTimestamp("created_at")
                );
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getFloorByOfficeId(int officeId) {
        String sql = "SELECT floor_no FROM offices WHERE office_id = ?";
        try (Connection conn = DBUtil.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, officeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("floor_no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 찾지 못했을 때
    }
}