package test;

import java.util.Scanner;
import dao.UserDAOImpl;
import dto.MemberDTO;

public class UserViewTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserDAOImpl dao = new UserDAOImpl(); 

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
        sc.close();
    }
}
