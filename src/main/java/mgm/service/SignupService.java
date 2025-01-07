package mgm.service;

import mgm.model.entity.Users;

public interface SignupService {
    void register(Users user);

    boolean usernameTaken(String username);

    boolean emailTaken(String email);
}
