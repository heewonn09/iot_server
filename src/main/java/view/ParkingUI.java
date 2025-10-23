package view;

import dto.ParkingSpaceDTO;
import service.ParkingService;
import service.ParkingServiceImpl;
import java.util.List;

public class ParkingUI {

    private static final ParkingService service = new ParkingServiceImpl();

    public static void showAllSpaces() {
        System.out.println("\n🚗 전체 주차공간 조회");
        List<ParkingSpaceDTO> list = service.getAllSpaces();
        if (list.isEmpty()) {
            System.out.println("등록된 주차공간이 없습니다.");
            return;
        }
        for (ParkingSpaceDTO s : list) {
            String status = s.getIsOccupied() ? "🟥 사용중" : "🟩 비어있음";
            System.out.printf("[%s] %s (%s)\n", s.getLocation(), status, s.getLastUpdate());
        }
    }

    public static void showOccupiedCount() {
        int total = service.getTotalSpaces();
        int occupied = service.getOccupiedCount();
        System.out.printf("총 %d개 중 %d개가 사용 중입니다.\n", total, occupied);
    }

    public static void showOccupancyRate() {
        int total = service.getTotalSpaces();
        int occupied = service.getOccupiedCount();
        if (total == 0) {
            System.out.println("등록된 주차공간이 없습니다.");
            return;
        }
        double rate = (double) occupied / total * 100;
        System.out.printf("현재 점유율: %.1f%% (%d/%d)\n", rate, occupied, total);
    }
}
