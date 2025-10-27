package controller;

import java.util.List;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import dao.AdminParkingDAOImpl;
import dao.UserDAOImpl;
import dto.MemberDTO;
import dto.ParkingDashboardDTO;
import dto.ParkingSpaceDTO;
import dto.ParkingSummaryDTO;
import mqtt.MqttSubClientParking;  // ✅ 추가

public class ParkedController {

    private final AdminParkingDAOImpl dao = new AdminParkingDAOImpl();
    private MqttClient mqttClient;

    public ParkedController() {
        connectMqtt();
    }

    private void connectMqtt() {
        try {
            if (mqttClient == null || !mqttClient.isConnected()) {
                mqttClient = new MqttClient("tcp://192.168.14.56:1883", MqttClient.generateClientId());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                mqttClient.connect(options);
                
            }
        } catch (MqttException e) {
            System.out.println("⚠️ MQTT 연결 실패: " + e.getMessage());
        }
    }

    public void adminParked(MemberDTO currentUser) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== 🅰️ 관리자 메뉴 =====");
            System.out.println("1. 주차 공간 상세 현황 보기");
            System.out.println("2. 시스템 대시보드 보기");
            System.out.println("3. 사용자 주차 이력 요약 보기");
            System.out.println("4. 주차장 센서 활성화");
            System.out.println("5. 상위 메뉴로 이동");
            System.out.print("메뉴 선택: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> {
                    List<ParkingSpaceDTO> spaces = dao.getAllSpace();
                    if (spaces == null || spaces.isEmpty()) {
                        System.out.println("⚠️ 등록된 주차 공간이 없습니다.");
                    } else {
                        spaces.forEach(System.out::println);
                    }
                }

                case "2" -> {
                    ParkingDashboardDTO dashboard = dao.getSystem();
                    System.out.println((dashboard != null) ? dashboard : "⚠️ 대시보드 정보를 불러올 수 없습니다.");
                }

                case "3" -> {
                    List<ParkingSummaryDTO> list = dao.getUserParkingSummary();
                    if (list == null || list.isEmpty()) {
                        System.out.println("⚠️ 주차 이력 데이터가 없습니다.");
                    } else {
                        list.forEach(System.out::println);
                    }
                }

                case "4" -> {
                    try {
                        if (mqttClient == null || !mqttClient.isConnected()) connectMqtt();

                        Thread subThread = new Thread(() -> {
                            try {
                                System.out.println("🛰 차량 감지 스레드 실행 중...");
                                MqttSubClientParking sub = new MqttSubClientParking();
                                sub.start();
                            } catch (Exception e) {
                                System.out.println("⚠️ 구독 스레드 오류: " + e.getMessage());
                            }
                        });
                        subThread.setDaemon(true); // 백그라운드 스레드로 설정 (메인 블로킹 방지)
                        subThread.start();

                        // ✅ 라즈베리파이에 센서 활성화 명령 전송
                        String topic = "1/parking/01/cmd";
                        String msg = "{\"action\":\"activate\"}";
                        MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
                        mqttMessage.setQos(0);
                        mqttClient.publish(topic, mqttMessage);

                        System.out.println("📤 MQTT Publish → " + topic + " : " + msg);
                        System.out.println("✅ 주차장 센서 활성화 명령 전송 완료!");
                        System.out.println("💡 차량 감지 스레드가 백그라운드에서 대기 중입니다.");
                        
                    } catch (Exception e) {
                        System.out.println("❌ MQTT 전송 실패: " + e.getMessage());
                    }
                }

                case "5" -> {
                    System.out.println("관리자 메뉴를 종료하고 상위 메뉴로 이동합니다");
                    running = false;
                }

                default -> System.out.println("⚠️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
        sc.close();
    }



	public void userhandleAccess(MemberDTO currentUser) {
		final UserDAOImpl dao2 = new UserDAOImpl();
		Scanner sc = new Scanner(System.in);

		boolean running = true;
		while (running) {
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

				MemberDTO dto = dao2.getUserInfo(inputId);

				if (dto != null) {
					System.out.println("조회 성공 ✅");
					System.out.println("-----------------------------------");
					System.out.println("회원번호: " + dto.getUserId());
					System.out.println("아이디: " + dto.getId());
					System.out.println("이름: " + dto.getName());
					System.out.println("카드ID: " + dto.getCardId());
					System.out.println("차량번호: " + dto.getVehicle_no());
					System.out.println("권한레벨: " + dto.getAccess_level());
					System.out.println("활성상태: " + ((dto.getActive() != null && dto.getActive()) ? "활성" : "비활성"));
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

				boolean result = dao2.updateVehicle(id, vehicleNo);

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
