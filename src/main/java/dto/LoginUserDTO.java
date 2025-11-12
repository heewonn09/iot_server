package dto;

public class LoginUserDTO {
    private String id;
    private String pw;

    public LoginUserDTO() {
    }
    public LoginUserDTO(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    @Override
    public String toString() {
        return "loginDTO{" + "id='" + id +", pw=" + pw + "}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
