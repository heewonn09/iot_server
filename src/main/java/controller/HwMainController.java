package controller;


import java.util.Scanner;
import dao.UserDAOImpl;
import dto.MemberDTO;

public class HwMainController {
	private final UserDAOImpl dao = new UserDAOImpl();
    public void userhandleAccess(MemberDTO currentUser) {
        Scanner sc = new Scanner(System.in);
       
        boolean running = true;
        while(running) {
            System.out.println("\n===== 사용자 관리 메뉴 =====");
            System.out.println("1. 사용자 정보 조회");
            System.out.println("2. 차량 등록");
            System.out.println("3. 상위 메뉴로 이동");
            System.out.print("메뉴 선택: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                	
                    System.out.print("조회할 사용자 ID 입력: ");
                    String inputId = sc.nextLine();

                    MemberDTO dto = dao.getUserInfo(inputId);

                    if (dto != null) {
                        System.out.println("조회 성공 ✅");
                        System.out.println("-----------------------------------");
                        System.out.println("회원번호: " + dto.getUserId());
                        System.out.println("아이디: " + dto.getId());
                        System.out.println("이름: " + dto.getName());
                        System.out.println("카드ID: " + dto.getCardId());
                        System.out.println("차량번호: " + dto.getVehicle_no());
                        System.out.println("권한레벨: " + dto.getAccess_level());
                        System.out.println("활성상태: " +
                                ((dto.getActive() != null && dto.getActive()) ? "활성" : "비활성"));
                        System.out.println("가입일: " + dto.getCreated_at());
                        System.out.println("-----------------------------------");
                    } else {
                    	System.out.println("❌ 해당 아이디의 사용자를 찾을 수 없습니다.");
                        
                    }
                    System.out.println("엔터를 누르면 다시 메뉴로 돌아갑니다.");
                    sc.nextLine();
                    break;
                    
                    

                case "2":
                    // 차량 등록
                    System.out.print("사용자 ID를 입력하세요: ");
                    String id = sc.nextLine();
                    System.out.print("등록할 차량 번호를 입력하세요 (예: 123가4567): ");
                    String vehicleNo = sc.nextLine();

                    boolean result = dao.updateVehicle(id, vehicleNo);

                    if (result) {
                        System.out.println("✅ 차량 등록이 완료되었습니다!");
                    } else {
                        System.out.println("⚠️ 차량 등록 실패 또는 이미 등록된 차량이 있습니다.");
                    }
                    System.out.println("엔터를 누르면 다시 메뉴로 돌아갑니다.");
                    sc.nextLine();
                    break;

                case "3":
                    return;

                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
                    break;
            }
        }
        sc.close();
    }
}
