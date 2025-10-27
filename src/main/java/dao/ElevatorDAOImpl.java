package dao;

import dto.ElevatorLogDTO;

import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorDAOImpl implements ElevatorDAO{
    @Override
    public List<ElevatorLogDTO> selectELVLog() {
        List<ElevatorLogDTO> list = new ArrayList<>();
        String sql = """
            SELECT * FROM elevator_log
        """;
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ElevatorLogDTO(
                        rs.getInt("elevator_id"),
                        rs.getInt("user_id"),
                        rs.getInt("device_id"),
                        rs.getInt("from_floor"),
                        rs.getInt("to_floor"),
                        rs.getString("status"),
                        rs.getTimestamp("requested_at")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
