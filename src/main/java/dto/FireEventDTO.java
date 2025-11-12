package dto;

import java.sql.Timestamp;

public class FireEventDTO {
    private int deviceId;
    private int userId;
    private int officeId;
    private String eventType;
    private String eventAction;
    private String value;
    private String note;
    private Timestamp timestamp;
    
    public FireEventDTO() {}

    public FireEventDTO(int deviceId, int userId, int officeId, String eventType, String eventAction, String value, String note, Timestamp timestamp) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.officeId = officeId;
        this.eventType = eventType;
        this.eventAction = eventAction;
        this.value = value;
        this.note = note;
        this.timestamp = timestamp;
    }

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getOfficeId() {
		return officeId;
	}

	public void setOfficeId(int officeId) {
		this.officeId = officeId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventAction() {
		return eventAction;
	}

	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

   
}