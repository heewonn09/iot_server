package dao;

import dto.AccessLogDTO;
import DBUtil.DBUtil;
import java.sql.*;

public class AccessDAOImpl implements AccessDAO {

    @Override
    public boolean checkPermission(int userId, int deviceId) {
        String sql = """
            SELECT COUNT(*) 
            FROM user_device_permissions 
            WHERE user_id = ? AND device_id = ?
        """;
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ptmt = con.prepareStatement(sql)) {
            ptmt.setInt(1, userId);
            ptmt.setInt(2, deviceId);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void insertAccessLog(AccessLogDTO dto) {
        String sql = """
            INSERT INTO event_log (user_id, device_id, office_id, event_action, note)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ptmt = con.prepareStatement(sql)) {
            ptmt.setInt(1, dto.getUserId());
            ptmt.setInt(2, dto.getDeviceId());
            ptmt.setInt(3, dto.getOfficeId());
            ptmt.setString(4, dto.getAction());
            ptmt.setString(5, dto.getNote());
            ptmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showLogsByUser(int userId) {
        String sql = "SELECT event_id, event_action, timestamp, note FROM event_log WHERE user_id = ?";
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ptmt = con.prepareStatement(sql)) {
            ptmt.setInt(1, userId);
            ResultSet rs = ptmt.executeQuery();
            System.out.println("\n[내 출입 로그]");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s%n",
                        rs.getInt("event_id"),
                        rs.getString("event_action"),
                        rs.getTimestamp("timestamp"),
                        rs.getString("note"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void showLogsByOffice(int officeId) {
        String sql = "SELECT event_id, user_id, event_action, timestamp FROM event_log WHERE office_id = ?";
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ptmt = con.prepareStatement(sql)) {
            ptmt.setInt(1, officeId);
            ResultSet rs = ptmt.executeQuery();
            System.out.println("\n[층 출입 로그]");
            while (rs.next()) {
                System.out.printf("%d | User:%d | %s | %s%n",
                        rs.getInt("event_id"),
                        rs.getInt("user_id"),
                        rs.getString("event_action"),
                        rs.getTimestamp("timestamp"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void showAllLogs() {
        String sql = "SELECT * FROM event_log ORDER BY timestamp DESC";
        try (Connection con = DBUtil.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n[전체 출입 로그]");
            while (rs.next()) {
                System.out.printf("%d | user:%d | office:%d | %s | %s%n",
                        rs.getInt("event_id"),
                        rs.getInt("user_id"),
                        rs.getInt("office_id"),
                        rs.getString("event_action"),
                        rs.getTimestamp("timestamp"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void listDevicesByOffice(int officeId) {
        String sql = "SELECT name, type, status FROM devices WHERE office_id = ?";
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ptmt = con.prepareStatement(sql)) {
            ptmt.setInt(1, officeId);
            ResultSet rs = ptmt.executeQuery();
            System.out.println("\n[층 장치 목록]");
            while (rs.next()) {
                System.out.printf("%s | %s | %s%n",
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("status"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void listAllDevices() {
        String sql = "SELECT name, type, status FROM devices";
        try (Connection con = DBUtil.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n[전체 장치 목록]");
            while (rs.next()) {
                System.out.printf("%s | %s | %s%n",
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("status"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}