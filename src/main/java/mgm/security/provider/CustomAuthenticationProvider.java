package mgm.security.provider;

import mgm.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    CustomUserDetailsService customUserDetails;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username;
        String password;
        UserDetails user;
        try {
            username = authentication.getName();
            password = authentication.getCredentials().toString();
            user = customUserDetails.loadUserByUsername(username);
        } catch (RuntimeException e){
            throw new AccessDeniedException(e.getMessage());
        }

        if (passwordEncoder.matches(password, user.getPassword())){
            return UsernamePasswordAuthenticationToken.authenticated(
                    username,
                    password,
                    user.getAuthorities()
            );
        }

        return null;
    }

    @Override public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
