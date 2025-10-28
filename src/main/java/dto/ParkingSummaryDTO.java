package dto;

public class ParkingSummaryDTO {
	private String name;
	private String vehicleNo;
	private String lastIn;
	private String lastOut;
	private int totalLogs;
	private int totalMinutes;
	public ParkingSummaryDTO(String name, String vehicleNo, String lastIn, String lastOut, int totalLogs,
			int totalMinutes) {
		super();
		this.name = name;
		this.vehicleNo = vehicleNo;
		this.lastIn = lastIn;
		this.lastOut = lastOut;
		this.totalLogs = totalLogs;
		this.totalMinutes = totalMinutes;
	}
	public ParkingSummaryDTO() {
		
	}
	 public String toString() {
	        return String.format("%-8s | 차량번호: %-10s | 최근입차: %-19s | 최근출차: %-19s | 이용횟수: %-2d | 총주차시간: %-4d분",
	                name, vehicleNo, lastIn, lastOut, totalLogs, totalMinutes);
	 }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getLastIn() {
		return lastIn;
	}
	public void setLastIn(String lastIn) {
		this.lastIn = lastIn;
	}
	public String getLastOut() {
		return lastOut;
	}
	public void setLastOut(String lastOut) {
		this.lastOut = lastOut;
	}
	public int getTotalLogs() {
		return totalLogs;
	}
	public void setTotalLogs(int totalLogs) {
		this.totalLogs = totalLogs;
	}
	public int getTotalMinutes() {
		return totalMinutes;
	}
	public void setTotalMinutes(int totalMinutes) {
		this.totalMinutes = totalMinutes;
	}
	
	
}
