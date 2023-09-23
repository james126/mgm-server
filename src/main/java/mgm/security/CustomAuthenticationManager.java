package mgm.security;

import mgm.security.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return customAuthenticationProvider.authenticate(authentication);

    }
}
