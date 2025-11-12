package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.EnvironmentDTO;
import dto.FireEventDTO;
import util.DBUtil;

public class FireDAOImpl implements FireDAO {

    @Override
    public List<EnvironmentDTO> getRecentEnvironmentData(int officeId) {
        List<EnvironmentDTO> list = new ArrayList<>();
        String sql = """
            SELECT e.* FROM environment_data e
            JOIN devices d ON e.device_id = d.device_id
            WHERE d.office_id = ?
            ORDER BY e.measured_at
        """;
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, officeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new EnvironmentDTO(
                        rs.getInt("record_id"),
                        rs.getInt("device_id"),
                        rs.getFloat("temperature"),
                        rs.getFloat("humidity"),
                        rs.getFloat("gas_level"),
                        rs.getTimestamp("measured_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public EnvironmentDTO getLatestByOffice(int officeId) {
        String sql = """
            SELECT
                -- 가장 최근 온도 (DHT)
                (SELECT e1.temperature
                 FROM environment_data e1
                 JOIN devices d1 ON e1.device_id = d1.device_id
                 WHERE d1.office_id = ? AND e1.temperature IS NOT NULL
                 ORDER BY e1.measured_at DESC
                 LIMIT 1) AS temperature,

                -- 가장 최근 습도 (DHT)
                (SELECT e2.humidity
                 FROM environment_data e2
                 JOIN devices d2 ON e2.device_id = d2.device_id
                 WHERE d2.office_id = ? AND e2.humidity IS NOT NULL
                 ORDER BY e2.measured_at DESC
                 LIMIT 1) AS humidity,

                -- 가장 최근 가스 농도 (MQ2)
                (SELECT e3.gas_level
                 FROM environment_data e3
                 JOIN devices d3 ON e3.device_id = d3.device_id
                 WHERE d3.office_id = ? AND e3.gas_level IS NOT NULL
                 ORDER BY e3.measured_at DESC
                 LIMIT 1) AS gas_level
            FROM dual
        """;

        try (Connection con = DBUtil.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, officeId);
            ps.setInt(2, officeId);
            ps.setInt(3, officeId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Float temp = rs.getFloat("temperature");
                Float hum = rs.getFloat("humidity");
                Float gas = rs.getFloat("gas_level");
                return new EnvironmentDTO(0, 0, temp, hum, gas, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<Integer, List<EnvironmentDTO>> getAllOfficesEnvironmentData() {
        Map<Integer, List<EnvironmentDTO>> map = new HashMap<>();
        String sql = """
            SELECT d.office_id, e.* FROM environment_data e
            JOIN devices d ON e.device_id = d.device_id
            ORDER BY e.measured_at DESC
        """;
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int officeId = rs.getInt("office_id");
                EnvironmentDTO dto = new EnvironmentDTO(
                        rs.getInt("record_id"),
                        rs.getInt("device_id"),
                        rs.getFloat("temperature"),
                        rs.getFloat("humidity"),
                        rs.getFloat("gas_level"),
                        rs.getTimestamp("measured_at")
                );
                map.computeIfAbsent(officeId, k -> new ArrayList<>()).add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public void insertFireEvent(FireEventDTO event) {
        String sql = """
            INSERT INTO event_log (device_id, user_id, office_id, event_type, event_action, value, note, timestamp)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBUtil.getConnect()) {

            // ① device_id로부터 office_id 조회
            int officeId = 0;
            String officeQuery = "SELECT office_id FROM devices WHERE device_id = ?";
            try (PreparedStatement ps1 = con.prepareStatement(officeQuery)) {
                ps1.setInt(1, event.getDeviceId());
                try (ResultSet rs = ps1.executeQuery()) {
                    if (rs.next()) {
                        officeId = rs.getInt("office_id");
                    }
                }
            }

            // ② 조회한 office_id를 event에 세팅
            event.setOfficeId(officeId);

            // ③ 실제 insert 실행
            try (PreparedStatement ps2 = con.prepareStatement(sql)) {
                ps2.setInt(1, event.getDeviceId());
                ps2.setInt(2, event.getUserId());
                ps2.setInt(3, officeId);  // ✅ 이제 null 아님
                ps2.setString(4, event.getEventType());
                ps2.setString(5, event.getEventAction());
                ps2.setString(6, event.getValue());
                ps2.setString(7, event.getNote());
                ps2.setTimestamp(8, event.getTimestamp());
                ps2.executeUpdate();
            }

            System.out.printf("✅ Fire Event Logged: device_id=%d → office_id=%d%n",
                    event.getDeviceId(), officeId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FireEventDTO> getRecentFireLogs() {
        List<FireEventDTO> logs = new ArrayList<>();
        String sql = "SELECT * FROM event_log WHERE event_type='FIRE' ORDER BY timestamp DESC LIMIT 10";
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                logs.add(extractEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @Override
    public List<FireEventDTO> getFireLogsByOffice(int officeId) {
        List<FireEventDTO> logs = new ArrayList<>();
        String sql = "SELECT * FROM event_log WHERE event_type='FIRE' AND office_id=? ORDER BY timestamp DESC LIMIT 10";
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, officeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                logs.add(extractEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    private FireEventDTO extractEvent(ResultSet rs) throws SQLException {
        return new FireEventDTO(
                rs.getInt("device_id"),
                rs.getInt("user_id"),
                rs.getInt("office_id"),
                rs.getString("event_type"),
                rs.getString("event_action"),
                rs.getString("value"),
                rs.getString("note"),
                rs.getTimestamp("timestamp")
        );
    }
}