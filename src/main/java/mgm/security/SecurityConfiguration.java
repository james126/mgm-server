package mgm.security;

import mgm.security.filter.InputStreamCachingFilter;
import mgm.security.filter.JwtAuthenticationFilter;
import mgm.security.filter.PrintRequestFilter;
import mgm.security.handler.CustomAccessDeniedHandler;
import mgm.security.provider.CustomAuthenticationProvider;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    PrintRequestFilter printRequestsFilter;

    @Autowired
    InputStreamCachingFilter cachingFilter;

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable()
                .authenticationProvider(customAuthenticationProvider)
                .addFilterBefore(cachingFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(printRequestsFilter, AuthorizationFilter.class)

                .authorizeHttpRequests((request) -> {
                    request.requestMatchers("/", "/index", "/form").permitAll();
                    request.requestMatchers("/login", "/invalid").permitAll();
                    request.requestMatchers("/css/**", "/js/**", "/lib/**", "/image/**").permitAll();
                    request.requestMatchers("/entity/Contact").permitAll();
                    request.requestMatchers("/admin", "/admin/view-next", "/admin/delete").hasRole("ADMIN");
                    request.requestMatchers("/auth/**").authenticated();
                })
                .formLogin(configure -> {
                    configure.loginPage("/login");
                    configure.loginProcessingUrl("/login");
                    configure.successForwardUrl("/admin");
                    configure.failureForwardUrl("/invalid");
                })
                .logout().logoutSuccessUrl("/index").and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and().build();
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        var auth = success.getAuthentication();
        LoggerFactory.getLogger("Security").info("LOGIN SUCCESSFUL [{}] - {}", auth.getClass().getSimpleName(),
                auth.getName());
    }
}
