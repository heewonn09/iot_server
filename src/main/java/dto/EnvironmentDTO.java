package dto;

import java.sql.Timestamp;

public class EnvironmentDTO {
    private int recordId;
    private int deviceId;
    private float temperature;
    private float humidity;
    private float gasLevel;
    private Timestamp measuredAt;

    public EnvironmentDTO(int recordId, int deviceId, float temperature, float humidity, float gasLevel, Timestamp measuredAt) {
        this.recordId = recordId;
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.gasLevel = gasLevel;
        this.measuredAt = measuredAt;
    }

    public int getRecordId() { return recordId; }
    public int getDeviceId() { return deviceId; }
    public float getTemperature() { return temperature; }
    public float getHumidity() { return humidity; }
    public float getGasLevel() { return gasLevel; }
    public Timestamp getMeasuredAt() { return measuredAt; }
}