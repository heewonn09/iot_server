package view;

import java.util.Scanner;

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
        System.out.println("1. 유저 목록 조회");
        System.out.println("2. 유저 권한 추가");
        System.out.println("3. 기존 유저 권한 수정");
        System.out.println("4. 유저 권한 삭제");
        System.out.println("5. 페이지 나가기");
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

    public void stateSELECT(int id, int floor, boolean isEnable){ // 상태 조회
        String state = isEnable ? "이용 가능": "이용 불가";
        System.out.println("====== 엘리베이터 상태 조회 ======");
        System.out.println("E/V ID: "+ id);
        System.out.println("현 위치: "+ floor + "F");
        System.out.println("이용 여부: "+ state);
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
    public int floorControl(){ //위치 제어
        System.out.println("====== 엘리베이터 위치 제어 ======");
        System.out.println("엘리베이터 상태를 조회합니다...");
        // 상태 조회 기능 추가
        System.out.print("이동할 층 입력(1-3)>>>>>");
        return Integer.parseInt(key.nextLine());
    }
    public void showEVLog(){
        System.out.println("====== 엘리베이터 통계 조회======");
        System.out.println("기간 내 엘리베이터 이용 현황을 보여줍니다....");
    }

}
