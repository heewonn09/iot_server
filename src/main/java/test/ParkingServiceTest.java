package test;

import service.ParkingService;
import service.ParkingServiceImpl;
import dto.ParkingSpaceDTO;
import java.util.List;

public class ParkingServiceTest {
    public static void main(String[] args) {
        ParkingService service = new ParkingServiceImpl();

        System.out.println("전체 주차공간 조회 테스트");
        List<ParkingSpaceDTO> list = service.getAllSpaces();
        for (ParkingSpaceDTO s : list) {
            System.out.println(s);
        }

        System.out.println("총 주차공간: " + service.getTotalSpaces());
        System.out.println("현재 점유된 공간: " + service.getOccupiedCount());
    }
}
