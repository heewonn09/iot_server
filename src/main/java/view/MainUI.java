package view;

import java.util.List;
import java.util.Scanner;

import dto.LoginUserDTO;
import dto.OfficeDTO;

public class MainUI {
    public LoginUserDTO loginUI(){
        Scanner key = new Scanner(System.in);
        System.out.println("회원 로그인을 진행합니다.");
        System.out.print("id: ");
        String id = key.nextLine();
        System.out.print("pw: ");
        String pw = key.nextLine();

        return new LoginUserDTO(id, pw);
    }


    
    public String[] registerUI() {
        Scanner key = new Scanner(System.in);
        System.out.println("회원가입을 진행합니다.");
        System.out.print("id: ");
        String id = key.nextLine();
        System.out.print("pw: ");
        String pw = key.nextLine();
        System.out.print("이름: ");
        String name = key.nextLine();
        return new String[]{id, pw, name};

    }

    public void showOfficeUI(List<OfficeDTO> list){
        Scanner key = new Scanner(System.in);
        int num =1 ;
        for(OfficeDTO office : list){
            System.out.println((num++)+". "+ office.getName()+", "+office.getFloorNo()+"층, Id: "+office.getOfficeId());
        }
    }

    public static int adminUI(){
    	Scanner key = new Scanner(System.in);
        System.out.println("관리자페이지입니다.");
        System.out.println("1. 출입문 제어 기능");
        System.out.println("2. 엘리베이터 제어 기능");
        System.out.println("3. 호실 별 디바이스 제어 기능");
        System.out.println("4. 주차장 제어 기능");
        System.out.println("5. 화재 감지 모드");
        System.out.println("6. 관리자 로그아웃");
        System.out.print(">>> 숫자 선택 (1 ~ 6) : ");
        return key.nextInt();
        
        
    }
    public static int userUI(){
    	Scanner key = new Scanner(System.in);
        System.out.println("사용자페이지입니다.");
        System.out.println("1. 출입문 제어 기능");
        System.out.println("2. 엘리베이터 제어 기능");
        System.out.println("3. 호실 별 디바이스 제어 기능");
        System.out.println("4. 주차장 제어 기능");
        System.out.println("5. 화재 감지 모드");
        System.out.println("6. 사용자 로그아웃");
        System.out.print(">>> 숫자 선택 (1 ~ 6) : ");
        return key.nextInt();
    }
}
