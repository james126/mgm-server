package mgm.service;

import mgm.repository.ResetPasswordRepository;
import mgm.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResetPasswordImpl implements ResetPasswordService {
    private final UsersRepository users;
    private final ResetPasswordRepository resetPassword;

    @Autowired
    public ResetPasswordImpl(UsersRepository repository, ResetPasswordRepository resetPassword) {
        this.users = repository;
        this.resetPassword = resetPassword;
    }

    @Override
    public boolean forgotPassword(String email) {
        return users.emailExists(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.existsByUsername(username);
    }

    @Override
    public void updatePassword(String password, String username) {
        this.resetPassword.updatePassword(password, username);
    }

    @Override
    public void submitTempPassword(String password, String email) {
        this.resetPassword.submitTempPassword(password, email);
    }
}
