package controller;

import dao.OfficeDAO;
import dto.LoginUserDTO;
import dto.MemberDTO;
import dto.OfficeDTO;
import service.UserService;
import view.MainUI;

import java.util.List;
import java.util.Scanner;

public class AuthController {

    private final UserService userService;
    private final MainUI view = new MainUI(); // UI는 여기서 직접 관리하거나 주입받을 수 있습니다.
    private final OfficeDAO officeDAO = new OfficeDAO(); // DAO도 주입받는 것이 더 좋습니다.

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    public MemberDTO loginMenu() {
        LoginUserDTO loginInfo = view.loginUI();

        MemberDTO currentUser = userService.login(loginInfo.getId(), loginInfo.getPw());

        if (currentUser == null) {
            System.out.println("❌ 로그인 실패. 아이디 혹은 비밀번호를 확인하세요.\n 엔터 시 화면이 전환됩니다.");
        } else {
            System.out.printf("✅ 로그인 성공 (%s님, 등급:%d)%n엔터 시 화면이 전환됩니다.", currentUser.getName(), currentUser.getAccess_level());
        }
        new Scanner(System.in).nextLine();
        return currentUser;
    }
    public void registerMenu() {
        String[] info = view.registerUI();
        Scanner sc = new Scanner(System.in);

        List<OfficeDTO> list = officeDAO.getAllOfficeInfo();
        view.showOfficeUI(list);
        System.out.print("이용하려는 Office ID를 입력하세요: ");
        int officeId = sc.nextInt();

        boolean result = userService.register(info[0], info[1], info[2],officeId);
        if (result) {
            System.out.println("✅ 회원가입 완료! 로그인 후 이용해주세요.");
        } else {
            System.out.println("❌ 회원가입 실패. 아이디 중복 또는 DB 오류입니다.");
        }
    }
}
