package dto;

public class DeviceDTO {
	private int office_id;
	private int device_id;
	private String device_name;
	private String device_type;
	private String status;
	private double temperature;
	private double humidity;

	public DeviceDTO(int office_id, int device_id, String device_name,
                     String device_type, String status, double temperature, double humidity) {
		this.office_id = office_id;
		this.device_id = device_id;
		this.device_name = device_name;
		this.device_type = device_type;
		this.status = status;
		this.temperature = temperature;
		this.humidity = humidity;
	}

	public int getOffice_id() { return office_id; }
	public void setOffice_id(int office_id) { this.office_id = office_id; }
	public int getDevice_id() { return device_id; }
	public void setDevice_id(int device_id) { this.device_id = device_id; }
	public String getDevice_name() { return device_name; }
	public void setDevice_name(String device_name) { this.device_name = device_name; }
	public String getDevice_type() { return device_type; }
	public void setDevice_type(String device_type) { this.device_type = device_type; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public double getTemperature() { return temperature; }
	public void setTemperature(double temperature) { this.temperature = temperature; }
	public double getHumidity() { return humidity; }
	public void setHumidity(double humidity) { this.humidity = humidity; }
}