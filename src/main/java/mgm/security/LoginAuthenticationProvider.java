package mgm.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

public class LoginAuthenticationProvider implements AuthenticationProvider {
    private final List<String> passwords;

    public LoginAuthenticationProvider(List<String> password) {
        this.passwords = password;
    }

    @Override public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        var authRequest = (LoginAuthentication) authentication;
//        var password = authRequest.getPassword();
//        if (!passwords.contains(password)) {
//            throw new BadCredentialsException("You are not Ms Robot");
//        }
        System.out.println("AUTHENTICATION");
        return LoginAuthentication.authenticated();
        //return authentication;
    }

    @Override public boolean supports(Class<?> authentication) {
        return LoginAuthentication.class.isAssignableFrom(authentication);
    }
}
