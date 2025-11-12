package dto;

import java.sql.Timestamp;

public class ElevatorLogDTO {
    private int elevator_id;
    private int user_id;
    private int from_floor;
    private int to_floor;
    private String statue; // ENUM('CALLED','ARRIVED','DENIED') DEFAULT 'CALLED', -- 상태
    private Timestamp requested_at; // DEFAULT CURRENT_TIMESTAMP

    public ElevatorLogDTO(int elevator_id, int user_id,  int from_floor, int to_floor, String statue, Timestamp requested_at) {
        this.elevator_id = elevator_id;
        this.user_id = user_id;
        this.from_floor = from_floor;
        this.to_floor = to_floor;
        this.statue = statue;
        this.requested_at = requested_at;
    }

    @Override
    public String toString() {
        return "ElevatorLogDTO{" +
                "elevator_id=" + elevator_id +
                ", user_id=" + user_id +
                ", from_floor=" + from_floor +
                ", to_floor=" + to_floor +
                ", statue='" + statue + '\'' +
                ", requested_at=" + requested_at +
                '}';
    }

    public int getElevator_id() {
        return elevator_id;
    }

    public void setElevator_id(int elevator_id) {
        this.elevator_id = elevator_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFrom_floor() {
        return from_floor;
    }

    public void setFrom_floor(int from_floor) {
        this.from_floor = from_floor;
    }

    public int getTo_floor() {
        return to_floor;
    }

    public void setTo_floor(int to_floor) {
        this.to_floor = to_floor;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public Timestamp getRequested_at() {
        return requested_at;
    }

    public void setRequested_at(Timestamp requested_at) {
        this.requested_at = requested_at;
    }
}
