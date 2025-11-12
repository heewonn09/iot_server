package dto;

import java.sql.Timestamp;

public class MemberDTO {
    private int userId;
    private String id;
    private String name;
    private String cardId;
    private String pass;
    private int access_level;
    private int officeId;
    private String vehicleNo;
    private Boolean isActive;
    private Timestamp created_at;

    public MemberDTO() {
    }

    public MemberDTO(int userId, String id, String name, String cardId, String pass, int access_level, int officeId, String vehicleNo,  Boolean isActive, Timestamp created_at) {
        this.userId = userId;
        this.id = id;
        this.name = name;
        this.cardId = cardId;
        this.pass = pass;
        this.access_level = access_level;
        this.officeId = officeId;
        this.vehicleNo = vehicleNo;
        this.isActive = isActive;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "userId=" + userId +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", pass='" + pass + '\'' +
                ", access_level=" + access_level +
                ", officeId=" + officeId +
                ", vehicleNo=" + vehicleNo +
                ", isActive=" + isActive +
                ", created_at=" + created_at +
                '}';
    }
    
    public void setIsActive(boolean isActive) {
    	this.isActive = isActive;
    }
    public boolean getIsActive() {
    	return isActive;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getAccess_level() {
        return access_level;
    }

    public void setAccess_level(int access_level) {
        this.access_level = access_level;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

	public void setVehicle_no(String vehicleNo) {
		this.vehicleNo = vehicleNo;
		
	}
	public String getVehicle_no() {
		return vehicleNo;
		
	}


	
}
