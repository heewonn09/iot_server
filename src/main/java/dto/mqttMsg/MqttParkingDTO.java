package dto.mqttMsg;

import com.google.gson.annotations.SerializedName;

public class MqttParkingDTO {
    @SerializedName("carNo")  // JSON에서 이 이름으로 받을 때 매핑됨
    private String carNo;
    @SerializedName("status")
    private String Action;

    
    public String getCarNo() { return carNo; }
    public void setCarNo(String carNo) { this.carNo = carNo; }
    public String getAction() { return Action; }
    public void setAction(String action) { this.Action = action; }
}
