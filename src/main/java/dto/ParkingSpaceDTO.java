package dto;

public class ParkingSpaceDTO {
	private int spaceId;
	private String loaction;
	private boolean isOccupied;
	private String vehicleno;
	private String lastUpdate;
	
	
	public ParkingSpaceDTO(int spaceId, String loaction, boolean isOccupied, String vehicleno, String lastUpdate) {
		super();
		this.spaceId = spaceId;
		this.loaction = loaction;
		this.isOccupied = isOccupied;
		this.vehicleno = vehicleno;
		this.lastUpdate = lastUpdate;
	}
	ParkingSpaceDTO(){
		
	}
	
	public int getSpaceId() {
		return spaceId;
	}
	
	public String getLocation() {
		return loaction;
		
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	
	public boolean getIsOccupied() {
		return isOccupied;
	}
	
	public String getVehicleno() {
		return vehicleno;
	}

	public void setLoaction(String loaction) {
		this.loaction = loaction;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	@Override
	public String toString() {
		return "ParkingSpaceDTO [spaceId=" + spaceId + ", loaction=" + loaction + ", isOccupied=" + isOccupied
				+ ", vehicleno=" + vehicleno + ", lastUpdate=" + lastUpdate + "]";
	}

	
	
}
