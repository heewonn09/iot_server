package controller;

import java.util.List;
import java.util.Scanner;

import dao.OfficeDAO;
import dto.MemberDTO;
import dto.OfficeDTO;
import mqtt.MqttManager;
import service.AccessService;
import service.AccessServiceImpl;
import util.TimeUtil;
import view.MainUI;

public class AccessController {

    private final AccessService service;
    private MqttManager mqttManager;

    public AccessController(MqttManager mqttManager) {
        this.mqttManager = mqttManager;
        service = new AccessServiceImpl(this.mqttManager);
    }

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
        System.out.println("=== 출입 통제 기능 ===");
        OfficeDAO dao = new OfficeDAO();
        List<OfficeDTO> list = dao.getAllOfficeInfo(); //모든 Office 목록을 불러오기
        MainUI view = new MainUI();
        view.showOfficeUI(list); //Office 목록들을 출력시켜 접근하려는 Office Id 선택하게 하기

        System.out.print("출입하려는 사무실 ID를 입력하세요: ");
        int officeId = sc.nextInt();
        sc.nextLine(); // flush buffer

        System.out.println("\n" + CYAN + "🔍 출입 요청 중..." + RESET);
        System.out.println("──────────────────────────────────────────────");

        service.tryAccessDoor(currentUser, officeId); //선택 시 해당
    }
}
