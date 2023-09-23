package mgm.security;

import mgm.model.entity.Authorities;
import mgm.model.entity.Users;
import mgm.repository.AuthoritiesRepository;
import mgm.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> optionalUser = userRepository.findByUsername(username);
        Optional<Authorities> optionalAuthorities = authoritiesRepository.findAuthorityByUsername(username);

        Users user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Authorities authorities = optionalAuthorities.orElseThrow(() -> new RuntimeException("User authority not found with username: " + username));

        return new mgm.model.CustomUserDetails(user.getUsername(), user.getPassword(),
                AuthorityUtils.createAuthorityList(authorities.getAuthority()),
                user.getEnabled());
    }
}
