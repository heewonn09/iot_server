package menutest;

import java.util.List;
import java.util.Scanner;
import dao.AdminParkingDAOImpl;
import dto.ParkingSpaceDTO;
import dto.ParkingDashboardDTO;
import dto.ParkingSummaryDTO;

public class AdminMenuUItest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AdminParkingDAOImpl dao = new AdminParkingDAOImpl();
        boolean running = true;

        while (running) {
            System.out.println("\n===== 🅰️ 관리자 메뉴 =====");
            System.out.println("1. 주차 공간 상세 현황 보기");
            System.out.println("2. 시스템 대시보드 보기");
            System.out.println("3. 사용자 주차 이력 요약 보기");
            System.out.println("4. 종료");
            System.out.print("메뉴 선택: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n===== 주차 공간 상세 현황 =====");
                    List<ParkingSpaceDTO> spaces = dao.getAllSpace();
                    if (spaces == null || spaces.isEmpty()) {
                        System.out.println("⚠️ 등록된 주차 공간이 없습니다.");
                    } else {
                        for (ParkingSpaceDTO s : spaces) {
                            System.out.println(s);
                        }
                    }
                    break;

                case "2":
                    System.out.println("\n===== 시스템 대시보드 =====");
                    ParkingDashboardDTO dashboard = dao.getSystem();
                    if (dashboard != null) {
                        System.out.println(dashboard);
                    } else {
                        System.out.println("⚠️ 대시보드 정보를 불러올 수 없습니다.");
                    }
                    break;

                case "3":
                    System.out.println("\n===== 사용자 주차 이력 요약 =====");
                    List<ParkingSummaryDTO> list = dao.getUserParkingSummary();

                    if (list == null || list.isEmpty()) {
                        System.out.println("⚠️ 주차 이력 데이터가 없습니다.");
                    } else {
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
                    break;

                case "4":
                    System.out.println("관리자 메뉴를 종료합니다.");
                    running = false;
                    break;

                default:
                    System.out.println("⚠️ 잘못된 입력입니다. 다시 선택해주세요.");
                    break;
            }
        }

        sc.close();
    }
}
