package service;

import com.google.gson.Gson;
import dto.MemberDTO;
import dto.mqttMsg.MqttDeviceDTO;
import dto.mqttMsg.MqttElevatorDTO;
import mqtt.MqttManager;

public class ElevatorServiceImpl implements ElevatorService{
    private MemberDTO loginUser;
    private MqttManager mqttManager;
    private Gson gson;

    public ElevatorServiceImpl(MemberDTO loginUser, MqttManager mqttManager) {
        this.loginUser = loginUser;
        this.mqttManager = mqttManager;
        this.gson = new Gson();
    }

    @Override
    public void getEVState() {
        String action = "state_return";
        Boolean isActive = true;
        int userId = loginUser.getUserId();
        MqttDeviceDTO dto = new MqttDeviceDTO(action,isActive,userId);
        String msg = gson.toJson(dto);
        String topic = "1/elevator/ev1/cmd";
        mqttManager.publish(topic,msg);
    }

    @Override
    public void setEVEnable(Boolean isEnable) {
        String action = "state_change";
        int userId = loginUser.getUserId();
        MqttDeviceDTO dto = new MqttDeviceDTO(action,isEnable,userId);
        String msg = gson.toJson(dto);
        String topic = "1/elevator/ev1/cmd";
        mqttManager.publish(topic,msg);
    }

    @Override
    public void setEVFloor(int start, int end) {
        String action = "call";
        int userId = loginUser.getUserId();
        MqttDeviceDTO dto = new MqttElevatorDTO(action,null,userId,start,end);
        String msg = gson.toJson(dto);
        String topic = "1/elevator/ev1/cmd";
        mqttManager.publish(topic,msg);
    }

    @Override
    public void getEVLog() {

    }
}
