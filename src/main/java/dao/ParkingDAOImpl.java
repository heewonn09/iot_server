package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import dto.ParkingLogDTO;
import dto.ParkingSpaceDTO;
import util.DBUtil;

public class ParkingDAOImpl implements ParkingDAO {

    // ✅ 최근 주차 상태 조회
    @Override
    public ParkingLogDTO getCurrentParkingStatus(int userId) {
        String sql = """
                SELECT p.*, s.location
                FROM parking_log p
                JOIN parking_space s ON p.space_id = s.space_id
                WHERE p.user_id = ?
                ORDER BY p.in_time DESC
                LIMIT 1
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

            if (rs.next()) {
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

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ptmt, con);
        }
        return dto;
    }

    @Override
    public List<ParkingLogDTO> getParkingLogsByUser(int userId) {
        return null; // 나중에 필요 시 구현
    }

    @Override
    public List<ParkingSpaceDTO> getAllSpaces() {
        return null; // 나중에 필요 시 구현
    }

    // ✅ 차량 입출차 로그 처리
    public boolean  processVehicleLog(String carNo, String action) {
        if (carNo == null || carNo.trim().isEmpty()) {
            System.out.println("🚗 차량 번호가 비어 있어 로그를 처리할 수 없습니다.");
            return false;
        }

        String normalizedAction = (action == null || action.trim().isEmpty()) ? "IN" : action.trim().toUpperCase();

        try (Connection con = DBUtil.getConnect()) {

            Integer userId = null;
            String findUserSql = "SELECT user_id FROM users WHERE TRIM(vehicle_no) = ?";
            try (PreparedStatement findUser = con.prepareStatement(findUserSql)) {
                findUser.setString(1, carNo.trim());
                try (ResultSet userRs = findUser.executeQuery()) {
                    if (userRs.next()) {
                        userId = userRs.getInt("user_id");
                    }
                }
            }

            if (userId == null) {
                System.out.printf("🚫 차량 번호 %s 에 해당하는 등록 정보가 없습니다.%n", carNo);
                return false;
            }
            boolean success = false;
            switch (normalizedAction) {
                case "IN" :
                	success = logVehicleEntry(con, userId, carNo);
                case "OUT" :
                	success = logVehicleExit(con, userId, carNo);
                default :
                	System.out.printf("⚠️ 지원하지 않는 차량 액션: %s%n", normalizedAction);
                	return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 입차 로그 기록
    private boolean logVehicleEntry(Connection con, int userId, String carNo) throws SQLException {
        String insertSql = """
                INSERT INTO parking_log (user_id, space_id, action, note, in_time)
                VALUES (?, ?, 'IN', ?, NOW())
                """;
        try (PreparedStatement insert = con.prepareStatement(insertSql)) {
            insert.setInt(1, userId);
            insert.setNull(2, Types.INTEGER);
            insert.setString(3, buildNote("입차", carNo));
            return insert.executeUpdate() > 0;
            
        }
    }

    // ✅ 출차 로그 기록
    private boolean  logVehicleExit(Connection con, int userId, String carNo) throws SQLException {
        String findOpenSql = """
                SELECT parking_id, in_time
                FROM parking_log
                WHERE user_id = ? AND out_time IS NULL
                ORDER BY in_time DESC
                LIMIT 1
                """;
        try (PreparedStatement findOpen = con.prepareStatement(findOpenSql)) {
            findOpen.setInt(1, userId);
            try (ResultSet openRs = findOpen.executeQuery()) {
                if (openRs.next()) {
                    int parkingId = openRs.getInt("parking_id");
                    Timestamp inTime = openRs.getTimestamp("in_time");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    long diff = now.getTime() - (inTime != null ? inTime.getTime() : now.getTime());
                    int duration = diff > 0 ? (int) (diff / 60000) : 0;

                    String updateSql = """
                            UPDATE parking_log
                            SET out_time = ?, duration_min = ?, action = 'OUT', note = ?
                            WHERE parking_id = ?
                            """;
                    try (PreparedStatement update = con.prepareStatement(updateSql)) {
                        update.setTimestamp(1, now);
                        update.setInt(2, duration);
                        update.setString(3, buildNote("출차", carNo));
                        update.setInt(4, parkingId);
                        return update.executeUpdate() > 0;
                       
                    }
                } else {
                    String insertOutSql = """
                            INSERT INTO parking_log (user_id, space_id, action, note, out_time)
                            VALUES (?, ?, 'OUT', ?, NOW())
                            """;
                    try (PreparedStatement insert = con.prepareStatement(insertOutSql)) {
                        insert.setInt(1, userId);
                        insert.setNull(2, Types.INTEGER);
                        insert.setString(3, buildNote("출차", carNo));
                        return insert.executeUpdate() > 0;
                        
                    }
                }
            }
        }
    }


    private String buildNote(String type, String carNo) {
        return String.format("[%s] 차량번호: %s", type, carNo);
    }
}
