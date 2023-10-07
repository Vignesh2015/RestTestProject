package pojo_classes;

public class LoginRequest {

    private String userEmail;
    private String userPassword;

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }
}
