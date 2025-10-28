package service;

import com.google.gson.Gson;
import dao.ElevatorDAOImpl;
import dto.ElevatorLogDTO;
import dto.MemberDTO;
import dto.mqttMsg.MqttDeviceDTO;
import dto.mqttMsg.MqttElevatorDTO;
import mqtt.MqttManager;

import java.util.List;

public class ElevatorServiceImpl implements ElevatorService{
    private MemberDTO loginUser;
    private MqttManager mqttManager;
    private Gson gson;
    private String topic;


    public ElevatorServiceImpl(MemberDTO loginUser, MqttManager mqttManager,int officeId, int deviceId) {
        this.loginUser = loginUser;
        this.mqttManager = mqttManager;
        this.gson = new Gson();
        this.topic = officeId+"/"+"elevator"+"/"+deviceId+"/";

    }

    @Override
    public void requestEVState() {
        String action = "state_return";
        Boolean isActive = true;
        int userId = loginUser.getUserId();
        MqttDeviceDTO dto = new MqttDeviceDTO(action,isActive,userId);
        String msg = gson.toJson(dto);
        String topic = this.topic+"cmd";
        mqttManager.publish(topic,msg);
    }

    @Override
    public void setEVEnable(Boolean isEnable) {
        String action = "state_change";
        int userId = loginUser.getUserId();
        MqttDeviceDTO dto = new MqttDeviceDTO(action,isEnable,userId);
        String msg = gson.toJson(dto);
        String topic = this.topic+"cmd";
        mqttManager.publish(topic,msg);
    }

    @Override
    public void callEVFloor(int start, int end) {
        String action = "call";
        int userId = loginUser.getUserId();
        MqttDeviceDTO dto = new MqttElevatorDTO(action,null,userId,start,end);
        String msg = gson.toJson(dto);
        String topic = this.topic+"cmd";
        mqttManager.publish(topic,msg);
    }

    @Override
    public void showELVLog() {
        ElevatorDAOImpl elvDAO = new ElevatorDAOImpl();
        List<ElevatorLogDTO> elvLogList = elvDAO.showELVLog();
        if(elvLogList==null){
            System.out.println("======최근 엘리베이터를 이용한 기록이 존재하지 않습니다.======");
        }
        else{
            System.out.println("======엘리베이터를 호출 및 제어한 로그 데이터 정보를 출력합니다.=======");
            for(ElevatorLogDTO elvLog : elvLogList){
                System.out.println(elvLog);
            }
        }
    }

    @Override
    public void showUserFloor(){

    }
}
