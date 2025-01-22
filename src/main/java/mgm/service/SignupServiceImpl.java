package mgm.service;

import lombok.NonNull;
import mgm.model.entity.Authorities;
import mgm.model.entity.Users;
import mgm.repository.AuthoritiesRepository;
import mgm.repository.SignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignupServiceImpl implements SignupService {
    private final SignupRepository signupRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Autowired
    public SignupServiceImpl(SignupRepository signupRepository, AuthoritiesRepository authoritiesRepository) {
        this.signupRepository = signupRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    @Override
    public void register(@NonNull Users user, Authorities authorities) {
        signupRepository.saveAndFlush(user);
        authoritiesRepository.saveAndFlush(authorities);
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
