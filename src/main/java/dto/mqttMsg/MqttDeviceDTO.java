package dto.mqttMsg;

public class MqttDeviceDTO {
    private String action;
    private Boolean state;
    private int userId;

    public MqttDeviceDTO() {
    }
    public MqttDeviceDTO(String action, Boolean state, int userId) {
        this.action = action;
        this.state = state;
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
