package controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import mqtt.MqttManager;
import util.TimeUtil;
import dao.OfficeDAO;
import dto.EnvironmentDTO;
import dto.FireEventDTO;
import dto.MemberDTO;
import service.FireService;
import service.FireServiceImpl;

public class FireController {
    private MqttManager mqttManager;
    private FireService service;

    public FireController(MqttManager mqttManager) {
        this.mqttManager = mqttManager;
        this.service = new FireServiceImpl(this.mqttManager);
    }

    private final Scanner sc = new Scanner(System.in);

    private double tempThreshold = 60.0;
    private String smokeSensitivity = "HIGH";
    private boolean alarmOn = false;
    
 // ANSI 색상 코드 정의
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE_BOLD = "\u001B[1;37m";
    
    // 🔹 로그인된 사용자 권한에 따라 모드 분기
    public void handleFireMode(MemberDTO user) {
        int level = user.getAccess_level();
        if (level == 3) {
            handleBuildingAdminMode(user);
        } else if (level == 2) {
            handleFloorAdminMode(user);
        } else {
            handleUserFireMode(user);
        }
    }

    // 🔸 건물 전체 관리자
    private void handleBuildingAdminMode(MemberDTO user) {
    	while (true) {
    		System.out.println("\n" + WHITE_BOLD + "═══════════════════════════════════════════════════════" + RESET);
            System.out.println(BLUE + "🏢 [화재 감지 모드 - 건물 관리자]" + RESET);
            System.out.println("──────────────────────────────────────────────");
            System.out.println("👤 관리자: " + user.getName() + "   🕒 " + TimeUtil.now());
            System.out.println("═══════════════════════════════════════════════════════");

            System.out.println(YELLOW + "\n🔥 [시스템 상태]" + RESET);
            System.out.println("──────────────────────────────────────────────");
            EnvironmentDTO latest = service.getLatestData();
            if (latest != null) {
                System.out.printf("🌡 온도: %.1f ℃%n💧 습도: %.1f %%\n", latest.getTemperature(), latest.getHumidity());
                System.out.printf("💨 연기 감지: %s%n", latest.getGasLevel() > 300 ? (RED + "⚠️ 감지됨" + RESET) : (GREEN + "정상" + RESET));
                System.out.printf("🚨 경보 상태: %s\n", alarmOn ? (RED + "🔴 ON" + RESET) : (GREEN + "🟢 OFF" + RESET));
            } else {
                System.out.println(RED + "❌ 센서 데이터가 없습니다." + RESET);
            }
            System.out.println("──────────────────────────────────────────────");
            System.out.println(CYAN + "\n[기능 선택]" + RESET);
            System.out.println("1️⃣ 실시간 모니터링");
            System.out.println("2️⃣ 화재 임계값 설정");
            System.out.println("3️⃣ 🚨 수동 경보 발생");
            System.out.println("4️⃣ 🟢 경보 해제 / 시스템 리셋");
            System.out.println("5️⃣ 📜 로그 확인");
            System.out.println("6️⃣ 🚪 모드 종료");
            System.out.println("──────────────────────────────────────────────");
            System.out.print(YELLOW + "👉 선택 >> " + RESET);
            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();
            
            switch (choice) {
                case 1 -> startMonitoring(user);
                case 2 -> configureThreshold();
                case 3 -> manualAlarm(user);
                case 4 -> resetSystem(user);
                case 5 -> printLogs();
                case 6 -> { 
                    System.out.println(GREEN + "✅ 화재 감지 모드를 종료합니다. 관리자 메뉴로 돌아갑니다." + RESET);
                    return;
                }
                default -> System.out.println(RED + "❌ 잘못된 입력입니다." + RESET);
            }
        }
    }

