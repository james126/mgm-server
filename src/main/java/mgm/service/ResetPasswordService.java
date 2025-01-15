package mgm.service;

public interface ResetPasswordService {

    boolean forgotPassword(String email);

    boolean existsByUsername(String username);

    void updatePassword(String password, String username);

    void submitTempPassword(String password, String email);
}
