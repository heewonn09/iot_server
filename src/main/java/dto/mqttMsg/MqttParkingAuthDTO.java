package dto.mqttMsg;

import com.google.gson.annotations.SerializedName;

public class MqttParkingAuthDTO {
    @SerializedName("carNo")
    private String carNo;
    @SerializedName("status")
    private String status;

    public String getCarNo() { return carNo; }
    public void setCarNo(String carNo) { this.carNo = carNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
