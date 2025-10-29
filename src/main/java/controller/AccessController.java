package controller;

import java.util.Scanner;

import dto.MemberDTO;
import service.AccessService;
import service.AccessServiceImpl;
import util.TimeUtil;

public class AccessController {

    private final AccessService service = new AccessServiceImpl();

    // ANSI 색상 코드 정의
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE_BOLD = "\u001B[1;37m";
    
    public void handleAccess(MemberDTO currentUser) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n" + WHITE_BOLD + "═══════════════════════════════════════════════════════" + RESET);
        System.out.println(BLUE + "🚪 [출입 통제 기능]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("👤 사용자: " + currentUser.getName() + "   🕒 " + TimeUtil.now());
        System.out.println("═══════════════════════════════════════════════════════");

        System.out.print(YELLOW + "👉 출입하려는 사무실 ID를 입력하세요: " + RESET);
        int officeId = sc.nextInt();
        sc.nextLine(); // flush buffer

        System.out.println("\n" + CYAN + "🔍 출입 요청 중..." + RESET);
        System.out.println("──────────────────────────────────────────────");

        service.tryAccessDoor(currentUser, officeId);
    }
}
