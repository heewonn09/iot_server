package service;

import dto.MemberDTO;
import mqtt.MqttManager;

public interface ElevatorService {
    void getEVState(); //엘리베이터 상태 조회
    void setEVEnable(Boolean isEnable); //엘리베이터 이용 상태 변경
    void setEVFloor(int start, int end); //엘리베이터 위치 제어(층 변경)
    void getEVLog();

}
