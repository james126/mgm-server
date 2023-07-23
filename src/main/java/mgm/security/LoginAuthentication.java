package mgm.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LoginAuthentication implements Authentication {

    private final List<GrantedAuthority> authorities;
    private final boolean isAuthenticated;
    private final String password;

    public LoginAuthentication(List<GrantedAuthority> authorities, String password) {
        this.authorities = authorities;
        this.isAuthenticated = password == null; //when you don't pass a password you're authenticated
        this.password = password;
    }

    public static LoginAuthentication unauthenticated(String password){
        return new LoginAuthentication(Collections.EMPTY_LIST, password);
    }

    public static LoginAuthentication authenticated(){
        return new LoginAuthentication(AuthorityUtils.createAuthorityList("ROLE_robot"), null);
    }


    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public Object getCredentials() {
        return null;
    }

    @Override public Object getDetails() {
        return null;
    }

    @Override public Object getPrincipal() {
        return getName();
    }

    @Override public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Is always authenticated");
    }

    @Override public String getName() {
        return "Ms Robot";
    }

    public String getPassword() {
        return password;
    }
}
