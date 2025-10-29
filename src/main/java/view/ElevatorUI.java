package view;

import dto.MemberDTO;

import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ElevatorUI {
	private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE_BOLD = "\u001B[1;37m";
    private static final String PURPLE = "\u001B[35m";
	
    Scanner key = new Scanner(System.in);
    public int adminUI(){
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "👑 [관리자용 엘리베이터 제어 페이지]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("1️⃣ 엘리베이터 권한 관리 메뉴");
        System.out.println("2️⃣ 엘리베이터 조작 메뉴");
        System.out.println("3️⃣ 페이지 나가기");
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "👉 선택 >> " + RESET);
        return Integer.parseInt(key.nextLine());
    }
    public int userUI(){
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(GREEN + "🙋 [회원용 엘리베이터 UI]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("1️⃣ 엘리베이터 호출");
        System.out.println("2️⃣ 페이지 나가기");
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "👉 선택 >> " + RESET);
        return Integer.parseInt(key.nextLine());
    }
    public int authUI(){
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(BLUE + "🔒 [엘리베이터 권한 관리]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("권한 관리 메뉴에서는 층별 접근 가능한 유저 목록을 볼 수 있습니다.");
        System.out.println("권한을 직접 수정하려면 '유저 관리 메뉴'를 이용하세요.");
        System.out.println("──────────────────────────────────────────────");
        System.out.println("1️⃣ 유저 목록 조회");
        System.out.println("2️⃣ 페이지 나가기");
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "👉 선택 >> " + RESET);
        return Integer.parseInt(key.nextLine());
    }
    public int stateUI(){
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(PURPLE + "⚙️ [엘리베이터 조작]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("1️⃣ 엘리베이터 상태 조회");
        System.out.println("2️⃣ 엘리베이터 이용 정지");
        System.out.println("3️⃣ 엘리베이터 위치 제어");
        System.out.println("4️⃣ 엘리베이터 통계 리포트");
        System.out.println("5️⃣ 페이지 나가기");
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "👉 선택 >> " + RESET);
        return Integer.parseInt(key.nextLine());
    }

    public String stateSELECT(int id, int floor, boolean isEnable){ // 상태 조회
    	String state = isEnable ? (GREEN + "이용 가능" + RESET) : (RED + "이용 불가" + RESET);
        System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "🧾 [엘리베이터 상태 조회]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.printf("🔢 E/V ID: %d%n", id);
        System.out.printf("🏢 현 위치: %d층%n", floor);
        System.out.printf("🚪 이용 여부: %s%n", state);
        System.out.println("──────────────────────────────────────────────");
        System.out.print(YELLOW + "👉 아무 키나 입력하면 나갑니다 >> " + RESET);
        return key.nextLine();
    }
    public String stateARRIVE(int floor){

        System.out.println(GREEN + "✅ " + floor + "층에 도착했습니다!" + RESET);
        return key.nextLine();
    }
    public String stateSTOP(){ // 이용 정지
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(RED + "⛔ [엘리베이터 이용 상태 변경]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("엘리베이터의 이용 가능 여부를 변경합니다.");
        //상태 기능 조회
        System.out.print(YELLOW + "👉 이용 가능 여부 선택 (Y/N): " + RESET);
        String input = key.nextLine();
        System.out.println(input);
        return input.toLowerCase();
    }
    public int floorControl(){ //위치 제어
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(PURPLE + "🕹️ [엘리베이터 위치 제어]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("엘리베이터의 현재 상태를 조회합니다...");
        System.out.print(YELLOW + "👉 이동할 층 입력 (1~3): " + RESET);
        return Integer.parseInt(key.nextLine());
    }
    public void showEVUser(Map<MemberDTO,Integer> map){
    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "📋 [엘리베이터 접근 권한 조회]" + RESET);
        System.out.println("층 별 엘리베이터 접근 권한을 보여줍니다....");
        System.out.println("──────────────────────────────────────────────");
        
        // map의 entrySet을 스트림으로 변환한 후, 값(floor)을 기준으로 정렬하고 출력합니다.
        map.entrySet()
                .stream() // 1. Map의 모든 항목(Entry)을 스트림으로 변환
                .sorted(Map.Entry.comparingByValue()) // 2. 값을 기준(Integer)으로 오름차순 정렬
                .forEach(entry -> { // 3. 정렬된 각 항목을 순회하며 출력
                    MemberDTO user = entry.getKey();
                    Integer floor = entry.getValue();
                    String authName = "일반 사용자";
                    if(user.getAccess_level()==3){
                        authName = "전체 관리자";
                    } else if (user.getAccess_level()==2) {
                        authName = "층 관리자";
                    }


                    // DTO에 getName(), getId() 메서드가 있다고 가정
                    System.out.printf("권한: %s | 층: %-3d | 이름: %-10s | 아이디: %s\n", authName, floor, user.getName(), user.getId());
                });
        System.out.println(WHITE_BOLD + "──────────────────────────────────────────────" + RESET);
    }

}
