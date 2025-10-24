package dto;

public class ParkingDashboardDTO {
	private int totalUsers;
	private int registeredVehicles;
	private int totalSpaces;
	private int usedSpaces;
	
	
	public ParkingDashboardDTO() {
		
	}
	

	@Override
	public String toString() {
		int emptySpaces = totalSpaces - usedSpaces;
		return """
	               ===== 주차장 시스템 전체 현황 =====
	               👤 전체 사용자: %d명
	               🚗 등록 차량: %d대
	               🅿️ 전체 주차공간: %d개 (사용 중: %d / 비어 있음: %d)
	               """.formatted(totalUsers, registeredVehicles, totalSpaces, usedSpaces, emptySpaces);
	}


	public ParkingDashboardDTO(int totalUsers, int registeredVehicles, int totalSpaces, int usedSpaces,
			int currntlyParked) {
		super();
		this.totalUsers = totalUsers;
		this.registeredVehicles = registeredVehicles;
		this.totalSpaces = totalSpaces;
		this.usedSpaces = usedSpaces;
	}


	public int getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}



	public int getRegisteredVehicles() {
		return registeredVehicles;
	}

	public void setRegisteredVehicles(int registeredVehicles) {
		this.registeredVehicles = registeredVehicles;
	}

	public int getTotalSpaces() {
		return totalSpaces;
	}

	public void setTotalSpaces(int totalSpaces) {
		this.totalSpaces = totalSpaces;
	}

	public int getUsedSpaces() {
		return usedSpaces;
	}

	public void setUsedSpaces(int usedSpaces) {
		this.usedSpaces = usedSpaces;
	}

}
