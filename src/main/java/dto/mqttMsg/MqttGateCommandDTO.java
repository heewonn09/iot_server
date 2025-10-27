package dto.mqttMsg;

public class MqttGateCommandDTO {
    private String command;
    private String carNo;
    private boolean authorized;

    public MqttGateCommandDTO() {
    }

    public MqttGateCommandDTO(String command, String carNo, boolean authorized) {
        this.command = command;
        this.carNo = carNo;
        this.authorized = authorized;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
