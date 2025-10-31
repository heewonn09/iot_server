package dto;

public class DeviceDTO {
	private int room_id;
	private String room_name;
	private String device_name;
	private String device_type;
	private String status;
	private double temperature;
	private double humidity;

	public DeviceDTO(int room_id, String room_name, String device_name,
                     String device_type, String status, double temperature, double humidity) {
		this.room_id = room_id;
		this.room_name = room_name;
		this.device_name = device_name;
		this.device_type = device_type;
		this.status = status;
		this.temperature = temperature;
		this.humidity = humidity;
	}

	public int getRoom_id() { return room_id; }
	public void setRoom_id(int room_id) { this.room_id = room_id; }
	public String getRoom_name() { return room_name; }
	public void setRoom_name(String room_name) { this.room_name = room_name; }
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