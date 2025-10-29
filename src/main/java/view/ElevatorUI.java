package view;

import dto.MemberDTO;

import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ElevatorUI {
    Scanner key = new Scanner(System.in);
    public int adminUI(){
        System.out.println("======관리자용 UI======");
        System.out.println("======엘리베이터 제어 기능 페이지======");
        System.out.println("1. 엘리베이터 권한 관리 메뉴");
        System.out.println("2. 엘리베이터 조작 메뉴");
        System.out.println("3. 페이지 나가기");
        System.out.print(">>>> 선택: ");
        return Integer.parseInt(key.nextLine());
    }
    public int userUI(){
        System.out.println("======회원용 UI======");
        System.out.println("1. 엘리베이터 호출");
        System.out.println("2. 페이지 나가기");
        System.out.print(">>>> 선택: ");
        return Integer.parseInt(key.nextLine());
    }
    public int authUI(){
        System.out.println("======엘리베이터 권한 관리======");
        System.out.println("권한 관리 메뉴에선 층 별로 접근 가능한 유저 목록을 보여줍니다.");
        System.out.println("유저 권한 자체를 변경, 조작하는 경우 유저 관리 메뉴를 이용해주세요.");
        System.out.println("1. 유저 목록 조회");
        System.out.println("2. 페이지 나가기");
        System.out.print(">>>> 선택: ");
        return Integer.parseInt(key.nextLine());
    }
    public int stateUI(){
        System.out.println("======엘리베이터 조작======");
        System.out.println("1. 엘리베이터 상태 조회");
        System.out.println("2. 엘리베이터 이용 정지");
        System.out.println("3. 엘리베이터 위치 제어");
        System.out.println("4. 엘리베이터 통계 리포트");
        System.out.println("5. 페이지 나가기");
        System.out.print(">>>> 선택: ");
        return Integer.parseInt(key.nextLine());
    }

    public String stateSELECT(int id, int floor, boolean isEnable){ // 상태 조회
        String state = isEnable ? "이용 가능": "이용 불가";
        System.out.println("====== 엘리베이터 상태 조회 ======");
        System.out.println("E/V ID: "+ id);
        System.out.println("현 위치: "+ floor + "F");
        System.out.println("이용 여부: "+ state);
        System.out.print("아무입력이나 실행하면 나가집니다.");
        return key.nextLine();
    }
    public String stateARRIVE(int floor){

        System.out.println(floor+" 층에 도착했습니다.");
        return key.nextLine();
    }
    public String stateSTOP(){ // 이용 정지
        System.out.println("======엘리베이터 상태 변경======");
        System.out.println("엘리베이터의 이용 가능 여부를 변경하는 기능입니다.");
        // 상태 조회 기능 추가
        System.out.print(">>>>>> 이용 가능 선택 (Y/N) :");
        String input = key.nextLine();
        System.out.println(input);
        return input.toLowerCase();
    }
    public void showEVUser(Map<MemberDTO,Integer> map){
        System.out.println("====== 엘리베이터 권한 조회======");
        System.out.println("층 별 엘리베이터 접근 권한을 보여줍니다....");

        // map의 entrySet을 스트림으로 변환한 후, 값(floor)을 기준으로 정렬하고 출력합니다.
        map.entrySet()
                .stream() // 1. Map의 모든 항목(Entry)을 스트림으로 변환
                .sorted(Map.Entry.comparingByValue()) // 2. 값을 기준(Integer)으로 오름차순 정렬
                .forEach(entry -> { // 3. 정렬된 각 항목을 순회하며 출력
                    MemberDTO user = entry.getKey();
                    Integer floor = entry.getValue();
                    String authName = "사용자";
                    if(user.getAccess_level()==3){
                        authName = "전체 관리자";
                    } else if (user.getAccess_level()==2) {
                        authName = "층 관리자";
                    }


                    // DTO에 getName(), getId() 메서드가 있다고 가정
                    System.out.printf("권한: %s | 층: %-3d | 이름: %-10s | 아이디: %s\n", authName, floor, user.getName(), user.getId());
                });
    }

}
