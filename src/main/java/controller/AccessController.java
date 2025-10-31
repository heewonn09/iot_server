package controller;

import java.util.List;
import java.util.Scanner;

import dao.OfficeDAO;
import dto.MemberDTO;
import dto.OfficeDTO;
import mqtt.MqttManager;
import service.AccessService;
import service.AccessServiceImpl;
import util.ConsoleUtils;
import util.TimeUtil;
import view.MainUI;
import static util.ColorUtil.*;

public class AccessController {

    private final AccessService service;
    private MqttManager mqttManager;

    public AccessController(MqttManager mqttManager) {
        this.mqttManager = mqttManager;
        service = new AccessServiceImpl(this.mqttManager);
    }
    
    public void handleAccess(MemberDTO currentUser) {
    	ConsoleUtils.clearConsole();
        Scanner sc = new Scanner(System.in);
        System.out.println("\n" + WHITE_BOLD + "═══════════════════════════════════════════════════════" + RESET);
        System.out.println(BLUE + "🚪 [출입 통제 기능]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("👤 사용자: " + currentUser.getName() + "   🕒 " + TimeUtil.now());
        System.out.println("═══════════════════════════════════════════════════════");

        System.out.println("🏢 [사무실 목록] 🏢");
        OfficeDAO dao = new OfficeDAO();
        List<OfficeDTO> list = dao.getAllOfficeInfo(); //모든 Office 목록을 불러오기
        MainUI view = new MainUI();
        view.showOfficeUI(list); //Office 목록들을 출력시켜 접근하려는 Office Id 선택하게 하기

        System.out.print(YELLOW + "👉 출입하려는 사무실 ID를 입력하세요: " + RESET);
        int officeId = sc.nextInt();
        sc.nextLine(); // flush buffer

        System.out.println("\n" + CYAN + "🔍 출입 요청 중..." + RESET);
        System.out.println("──────────────────────────────────────────────");
        sc.nextLine();

        service.tryAccessDoor(currentUser, officeId); //선택 시 해당
    }
}
