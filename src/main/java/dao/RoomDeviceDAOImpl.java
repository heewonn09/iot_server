package dao;

import util.DBUtil;
import dto.DeviceDTO;
import mqtt.MqttManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDeviceDAOImpl implements RoomDeviceDAO {
	//생성자 만들어서 매개변수로 mqttManager 불러와야댐
	private MqttManager mqttManager;
	public RoomDeviceDAOImpl(MqttManager mqttManager) {
		this.mqttManager = mqttManager;
	}

	@Override
	public List<DeviceDTO> getDeviceListByOffice(int officeId, String officeName) {
		// ✅ 수정: 기기별 1개씩만 선택 (GROUP BY type 사용)
		String sql = "SELECT * FROM devices " +
				"WHERE office_id = ? AND type IN ('LED', 'DHT', 'HVAC')";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<DeviceDTO> list = new ArrayList<>();

		try {
			con = DBUtil.getConnect();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, officeId);

			System.out.println("🔍 검색: " + officeName);
			rs = pstmt.executeQuery();
			
			int count = 0;
			double temperature = 0.0;
			double humidity = 0.0;

			while (rs.next()) {
				count++;
				int id = rs.getInt("office_id");
				String name = rs.getString("name");
				String type = rs.getString("type");
				String status = rs.getString("status");
				System.out.println("🔍 디바이스 " + count + ":  " + name + " (" + type + ")");
                System.out.println("   현재 상태 : "+status);

				// ✅ DHT 타입이면 온습도 데이터를 모두 수집
				if (type.equals("DHT")) {
					// 이 부분은 나중에 처리
				}
			}

			// ✅ 두 번째 쿼리: DHT 센서의 모든 데이터를 가져오기
			String dhtSql = "SELECT * FROM devices " +
					"WHERE office_id = ? AND type = 'DHT' " +
					"ORDER BY name";
			
			pstmt = con.prepareStatement(dhtSql);
			pstmt.setInt(1, officeId);
			rs = pstmt.executeQuery();
			
			System.out.println("\n🔍 DHT 센서 데이터 수집:");
			while (rs.next()) {
				String status = rs.getString("status");
				
				try {
					if (status != null) {
						if (status.contains("°C")) {
							temperature = Double.parseDouble(status.replace("°C", "").trim());
							System.out.println("   → 온도: " + temperature + "°C");
						} else if (status.contains("%")) {
							humidity = Double.parseDouble(status.replace("%", "").trim());
							System.out.println("   → 습도: " + humidity + "%");
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("   ⚠️ 파싱 실패: " + status);
				}
			}

			// ✅ 다시 한 번 GROUP BY 쿼리로 기본 정보 가져오기
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, officeId);
			rs = pstmt.executeQuery();
			
			count = 0;
			while (rs.next()) {
				count++;
				int room_id = rs.getInt("office_id");
				String name = rs.getString("name");
				String type = rs.getString("type");
				String status = rs.getString("status");

				DeviceDTO dto = new DeviceDTO(
					room_id,
					name,
					name,
					type,
					status,
					(type.equals("DHT") ? temperature : 0.0),
					(type.equals("DHT") ? humidity : 0.0)
				);
				list.add(dto);
			}

			System.out.println("🔍 총 " + count + "개 조회됨\n");

		} catch (SQLException e) {
			System.err.println("❌ SQL 오류: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, pstmt, con);
		}

		return list;
	}

	@Override
	public int updateStatus(int officeId, String device_name, String status) {
		String sql = "UPDATE devices SET status = ?, last_updated = CURRENT_TIMESTAMP WHERE office_id = ? AND name = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			con = DBUtil.getConnect();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, status);
			pstmt.setInt(2, officeId);
			pstmt.setString(3, device_name);
			result = pstmt.executeUpdate();

			if (result > 0) {
				//logDeviceControl(con, officeId, device_name, status);
				publishMqtt(device_name, status);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}

		return result;
	}

	private void logDeviceControl(Connection con, int room_id, int device_name, String status) {
		String sql = "INSERT INTO event_log (office_id, device_id, event_action) VALUES (?, ?, ?)";
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, room_id);
			pstmt.setInt(2, device_name);
			pstmt.setString(3, status);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("❌ 로그 기록 실패: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
	}

	private void publishMqtt(String device_name, String status) {
		try {
			String office = device_name.split(" ")[0];

			if (device_name.contains("LED") || device_name.contains("조명")) {
				String topic = "office/" + office + "/led";
				String message = "{\"action\":\"" + status.toUpperCase() + "\",\"device_id\":1}";
				mqttManager.publish(topic,message);
				System.out.println("📡 LED 제어 MQTT 발행: " + device_name + " -> " + status);

			} else if (device_name.contains("환풍") || device_name.contains("AC")) {
				String topic = "office/" + office + "/ac";
				String message = "{\"action\":\"" + status.toUpperCase() + "\",\"device_id\":2}";
				mqttManager.publish(topic,message);
				System.out.println("📡 AC 제어 MQTT 발행: " + device_name + " -> " + status);

			} else if (device_name.contains("팬") || device_name.contains("쿨링") || device_name.contains("FAN")) {
				String topic = "office/" + office + "/fan";
				String message = "{\"action\":\"" + status.toUpperCase() + "\",\"device_id\":3}";
				mqttManager.publish(topic,message);
				System.out.println("📡 FAN 제어 MQTT 발행: " + device_name + " -> " + status);

			} else {
				System.out.println("⚠️ " + device_name + "는 제어 불가능한 센서입니다.");
			}

		} catch (Exception e) {
			System.err.println("❌ MQTT 발행 오류: " + e.getMessage());
		}
	}
}
