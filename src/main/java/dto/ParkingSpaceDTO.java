package dto;

import java.sql.Timestamp;

public class ParkingSpaceDTO {
	private int spaceId;
	private String location;
	private boolean isOccupied;
	private String vehicleno;
	private Timestamp  lastUpdate;
	
	
	public ParkingSpaceDTO(int spaceId, String location, boolean isOccupied, String vehicleno, Timestamp lastUpdate) {
		super();
		this.spaceId = spaceId;
		this.location = location;
		this.isOccupied = isOccupied;
		this.vehicleno = vehicleno;
		this.lastUpdate = lastUpdate;
	}
	public ParkingSpaceDTO(){
		
	}
	
	public int getSpaceId() {
		return spaceId;
	}
	
	public String getLocation() {
		return location;
		
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	
	public boolean getIsOccupied() {
		return isOccupied;
	}
	
	public String getVehicleno() {
		return vehicleno;
	}

	public void setLocation(String location) {
		this.location = location;
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
		return "ParkingSpaceDTO [spaceId=" + spaceId + ", location=" + location + ", isOccupied=" + isOccupied
				+ ", vehicleno=" + vehicleno + ", lastUpdate=" + lastUpdate + "]";
	}
	public void setLastUpdate(Timestamp timestamp) {
		
		
	}

	
	
}
