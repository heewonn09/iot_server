package dto;

public class ParkingLogDTO {
	private int parkingId;
	private int userId;
	private int spaceId;
	private int durationMin;
	private String action;
	private String note;
	private String inTime;
	private String outTime;
	
	
	public ParkingLogDTO(int parkingId, int userId, int spaceId, int durationMin, String action, String note,
			String inTime, String outTime) {
		super();
		this.parkingId = parkingId;
		this.userId = userId;
		this.spaceId = spaceId;
		this.durationMin = durationMin;
		this.action = action;
		this.note = note;
		this.inTime = inTime;
		this.outTime = outTime;
	}
	ParkingLogDTO(){
		
	}

	public int getParkingId() {
		return parkingId;
	}


	public void setParkingId(int parkingId) {
		this.parkingId = parkingId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}


	public int getDurationMin() {
		return durationMin;
	}


	public void setDurationMin(int durationMin) {
		this.durationMin = durationMin;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getInTime() {
		return inTime;
	}


	public void setInTime(String inTime) {
		this.inTime = inTime;
	}


	public String getOutTime() {
		return outTime;
	}


	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}


	@Override
	public String toString() {
		return "ParkingLogDTO [parkingId=" + parkingId + ", userId=" + userId + ", spaceId=" + spaceId
				+ ", durationMin=" + durationMin + ", action=" + action + ", note=" + note + ", inTime=" + inTime
				+ ", outTime=" + outTime + "]";
	}
	
	
	
	
	
}
