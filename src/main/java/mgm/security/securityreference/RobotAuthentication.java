package mgm.security.secdev;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RobotAuthentication implements Authentication {

    private final List<GrantedAuthority> authorities;
    private final boolean iaAuthenticated;
    private final String password;

    public RobotAuthentication(List<GrantedAuthority> authorities, String password) {
        this.authorities = authorities;
        this.iaAuthenticated = password == null; //when you don't pass a password you're authenticated
        this.password = password;
    }

    public static RobotAuthentication unauthenticated(String password){
        return new RobotAuthentication(Collections.EMPTY_LIST, password);
    }

    public static RobotAuthentication authenticated(){
        return new RobotAuthentication(AuthorityUtils.createAuthorityList("ROLE_robot"), null);
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
        return iaAuthenticated;
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
