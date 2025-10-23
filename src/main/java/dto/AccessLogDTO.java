package dto;

public class AccessLogDTO {
    private int userId;
    private int deviceId;
    private int officeId;
    private String action;
    private String note;

    public AccessLogDTO(int userId, int deviceId, int officeId, String action, String note) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.officeId = officeId;
        this.action = action;
        this.note = note;
    }

    public int getUserId() { return userId; }
    public int getDeviceId() { return deviceId; }
    public int getOfficeId() { return officeId; }
    public String getAction() { return action; }
    public String getNote() { return note; }
}