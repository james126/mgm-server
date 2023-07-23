package mgm.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

public class LoginConfigurer extends AbstractHttpConfigurer<LoginConfigurer, HttpSecurity> {

    private final List<String> passwords = new ArrayList<>();

    @Override
    public void init(HttpSecurity http) {
        // Called when http.build() is called.
        // All Configurers have their .init() called first.
        // Add AuthenticationProviders.

        http.authenticationProvider(new LoginAuthenticationProvider(passwords));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Called when http.build() is called.
        // All Configurers have their .configure() called after ALL configurers had .init() called.
        // Here we have the most objects in the http.getSharedObject cache.
        // Add Filters.
        var authManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(new CustomFilter(authManager), UsernamePasswordAuthenticationFilter.class);
    }

    public LoginConfigurer password(String password){
        this.passwords.add(password);
        return this;
    }
}
