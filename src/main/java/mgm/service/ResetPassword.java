package mgm.service;

public interface ResetPassword {

    boolean forgotPassword(String email);

    void resetPassword(String email);
}
