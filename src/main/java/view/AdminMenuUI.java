package view;

import service.ParkingService;
import service.ParkingServiceImpl;
import java.util.Scanner;

public class AdminMenuUI {
    private final ParkingService service = new ParkingServiceImpl();
    private final Scanner sc = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("========== [주차 시스템 - 관리자용] ==========");
            System.out.println("1. 전체 차량 목록 조회");
            System.out.println("2. 현재 주차 현황 보기");
            System.out.println("3. 차량 입출 이력 조회");
            System.out.println("4. 특정 차량 검색");
            System.out.println("5. 이전 메뉴로");
            System.out.print("선택 >> ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> service.getAllSpaces();
                case 2 -> service.getTotalSpaces();
                case 3 -> service.getOccupiedCount();
               
                case 5 -> { return; } // 메인으로 돌아감
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }
}
