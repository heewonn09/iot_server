package test;

import java.util.Scanner;
import dao.UserDAOImpl;

public class UserViewTest2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserDAOImpl dao = new UserDAOImpl();

        System.out.println("===== 차량 등록 테스트 =====");
        System.out.print("사용자 ID를 입력하세요: ");
        String id = sc.nextLine();

        System.out.print("등록할 차량 번호를 입력하세요 (예: 123가4567): ");
        String vehicleNo = sc.nextLine();

        boolean result = dao.updateVehicle(id, vehicleNo);

        if (result) {
            System.out.println("✅ 차량 등록이 완료되었습니다!");
        } else {
            System.out.println("⚠️ 차량 등록 실패 또는 이미 등록된 차량이 있습니다.");
        }

        sc.close();
    }
}
