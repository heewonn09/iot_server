	package test;
	
	import dao.AdminParkingDAOImpl;
	import dto.ParkingDashboardDTO;
	
	public class AdminViewTest2 {
	    public static void main(String[] args) {
	        AdminParkingDAOImpl dao = new AdminParkingDAOImpl();
	        ParkingDashboardDTO dashboard = dao.getSystem();
	
	        System.out.println(dashboard);
	    }
	}
