package test;

import java.util.List;
import dao.AdminParkingDAOImpl;
import dto.ParkingSpaceDTO;

public class AdminViewTest {
    public static void main(String[] args) {
        AdminParkingDAOImpl dao = new AdminParkingDAOImpl();

        System.out.println("===== 주차 공간 상세 현황 =====");
        List<ParkingSpaceDTO> list = dao.getAllSpace();
        for (ParkingSpaceDTO s : list) {
            System.out.println(s);
        }
    }
}
