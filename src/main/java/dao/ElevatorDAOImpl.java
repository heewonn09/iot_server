package dao;

import dto.ElevatorLogDTO;

import dto.MemberDTO;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElevatorDAOImpl implements ElevatorDAO{
    @Override
    public List<ElevatorLogDTO> showELVLog() {
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
    public Map<MemberDTO,Integer> showUserOffice(){
        String sql = """
                select u.*, o.floor_no from users u join offices o
                on u.office_id = o.office_id
                """;
        // 결과를 담을 Map 객체 생성
        Map<MemberDTO, Integer> userFloorMap = new HashMap<>();

        // try-with-resources 구문으로 자원 자동 해제
        try (Connection con = DBUtil.getConnect(); // DB 연결 메서드 호출
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // 결과(ResultSet)를 한 줄씩 순회
            while (rs.next()) {
                // 1. MemberDTO 객체 생성 및 데이터 채우기 (U.* 부분)
                MemberDTO member = new MemberDTO();
                member.setUserId(rs.getInt("user_id"));
                member.setId(rs.getString("id"));
                member.setName(rs.getString("name"));
                member.setCardId(rs.getString("card_id"));
                member.setPass(rs.getString("pass"));
                member.setAccess_level(rs.getInt("access_level"));
                member.setOfficeId(rs.getInt("office_id"));
                member.setVehicle_no(rs.getString("vehicle_no"));
                member.setActive(rs.getBoolean("is_active"));
                member.setCreated_at(rs.getTimestamp("created_at"));

                // 2. 층 정보 가져오기 (O.floor_no 부분)
                int floorNo = rs.getInt("floor_no");

                // 3. Map에 추가
                userFloorMap.put(member, floorNo);
            }

        } catch (SQLException e) {
            System.out.println("데이터베이스 조회 중 오류 발생");
            e.printStackTrace();
        }

        return userFloorMap;
    }
}
