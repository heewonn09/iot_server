package test;

import java.util.List;
import dao.AdminParkingDAOImpl;
import dto.ParkingSummaryDTO;

public class AdminViewTest3 {
    public static void main(String[] args) {
        AdminParkingDAOImpl dao = new AdminParkingDAOImpl();

        System.out.println("===== 사용자 주차 이력 요약 =====");
        List<ParkingSummaryDTO> list = dao.getUserParkingSummary();

        // 테이블 헤더
        System.out.printf("%-8s | %-12s | %-20s | %-20s | %-8s | %-8s\n",
                "이름", "차량번호", "최근입차", "최근출차", "이용횟수", "총주차시간");
        System.out.println("------------------------------------------------------------------------------------------");

        // 각 사용자별 출력
        for (ParkingSummaryDTO s : list) {
            String name = s.getName() != null ? s.getName() : "-";
            String vehicle = (s.getVehicleNo() != null && !s.getVehicleNo().isEmpty()) ? s.getVehicleNo() : "-";
            String in = (s.getLastIn() != null) ? s.getLastIn() : "-";
            String out = (s.getLastOut() != null) ? s.getLastOut() : "-";

            System.out.printf("%-8s | %-12s | %-20s | %-20s | %-6d회 | %-6d분\n",
                    name, vehicle, in, out, s.getTotalLogs(), s.getTotalMinutes());
        }
    }
}
