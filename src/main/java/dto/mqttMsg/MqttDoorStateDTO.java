package dto.mqttMsg;

import com.google.gson.annotations.SerializedName;

public class MqttDoorStateDTO {
    @SerializedName("action")
    private String action;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
