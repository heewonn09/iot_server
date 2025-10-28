package dto;

import java.sql.Timestamp;

public class OfficeDTO {
    private int officeId;
    private String name;
    private int floorNo;
    private Timestamp createdAt;

    public OfficeDTO(int officeId, String name, int floorNo, Timestamp createdAt) {
        this.officeId = officeId;
        this.name = name;
        this.floorNo = floorNo;
        this.createdAt = createdAt;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
