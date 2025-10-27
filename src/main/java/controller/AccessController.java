package controller;

import java.util.Scanner;

import dto.MemberDTO;
import service.AccessService;
import service.AccessServiceImpl;

public class AccessController {

    private final AccessService service = new AccessServiceImpl();

    public void handleAccess(MemberDTO currentUser) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== 출입 통제 기능 ===");
        System.out.print("출입하려는 사무실 ID를 입력하세요: ");
        int officeId = sc.nextInt();

        service.tryAccessDoor(currentUser, officeId);
    }
}
