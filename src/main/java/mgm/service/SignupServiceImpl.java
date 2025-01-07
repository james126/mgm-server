package mgm.service;

import lombok.NonNull;
import mgm.model.entity.Users;
import mgm.repository.SignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignupServiceImpl implements SignupService {
    private final SignupRepository signupRepository;

    @Autowired
    public SignupServiceImpl(SignupRepository signupRepository) {
        this.signupRepository = signupRepository;
    }

    @Override
    public void register(@NonNull Users user) {
        signupRepository.saveAndFlush(user);
    }

    @Override
    public boolean usernameTaken(@NonNull String username) {
        return signupRepository.usernameTaken(username);
    }

    @Override
    public boolean emailTaken(@NonNull String email) {
        return signupRepository.emailTaken(email);
    }
}
