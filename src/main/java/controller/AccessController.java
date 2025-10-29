package controller;

import java.util.List;
import java.util.Scanner;

import dao.OfficeDAO;
import dto.MemberDTO;
import dto.OfficeDTO;
import mqtt.MqttManager;
import service.AccessService;
import service.AccessServiceImpl;
import view.MainUI;

public class AccessController {

    private final AccessService service;
    private MqttManager mqttManager;

    public AccessController(MqttManager mqttManager) {
        this.mqttManager = mqttManager;
        service = new AccessServiceImpl(this.mqttManager);
    }

    public void handleAccess(MemberDTO currentUser) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== 출입 통제 기능 ===");
        OfficeDAO dao = new OfficeDAO();
        List<OfficeDTO> list = dao.getAllOfficeInfo(); //모든 Office 목록을 불러오기
        MainUI view = new MainUI();
        view.showOfficeUI(list); //Office 목록들을 출력시켜 접근하려는 Office Id 선택하게 하기

        System.out.print("출입하려는 사무실 ID를 입력하세요: ");
        int officeId = sc.nextInt();

        service.tryAccessDoor(currentUser, officeId); //선택 시 해당
    }
}
