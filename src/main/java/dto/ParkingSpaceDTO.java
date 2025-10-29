package dto;

import java.sql.Timestamp;

public class ParkingSpaceDTO {
	 	private int spaceId;
	    private String location;
	    private boolean isOccupied;
	    private Timestamp lastUpdate;
	    private String currentUserName;
		public int getSpaceId() {
			return spaceId;
		}
		public void setSpaceId(int spaceId) {
			this.spaceId = spaceId;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public boolean isOccupied() {
			return isOccupied;
		}
		public void setOccupied(boolean isOccupied) {
			this.isOccupied = isOccupied;
		}
		public Timestamp getLastUpdate() {
			return lastUpdate;
		}
		public void setLastUpdate(Timestamp lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
		public String getCurrentUserName() {
			return currentUserName;
		}
		public void setCurrentUserName(String currentUserName) {
			this.currentUserName = currentUserName;
		}
		public ParkingSpaceDTO(String currentUserName, Timestamp lastUpdate, boolean isOccupied, String location, int spaceId) {
			super();
			this.spaceId = spaceId;
			this.location = location;
			this.isOccupied = isOccupied;
			this.lastUpdate = lastUpdate;
			this.currentUserName = currentUserName;
		}
		public ParkingSpaceDTO() {
			
		}
		@Override
		public String toString() {
			 String status = isOccupied ? "🅿️ 사용중" : "✅ 비어있음";
		        String user = (currentUserName != null) ? currentUserName : "-";
		        return String.format("[%d] %s | %s | 사용자: %s", spaceId, location, status, user);
		    }
	
	    
}