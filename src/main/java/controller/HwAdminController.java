package controller;

import java.util.List;
import java.util.Scanner;
import dao.AdminParkingDAOImpl;
import dto.MemberDTO;
import dto.ParkingDashboardDTO;
import dto.ParkingSpaceDTO;
import dto.ParkingSummaryDTO;

public class HwAdminController {

    private final AdminParkingDAOImpl dao = new AdminParkingDAOImpl();

    public void adminParked(MemberDTO currentUser) {

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== 🅰️ 관리자 메뉴 =====");
            System.out.println("1. 주차 공간 상세 현황 보기");
            System.out.println("2. 시스템 대시보드 보기");
            System.out.println("3. 사용자 주차 이력 요약 보기");
            System.out.println("4. 상위 메뉴로 이동");
            System.out.print("메뉴 선택: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.println("\n===== 주차 공간 상세 현황 =====");
                    List<ParkingSpaceDTO> spaces = dao.getAllSpace(); // ✅ getAllSpace → getAllSpaces
                    if (spaces == null || spaces.isEmpty()) {
                        System.out.println("⚠️ 등록된 주차 공간이 없습니다.");
                    } else {
                        for (ParkingSpaceDTO s : spaces) System.out.println(s);
                    }
                    System.out.println("\n엔터를 누르면 관리자 메뉴로 돌아갑니다...");
                    sc.nextLine();
                }
                case "2" -> {
                    System.out.println("\n===== 시스템 대시보드 =====");
                    ParkingDashboardDTO dashboard = dao.getSystem();
                    System.out.println((dashboard != null) ? dashboard : "⚠️ 대시보드 정보를 불러올 수 없습니다.");
                    System.out.println("\n엔터를 누르면 관리자 메뉴로 돌아갑니다...");
                    sc.nextLine();
                }
                case "3" -> {
                    System.out.println("\n===== 사용자 주차 이력 요약 =====");
                    List<ParkingSummaryDTO> list = dao.getUserParkingSummary();
                    if (list == null || list.isEmpty()) {
                        System.out.println("⚠️ 주차 이력 데이터가 없습니다.");
                    } else {
                        System.out.printf("%-8s | %-12s | %-20s | %-20s | %-8s | %-8s\n",
                                "이름", "차량번호", "최근입차", "최근출차", "이용횟수", "총주차시간");
                        System.out.println("------------------------------------------------------------------------------------------");
                        for (ParkingSummaryDTO s : list) {
                            String name = (s.getName() != null) ? s.getName() : "-";
                            String vehicle = (s.getVehicleNo() != null && !s.getVehicleNo().isEmpty()) ? s.getVehicleNo() : "-";
                            String in = (s.getLastIn() != null) ? s.getLastIn() : "-";
                            String out = (s.getLastOut() != null) ? s.getLastOut() : "-";
                            System.out.printf("%-8s | %-12s | %-20s | %-20s | %-6d회 | %-6d분\n",
                                    name, vehicle, in, out, s.getTotalLogs(), s.getTotalMinutes());
                           
                        }
                        System.out.println("\n엔터를 누르면 관리자 메뉴로 돌아갑니다...");
      
                        sc.nextLine();
                        
                    }
                }
                case "4" -> {
                    System.out.println("관리자 메뉴를 종료하고 상위 메뉴로 이동합니다");
                    running = false;
                    return;
                }
                default -> System.out.println("⚠️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
        sc.close();
    }
}
