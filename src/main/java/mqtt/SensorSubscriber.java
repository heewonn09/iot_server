package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import util.DBUtil;
import java.sql.*;

public class SensorSubscriber implements IMqttMessageListener {

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        System.out.println("📨 센서 데이터 수신:");
        System.out.println(" Topic: " + topic);
        System.out.println(" Payload: " + payload);

        try {
            // JSON 파싱 (substring 사용 - GSON 라이브러리 없음)
            String room = extractJsonValue(payload, "room");
            String ledStatus = extractJsonValue(payload, "led_status");
            double temperature = Double.parseDouble(extractJsonValue(payload, "temperature"));
            double humidity = Double.parseDouble(extractJsonValue(payload, "humidity"));

            System.out.println("🔍 파싱 완료:");
            System.out.println(" 호실: " + room);
            System.out.println(" 온도: " + temperature + "°C");
            System.out.println(" 습도: " + humidity + "%");
            System.out.println(" LED 상태: " + ledStatus);

            // DB에 센서 데이터 저장
            updateSensorData(room, temperature, humidity, ledStatus);

            // 자동 제어 판단 (온도 >= 30°C AND 습도 >= 70%)
            if (temperature >= 30 && humidity >= 70) {
                System.out.println("🔴 경고: 높은 온습도 감지! 환풍펜 자동 ON");
                // 자동 제어 신호는 MqttManager를 통해 발행
                // MqttManager.publishAutoControlStatus(room, temperature, humidity, "ON");
            } else if (temperature >= 28 && humidity >= 60) {
                System.out.println("🟠 주의: 온습도 상승 추세 감지");
            } else {
                System.out.println("🟢 정상: 쾌적한 환경");
            }

        } catch (Exception e) {
            System.err.println("❌ JSON 파싱 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // JSON에서 특정 키의 값 추출
    private String extractJsonValue(String json, String key) {
        String searchStr = "\"" + key + "\":";
        int startIdx = json.indexOf(searchStr);
        
        if (startIdx == -1) {
            return "";
        }
        
        startIdx += searchStr.length();
        
        // 다음 쉼표 또는 닫는 중괄호 찾기
        int endIdx = json.indexOf(",", startIdx);
        if (endIdx == -1) {
            endIdx = json.indexOf("}", startIdx);
        }
        
        if (endIdx == -1) {
            endIdx = json.length();
        }
        
        String value = json.substring(startIdx, endIdx).trim();
        value = value.replaceAll("\"", "");  // 따옴표 제거
        
        return value;
    }

    // DB에 센서 데이터 저장
    private void updateSensorData(String room, double temperature, double humidity, String ledStatus) {
        Connection con = null;
        try {
            con = DBUtil.getConnect();
            
            // 온도 업데이트
            updateDeviceStatus(con, room + "%", "DHT", "온도센서", temperature + "°C");
            
            // 습도 업데이트
            updateDeviceStatus(con, room + "%", "DHT", "습도센서", humidity + "%");
            
            // LED 상태 업데이트
            updateDeviceStatus(con, room + "%", "LED", "LED조명", ledStatus);
            
            System.out.println("✅ 센서 데이터 DB 저장 완료 (온도: " + temperature + "°C, 습도: " + humidity + "%)");
            
        } catch (SQLException e) {
            System.err.println("❌ 센서 데이터 저장 실패: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(null, null, con);
        }
    }

    // 기기 상태 업데이트
    private void updateDeviceStatus(Connection con, String roomPattern, String type, String namePattern, String status) {
        String sql = "UPDATE device SET status = ? WHERE name LIKE ? AND type = ?";
        PreparedStatement pstmt = null;
        
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, roomPattern + " " + namePattern + "%");
            pstmt.setString(3, type);
            
            int result = pstmt.executeUpdate();
            System.out.println("  📊 DB 업데이트: " + roomPattern + " - " + namePattern + " = " + status + " (" + result + " rows)");
            
        } catch (SQLException e) {
            System.err.println("  ❌ 상태 업데이트 실패: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                // 무시
            }
        }
    }
}