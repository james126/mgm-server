package mgm.service;

import mgm.model.entity.Authorities;
import mgm.model.entity.Users;

public interface SignupService {
    void register(Users user, Authorities authorities);

    boolean usernameTaken(String username);

    boolean emailTaken(String email);
}
