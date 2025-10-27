package dto.mqttMsg;

public class MqttElevatorDTO extends MqttDeviceDTO{
    private int start_floor;
    private int end_floor;

    public MqttElevatorDTO(String action, Boolean state, int userId) {
        super(action, state, userId);
    }
    public MqttElevatorDTO(String action, Boolean state, int userId, int start_floor, int end_floor) {
        super(action, state, userId);
        this.start_floor = start_floor;
        this.end_floor = end_floor;
    }

    public int getStart_floor() {
        return start_floor;
    }

    public void setStart_floor(int start_floor) {
        this.start_floor = start_floor;
    }

    public int getEnd_floor() {
        return end_floor;
    }

    public void setEnd_floor(int end_floor) {
        this.end_floor = end_floor;
    }
}
