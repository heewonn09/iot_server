package dto;

import java.sql.Timestamp;

public class MemberDTO {
    private int id;
    private String userId;
    private String pass;
    private String name;
    private String cardId;
    private int access_level;
    private int officeId;
    private Timestamp created_at;

    public MemberDTO() {
    }
    public MemberDTO(int id, String userId, String pass, String name, String cardId, int access_level, int officeId, Timestamp created_at) {
        this.id = id;
        this.userId = userId;
        this.pass = pass;
        this.name = name;
        this.cardId = cardId;
        this.access_level = access_level;
        this.officeId = officeId;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", pass='" + pass + '\'' +
                ", name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", access_level=" + access_level +
                ", officeId=" + officeId +
                ", created_at=" + created_at +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
