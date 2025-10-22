package view;

import dto.LoginUserDTO;
import dto.MemberDTO;
import service.UserService;
import service.UserServiceImpl;

import java.util.Scanner;

public class MainUI {
    public static void startApp(){
        System.out.println("Console Menu의 첫 시작점");
        // 로그인으로 입력된 권한에 따라 관리자용 페이지, 사용자용 페이지 중 구분되어 실행된다.
        System.out.println("=====스마트 빌딩 서비스=====");
        LoginUserDTO loginInfo = loginMenu();
        UserService serv = new UserServiceImpl();
        int role = serv.login(loginInfo.getId(), loginInfo.getPw());
        switch (role){
            case 9:
                adminMenu(); //관리자 페이지 이동
                break;
            case 1:
                userMenu(); //사용자 페이지 이동
                break;
            default:
                System.out.println("error");
                break;
        }
    }
    public static LoginUserDTO loginMenu(){
        Scanner key = new Scanner(System.in);
        System.out.println("회원 로그인을 진행합니다.");
        System.out.print("id: ");
        String id = key.nextLine();
        System.out.print("pw: ");
        String pw = key.nextLine();
        // login 메서드 호출 -> MySQL에 회원 정보 확인 후 사용자 정보 반환
        LoginUserDTO user = new LoginUserDTO(id, pw);
        return user;
    }

    public static void adminMenu(){
        System.out.println("관리자페이지입니다.");
    }
    public static void userMenu(){
        System.out.println("사용자페이지입니다.");
    }
}
