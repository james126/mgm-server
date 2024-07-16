package mgm.service;

import mgm.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResetPasswordImpl implements ResetPassword {
    private final UsersRepository repository;

    @Autowired
    public ResetPasswordImpl(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean forgotPassword(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public void resetPassword(String email) {

    }
}
