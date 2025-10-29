package controller;

import java.util.List;
import java.util.Scanner;
import dao.AdminParkingDAOImpl;
import dao.UserDAOImpl;
import dto.MemberDTO;
import dto.ParkingDashboardDTO;
import dto.ParkingSpaceDTO;
import dto.ParkingSummaryDTO;
import mqtt.MqttManager;
import mqtt.MqttSubClientParking;
import util.TimeUtil;

public class ParkedController {
	
	private MqttManager mqttManager;
	public ParkedController(MqttManager mqttManager) {
		this.mqttManager = mqttManager;
    }
    private final AdminParkingDAOImpl dao = new AdminParkingDAOImpl();
 // ANSI 색상 코드 정의
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE_BOLD = "\u001B[1;37m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN_BOLD = "\u001B[1;36m";
    
    public void adminParked(MemberDTO currentUser) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
        	System.out.println("\n" + WHITE_BOLD + "═══════════════════════════════════════════════════════" + RESET);
            System.out.println(BLUE + "🅰️ [관리자 주차 관리 메뉴]" + RESET);
            System.out.println("──────────────────────────────────────────────");
            System.out.println("👤 관리자: " + currentUser.getName() + "   🕒 " + TimeUtil.now());
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.println("1️⃣ 주차 공간 상세 현황 보기");
            System.out.println("2️⃣ 시스템 대시보드 보기");
            System.out.println("3️⃣ 사용자 주차 이력 요약 보기");
            System.out.println("4️⃣ 주차장 센서 활성화");
            System.out.println("5️⃣️ 상위 메뉴로 이동");
            System.out.println("──────────────────────────────────────────────");
            System.out.print(YELLOW + "👉 메뉴 선택 >> " + RESET);
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> {
                	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
                	System.out.println("\n" + CYAN + "🚗 [주차 공간 상세 현황]" + RESET);
                    List<ParkingSpaceDTO> spaces = dao.getAllSpace(); // ✅ getAllSpace → getAllSpaces
                    if (spaces == null || spaces.isEmpty()) {
                        System.out.println(RED + "⚠️ 등록된 주차 공간이 없습니다." + RESET);
                    } else {
                        for (ParkingSpaceDTO s : spaces) System.out.println(GREEN + s + RESET);
                    }
                    System.out.println(WHITE_BOLD + "──────────────────────────────────────────────" + RESET);
                    System.out.println(YELLOW + "\n엔터를 누르면 관리자 메뉴로 돌아갑니다..." + RESET);
                    sc.nextLine();
                }
                case "2" -> {
                	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
                    System.out.println(BLUE + "📊 [시스템 대시보드]" + RESET);
                    System.out.println("──────────────────────────────────────────────");
                    ParkingDashboardDTO dashboard = dao.getSystem();
                    System.out.println((dashboard != null) ? dashboard :RED + "⚠️ 대시보드 정보를 불러올 수 없습니다." + RESET);
                    System.out.println(WHITE_BOLD + "──────────────────────────────────────────────" + RESET);
                    System.out.println(YELLOW + "\n엔터를 누르면 관리자 메뉴로 돌아갑니다..." + RESET);
                    sc.nextLine();
                }
                case "3" -> {
                	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
                    System.out.println(PURPLE + "🧾 [사용자 주차 이력 요약]" + RESET);
                    System.out.println("──────────────────────────────────────────────");
                    List<ParkingSummaryDTO> list = dao.getUserParkingSummary();
                    if (list == null || list.isEmpty()) {
                        System.out.println(RED + "⚠️ 주차 이력 데이터가 없습니다." + RESET);
                    } else {
                        System.out.printf(CYAN +"%-8s | %-12s | %-20s | %-20s | %-8s | %-8s\n",
                                "이름", "차량번호", "최근입차", "최근출차", "이용횟수", "총주차시간");
                        System.out.println("------------------------------------------------------------------------------------------");
                        for (ParkingSummaryDTO s : list) {
                            String name = (s.getName() != null) ? s.getName() : "-";
                            String vehicle = (s.getVehicleNo() != null && !s.getVehicleNo().isEmpty()) ? s.getVehicleNo() : "-";
                            String in = (s.getLastIn() != null) ? s.getLastIn() : "-";
                            String out = (s.getLastOut() != null) ? s.getLastOut() : "-";
                            System.out.printf("%-8s | %-12s | %-20s | %-20s | %-6d회 | %-6d분\n",
                                    name, vehicle, in, out, s.getTotalLogs(), s.getTotalMinutes());
                           
                        }
                        System.out.println(WHITE_BOLD + "──────────────────────────────────────────────" + RESET);
                        System.out.println(YELLOW + "\n엔터를 누르면 관리자 메뉴로 돌아갑니다..." + RESET);
                        sc.nextLine();
                        
                    }
                }
                case "4" -> {
                    try {
                    	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
                        System.out.println(PURPLE + "🛰 [주차 센서 통신 스레드 시작...]" + RESET);
                        System.out.println("──────────────────────────────────────────────");

                        // ✅ 수신용 SubClient 실행 (차량 감지 로그 확인용)
                        Thread subThread = new Thread(() -> {
                            MqttSubClientParking sub = new MqttSubClientParking();
                            sub.start();
                        });
                        subThread.setDaemon(true);
                        subThread.start();

                        // ✅ 파이썬으로 센서 활성화 명령 전송
                        String topic = "1/parking/01/cmd";
                        String msg = "{\"action\":\"activate\"}";
                        mqttManager.publish(topic, msg);

                        System.out.println("📤 MQTT Publish → " + topic + " : " + msg);
                        System.out.println(GREEN + "✅ 주차장 센서 활성화 명령 전송 완료!" + RESET);
                    } catch (Exception e) {
                        System.out.println(RED + "❌ MQTT 전송 실패: " + e.getMessage() + RESET);
                    }
                }
                case "5" -> {
                	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
                    System.out.println(RED + "🚪 관리자 메뉴를 종료하고 상위 메뉴로 이동합니다." + RESET);
                    System.out.println("═══════════════════════════════════════════════════════\n");
                    running = false;
                    return;
                }
                default -> System.out.println(RED + "⚠️ 잘못된 입력입니다. 다시 선택해주세요." + RESET);
            }
        }
        sc.close();
    }
    
    public void userhandleAccess(MemberDTO currentUser) {
    	final UserDAOImpl dao2 = new UserDAOImpl();
        Scanner sc = new Scanner(System.in);
       
        boolean running = true;
        while(running) {
        	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
            System.out.println(CYAN_BOLD + "👥 [사용자 관리 메뉴]" + RESET);
            System.out.println("──────────────────────────────────────────────");
            System.out.println("1️⃣ 사용자 정보 조회");
            System.out.println("2️⃣ 차량 등록");
            System.out.println("3️⃣ 상위 메뉴로 이동");
            System.out.println("──────────────────────────────────────────────");
            System.out.print(YELLOW + "👉 메뉴 선택 >> " + RESET);
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
                    System.out.println(CYAN_BOLD + "🔍 [사용자 정보 조회]" + RESET);
                    System.out.println("──────────────────────────────────────────────");
                    System.out.print(YELLOW + "조회할 사용자 ID 입력 >> " + RESET);
                    String inputId = sc.nextLine();

                    MemberDTO dto = dao2.getUserInfo(inputId);

                    if (dto != null) {
                    	System.out.println(GREEN + "\n✅ 조회 성공!" + RESET);
                        System.out.println(WHITE_BOLD + "──────────────────────────────────────────────" + RESET);
                        System.out.println("🆔 회원번호: " + dto.getUserId());
                        System.out.println("👤 아이디: " + dto.getId());
                        System.out.println("📛 이름: " + dto.getName());
                        System.out.println("💳 카드ID: " + dto.getCardId());
                        System.out.println("🚗 차량번호: " + dto.getVehicle_no());
                        System.out.println("🔒 권한레벨: " + dto.getAccess_level());
                        System.out.println("⚙️ 활성상태: " + 
                            ((dto.getActive() != null && dto.getActive()) ? GREEN + "활성" + RESET : RED + "비활성" + RESET));
                        System.out.println("📅 가입일: " + dto.getCreated_at());
                        System.out.println(WHITE_BOLD + "──────────────────────────────────────────────" + RESET);
                    } else {
                    	System.out.println(RED + "❌ 해당 아이디의 사용자를 찾을 수 없습니다." + RESET);
                        
                    }
                    System.out.println(YELLOW + "\n엔터를 누르면 다시 메뉴로 돌아갑니다..." + RESET);
                    sc.nextLine();
                    break;
                    
                    

                case "2":
                    // 차량 등록
                	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
                    System.out.println(CYAN_BOLD + "🚗 [차량 등록]" + RESET);
                    System.out.println("──────────────────────────────────────────────");
                    System.out.print(YELLOW + "사용자 ID 입력 >> " + RESET);
                    String id = sc.nextLine();
                    System.out.print(YELLOW + "등록할 차량 번호 입력 (예: 123가4567) >> " + RESET);
                    String vehicleNo = sc.nextLine();

                    boolean result = dao2.updateVehicle(id, vehicleNo);

                    if (result) {
                        System.out.println(GREEN + "\n✅ 차량 등록이 완료되었습니다!" + RESET);
                    } else {
                        System.out.println(RED + "⚠️ 차량 등록 실패 또는 이미 등록된 차량이 있습니다." + RESET);
                    }
                    System.out.println(YELLOW + "\n엔터를 누르면 다시 메뉴로 돌아갑니다..." + RESET);
                    sc.nextLine();
                    break;

                case "3":
                	System.out.println(WHITE_BOLD + "\n═══════════════════════════════════════════════════════" + RESET);
                    System.out.println(CYAN_BOLD + "⬆️ 상위 메뉴로 이동합니다." + RESET);
                    System.out.println("═══════════════════════════════════════════════════════\n");
                    running = false;
                    return;

                default:
                    System.out.println(RED + "⚠️ 잘못된 입력입니다. 다시 선택해주세요." + RESET);
                    break;
            }
        }
        sc.close();
    }
}
    