    // ✅ 1️⃣ 실시간 모니터링
    private void startMonitoring(MemberDTO user) {
    	System.out.println("\n" + CYAN + "🔍 [모니터링 시작]" + RESET);
    	System.out.println("──────────────────────────────────────────────");
        System.out.println("3초 간격으로 센서 데이터를 확인합니다...\n");

        for (int i = 0; i < 5; i++) {
            EnvironmentDTO data = service.getLatestData();
            String smoke = data.getGasLevel() > 300 ? (RED + "⚠️ 감지됨" + RESET) : (GREEN + "정상" + RESET);
            System.out.printf("[%s] 🌡 온도: %.1f°C | 💨 연기: %s%n", TimeUtil.now(), data.getTemperature(), smoke);

            if (data.getTemperature() > tempThreshold || data.getGasLevel() > 300) {
            	System.out.println(RED + "\n🔥 임계치를 초과했습니다! 경보를 작동합니다..." + RESET);
            	triggerAlarm(user, data);
                break;
            }
            sleep(3000);
        }
        sc.nextLine();

        if (!alarmOn) System.out.println(GREEN + "\n✅ 모니터링이 종료되었습니다. 이상 없음." + RESET);
        sc.nextLine();
    }

    // ✅ 2️⃣ 임계값 설정
    private void configureThreshold() {
        System.out.println("\n" + YELLOW + "⚙️ [임계값 설정]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.printf("현재 설정값:\n - 🌡 온도 임계값 : %.1f°C%n", tempThreshold);
        System.out.printf(" - 💨 연기 감지 민감도 : %s%n", smokeSensitivity);
        System.out.println("──────────────────────────────────────────────");

        System.out.print("새로운 온도 임계값 입력 >> ");
        tempThreshold = sc.nextDouble(); sc.nextLine();

        System.out.print("새로운 연기 민감도 설정 (LOW / MEDIUM / HIGH) >> ");
        smokeSensitivity = sc.nextLine().toUpperCase();

        System.out.println(GREEN + "✅ 설정이 저장되었습니다." + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.printf("현재 설정:\n - 🌡 온도 임계값 : %.1f°C\n - 💨 연기 감지 민감도 : %s%n",
                tempThreshold, smokeSensitivity);
        System.out.println("──────────────────────────────────────────────");
        sc.nextLine();
    }

    // ✅ 3️⃣ 수동 경보 발생
    private void manualAlarm(MemberDTO user) {
        alarmOn = true;
        System.out.println("\n" + RED + "🚨 [수동 경보 발생]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.println(RED + "수동으로 경보를 발생시켰습니다!" + RESET);
        System.out.println("🔊 부저: " + RED + "ON" + RESET + "   💡 경보등: " + RED + "ON" + RESET);
        System.out.println("📢 [안내] 모든 사용자에게 화재 알림을 전송 중...");
        System.out.println("──────────────────────────────────────────────");

        service.logEvent(user.getUserId(), 1, "FIRE", "MANUAL_TRIGGER", "관리자 수동 경보 발생");

        System.out.println(GREEN + "✅ 로그 기록 완료 및 MQTT 전송 요청 완료" + RESET);
        System.out.println("──────────────────────────────────────────────");
        sc.nextLine();
    }

    // ✅ 4️⃣ 시스템 리셋
    private void resetSystem(MemberDTO user) {
        System.out.println("\n" + BLUE + "🟢 [경보 해제 / 시스템 리셋]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        if (!alarmOn) {
            System.out.println(YELLOW + "⚠️ 현재 경보가 이미 꺼져 있습니다." + RESET);
            System.out.println("──────────────────────────────────────────────");
            return;
        }

        System.out.println(YELLOW + "⚠️ 경보 해제 명령을 전송합니다..." + RESET);
        alarmOn = false;

        System.out.println("🔊 부저: " + GREEN + "OFF" + RESET + "   💡 경보등: " + GREEN + "OFF" + RESET);
        System.out.println(GREEN + "✅ 시스템이 정상 모드로 복귀했습니다." + RESET);
        System.out.println("──────────────────────────────────────────────");

        // MQTT로 RESET 발행
        service.logEvent(user.getUserId(), 1, "FIRE", "RESET", "경보 해제 및 시스템 복귀");

        System.out.println(CYAN + "📡 MQTT 메시지 발행 완료: building/reset" + RESET);
        System.out.println("──────────────────────────────────────────────");
        sc.nextLine();
    }

    // ✅ 5️⃣ 로그 보기
    private void printLogs() {
        System.out.println("\n" + CYAN + "📜 [화재 감지 로그]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        List<FireEventDTO> logs = service.getFireLogs();
        if (logs.isEmpty()) {
            System.out.println(YELLOW + "⚠️ 기록된 로그가 없습니다." + RESET);
        } else {
            System.out.printf("%-20s | %-6s | %-15s | %s%n",
                    "🕒 시간", "USER", "동작", "비고");
            System.out.println("──────────────────────────────────────────────");
            for (FireEventDTO e : logs) {
                System.out.printf("%-20s | %-6d | %-15s | %s%n",
                        e.getTimestamp(), e.getUserId(), e.getEventAction(), e.getNote());
            }
            System.out.println("──────────────────────────────────────────────");
            System.out.printf(GREEN + "✅ 총 %d개의 로그가 저장되어 있습니다.%n" + RESET, logs.size());
        }
        System.out.println("──────────────────────────────────────────────");
        sc.nextLine();
    }

    // 🔥 알람 발생
    private void triggerAlarm(MemberDTO user, EnvironmentDTO data) {
        alarmOn = true;
        System.out.println("\n" + RED + "🔥 [화재 감지!]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.printf("🌡 온도: %.1f°C   💨 연기: %s%n", data.getTemperature(),
                data.getGasLevel() > 300 ? (RED + "감지됨" + RESET) : "정상");
        System.out.println("🔊 부저: " + RED + "ON" + RESET + "   💡 경보등: " + RED + "ON" + RESET);
        System.out.println("📢 센서 감지로 자동 경보가 작동되었습니다.");
        System.out.println("──────────────────────────────────────────────");
        service.logEvent(user.getUserId(), 1, "FIRE", "AUTO_TRIGGER", "센서 감지로 인한 자동 경보 발생");
        while (alarmOn) {
            System.out.println(CYAN + "\n[조치 옵션]" + RESET);
            System.out.println("1️⃣ 경보 해제");
            System.out.println("2️⃣ 로그 보기");
            System.out.println("3️⃣ 계속 모니터링");
            System.out.print(YELLOW + "👉 선택 >> " + RESET);

            int opt = sc.nextInt(); sc.nextLine();
            System.out.println();
            switch (opt) {
                case 1 -> resetSystem(user);
                case 2 -> printLogs();
                case 3 -> System.out.println("🔄 계속 모니터링 중...");
                default -> System.out.println(RED + "❌ 잘못된 입력입니다." + RESET);
            }
            if (!alarmOn) {
                System.out.println(GREEN + "✅ 경보가 해제되어 모니터링을 종료합니다." + RESET);
                break;
            }
        }
        sc.nextLine();
    }

    // 유틸
    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }


 // 🔸 층 관리자 모드
    private void handleFloorAdminMode(MemberDTO user) {
    	OfficeDAO officeDAO = new OfficeDAO();
        int officeId = user.getOfficeId();
        int floorNo = officeDAO.getFloorByOfficeId(officeId);

        while (true) {
        	 System.out.printf("\n" + WHITE_BOLD + "═══════════════════════════════════════════════════════%n" + RESET);
             System.out.printf(BLUE + "🏢 [화재 감지 모드 - 층 관리자 | %d층]%n" + RESET, floorNo);
             System.out.println("──────────────────────────────────────────────");
             System.out.println("👤 관리자: " + user.getName() + "   🕒 " + TimeUtil.now());
             System.out.println("═══════════════════════════════════════════════════════");

             EnvironmentDTO latest = getLatestByOffice(officeId);
             if (latest != null) {
                 System.out.printf("🌡 온도: %.1f ℃\n", latest.getTemperature());
                 System.out.printf("💧 습도: %.1f %%\n", latest.getHumidity());
                 System.out.printf("💨 연기 감지: %s\n",
                         latest.getGasLevel() > 300 ? (RED + "⚠️ 감지됨" + RESET) : (GREEN + "정상" + RESET));
                 System.out.printf("🚨 경보 상태: %s\n",
                         alarmOn ? (RED + "🔴 ON" + RESET) : (GREEN + "🟢 OFF" + RESET));
             } else {
                 System.out.println(YELLOW + "❌️ 센서 데이터가 없습니다." + RESET);
             }

             System.out.println("──────────────────────────────────────────────");
             System.out.println(CYAN + "\n[기능 선택]" + RESET);
             System.out.println("1️⃣ 내 층 실시간 모니터링");
             System.out.println("2️⃣ 내 층 화재 로그 보기");
             System.out.println("3️⃣ 🚪 모드 종료");
             System.out.println("──────────────────────────────────────────────");
             System.out.print(YELLOW + "👉 선택 >> " + RESET);

             int choice = sc.nextInt(); sc.nextLine();
             System.out.println();

             switch (choice) {
                 case 1 -> startFloorMonitoring(user, officeId);
                 case 2 -> printLogsByOffice(officeId);
                 case 3 -> {
                     System.out.println(GREEN + "✅ 층 관리자 모드를 종료합니다." + RESET);
                     return;
                 }
                 default -> System.out.println(RED + "❌ 잘못된 입력입니다." + RESET);
             }
         }
     }

    // 🔸 일반 사용자 모드
    private void handleUserFireMode(MemberDTO user) {
        int officeId = user.getOfficeId();
        System.out.println("\n" + WHITE_BOLD + "═══════════════════════════════════════════════════════" + RESET);
        System.out.printf(BLUE + "👤 [화재 감지 모드 - 일반 사용자 | 사무실 ID: %d]%n" + RESET, officeId);
        System.out.println("──────────────────────────────────────────────");
        System.out.println("🕒 현재시간: " + TimeUtil.now());
        System.out.println("═══════════════════════════════════════════════════════");
        EnvironmentDTO latest = getLatestByOffice(officeId);
        if (latest != null) {
            System.out.printf("🌡 온도: %.1f ℃%n", latest.getTemperature());
            System.out.printf("💧 습도: %.1f %%\n", latest.getHumidity());
            System.out.printf("💨 가스 농도: %.1f ppm%n", latest.getGasLevel());
            boolean danger = latest.getTemperature() > 55 || latest.getGasLevel() > 300;
            if (danger) {
                System.out.println(RED + "🚨 상태: 화재 위험 감지! 즉시 관리자에게 보고하십시오." + RESET);
            } else {
                System.out.println(GREEN + "✅ 상태: 정상" + RESET);
            }
        } else {
            System.out.println(YELLOW + "⚠️ 센서 데이터가 없습니다." + RESET);
        }
        System.out.println("──────────────────────────────────────────────");
        System.out.println(YELLOW + "※ 일반 사용자는 데이터 조회만 가능합니다." + RESET);
        System.out.println("──────────────────────────────────────────────");
        sc.nextLine();
    }
    
 // 🔹 특정 층의 최신 데이터 1건만 반환
    private EnvironmentDTO getLatestByOffice(int officeId) {
        List<EnvironmentDTO> list = service.getRecentData(officeId);
        if (list == null || list.isEmpty()) return null;
        return list.get(0);
    }

    // 🔹 층 관리자 전용 실시간 모니터링
    private void startFloorMonitoring(MemberDTO user, int officeId) {
    	System.out.println("\n" + CYAN + "📡 [층 실시간 모니터링 시작]" + RESET);
        System.out.println("──────────────────────────────────────────────");
        System.out.printf("3초 간격으로 센서 데이터를 확인 중... (Office ID: %d)%n", officeId);
        System.out.println("──────────────────────────────────────────────");
        for (int i = 0; i < 5; i++) {
            EnvironmentDTO data = getLatestByOffice(officeId);
            if (data == null) {
                System.out.println("⚠️ 센서 데이터가 없습니다.");
                break;
            }

            String smoke = data.getGasLevel() > 300 ? (RED + "⚠️ 감지됨" + RESET) : (GREEN + "정상" + RESET);
            System.out.printf("[%s] 🌡 %.1f°C | 💨 연기: %s%n", TimeUtil.now(), data.getTemperature(), smoke);
            

            if (data.getTemperature() > tempThreshold || data.getGasLevel() > 300) {
            	System.out.println(RED + "\n🔥 [경고] 임계치를 초과했습니다. 자동 경보를 작동합니다..." + RESET);
            	fireAlert(user, data);
            	sc.nextLine();
                break;
            }
            sleep(3000);
        }

        if (!alarmOn) System.out.println(GREEN + "\n✅ 모니터링 종료: 이상 없음." + RESET);
        System.out.println("──────────────────────────────────────────────");
        sc.nextLine();
    }

    // 🔹 특정 층(office_id) 데이터 출력
    private void printEnvironmentData(int officeId) {
        List<EnvironmentDTO> list = service.getRecentData(officeId);
        if (list.isEmpty()) {
            System.out.println("⚠️ 환경 데이터가 없습니다.");
            return;
        }
        EnvironmentDTO latest = list.get(0);
        System.out.printf("현재 온도: %.1f℃, 습도: %.1f%%, 가스농도: %.2f%n",
                latest.getTemperature(), latest.getHumidity(), latest.getGasLevel());
        sc.nextLine();
    }

    // 🔹 전체 층 센서 데이터 보기 (건물 관리자 전용)
    private void printAllFloorsData() {
        service.getAllOfficeData().forEach((officeId, dataList) -> {
            if (!dataList.isEmpty()) {
                EnvironmentDTO latest = dataList.get(0);
                System.out.printf("[Office %d] 온도: %.1f°C | 가스: %.1f | 습도: %.1f%%%n",
                        officeId, latest.getTemperature(), latest.getGasLevel(), latest.getHumidity());
            }
        });
    }

    // 🔹 특정 층 모니터링
    private void monitorSingleOffice(MemberDTO user, int officeId) {
        List<EnvironmentDTO> dataList = service.getRecentData(officeId);
        for (EnvironmentDTO data : dataList) {
            if (data.getTemperature() > 55 || data.getGasLevel() > 300) {
                fireAlert(user, data);
            } else {
                System.out.printf("[정상] %.1f°C | 가스 %.1f | 측정시각 %s%n",
                        data.getTemperature(), data.getGasLevel(), data.getMeasuredAt());
            }
        }
    }

    // 🔹 전체 층 모니터링 (건물 관리자)
    private void monitorAllFloors(MemberDTO user) {
        service.getAllOfficeData().forEach((officeId, dataList) -> {
            if (!dataList.isEmpty()) {
                EnvironmentDTO data = dataList.get(0);
                if (data.getTemperature() > 55 || data.getGasLevel() > 300) {
                    fireAlert(user, data);
                } else {
                    System.out.printf("[층 %d] 정상 - %.1f°C | %.1fppm%n",
                            officeId, data.getTemperature(), data.getGasLevel());
                }
            }
        });
    }

    // 🔹 화재 감지시 event_log 기록
    private void fireAlert(MemberDTO user, EnvironmentDTO data) {
        System.out.printf("🚨 화재 감지됨! [층 %d] 온도: %.1f°C, 가스농도: %.1f%n",
                data.getDeviceId(), data.getTemperature(), data.getGasLevel());

        FireEventDTO event = new FireEventDTO(
                data.getDeviceId(),
                user.getUserId(),
                user.getOfficeId(),
                "FIRE",
                "ALERT",
                String.valueOf(data.getTemperature()),
                "자동 화재 감지됨",
                Timestamp.valueOf(LocalDateTime.now())
        );
        service.recordFireEvent(event);
        sc.nextLine();
    }

//    // 🔹 전체 로그
//    private void printLogs() {
//        System.out.println("=== 최근 화재 로그 (전체) ===");
//        service.getFireLogs().forEach(log ->
//                System.out.printf("[%s] 층:%d | %s | %s%n",
//                        log.getTimestamp(), log.getOfficeId(),
//                        log.getEventAction(), log.getNote())
//        );
//    }

    // 🔹 특정 층 로그
    private void printLogsByOffice(int officeId) {
        System.out.printf("=== 최근 화재 로그 (층 %d) ===%n", officeId);
        service.getFireLogsByOffice(officeId).forEach(log ->
                System.out.printf("[%s] %s | %s%n",
                        log.getTimestamp(), log.getEventAction(), log.getNote())
        );
        sc.nextLine();
    }
}