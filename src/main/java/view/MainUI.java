package view;

import java.util.List;
import java.util.Scanner;

import dto.LoginUserDTO;
import dto.OfficeDTO;

public class MainUI {
	private static final String RESET = "\u001B[0m";
    private static final String WHITE_BOLD = "\u001B[1;37m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
	
    public LoginUserDTO loginUI(){
        Scanner key = new Scanner(System.in);
        System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "🔐 [스마트 빌딩 통합 로그인 시스템]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("👋 환영합니다! 로그인 정보를 입력해주세요.");
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "🆔 ID 입력 >> " + RESET);
        String id = key.nextLine();
        System.out.print(YELLOW + "🔑 PW 입력 >> " + RESET);
        String pw = key.nextLine();
        System.out.println("──────────────────────────────────────────────");
        System.out.println(GREEN + "✅ 로그인 요청 중..." + RESET);
        System.out.println(WHITE_BOLD + "═══════════════════════════════════════════════════════" + RESET);

        return new LoginUserDTO(id, pw);
    }

    public String[] registerUI() {
        Scanner key = new Scanner(System.in);
        System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(PURPLE + "📝 [회원가입 페이지]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "🆔 ID 입력 >> " + RESET);
        String id = key.nextLine();
        System.out.print(YELLOW + "🔑 PW 입력 >> " + RESET);
        String pw = key.nextLine();
        System.out.print(YELLOW + "👤 이름 입력 >> " + RESET);
        String name = key.nextLine();
        System.out.println(GREEN + "✅ 회원가입 요청 중..." + RESET);
        System.out.println(WHITE_BOLD + "═══════════════════════════════════════════════════════" + RESET);
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
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "👑 [관리자 페이지]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("1️⃣ 출입문 제어 기능");
        System.out.println("2️⃣ 엘리베이터 제어 기능");
        System.out.println("3️⃣ 호실 별 디바이스 제어 기능");
        System.out.println("4️⃣ 주차장 제어 기능");
        System.out.println("5️⃣ 화재 감지 모드");
        System.out.println("6️⃣ 관리자 로그아웃");
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "👉 숫자 선택 (1 ~ 6) >> " + RESET);
        return key.nextInt();
        
        
    }
    public static int userUI(){
    	Scanner key = new Scanner(System.in);
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(GREEN + "🙋 [사용자 페이지]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("1️⃣ 출입문 제어 기능");
        System.out.println("2️⃣ 엘리베이터 제어 기능");
        System.out.println("3️⃣ 호실 별 디바이스 제어 기능");
        System.out.println("4️⃣ 주차장 제어 기능");
        System.out.println("5️⃣ 화재 감지 모드");
        System.out.println("6️⃣ 사용자 로그아웃");
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "👉 숫자 선택 (1 ~ 6) >> " + RESET);
        return key.nextInt();
    }
}
