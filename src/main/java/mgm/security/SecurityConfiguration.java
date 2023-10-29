package mgm.security;

import mgm.security.filter.*;
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
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

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

    @Autowired
    CustomAuthenticationManager customAuthenticationManager;

    @Autowired
    CustomUsernamePasswordFilter customUsernamePasswordFilter;

    @Autowired
    CustomHeaderFilter customHeaderFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable()
                .addFilterBefore(cachingFilter, DisableEncodeUrlFilter.class)
                .addFilterAfter(printRequestsFilter, DisableEncodeUrlFilter.class)
                .addFilterBefore(customHeaderFilter, WebAsyncManagerIntegrationFilter.class)
                .addFilterAfter(new CustomUsernamePasswordFilter(customAuthenticationManager), SecurityContextHolderFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, HeaderWriterFilter.class)
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests((request) -> {
                    request.requestMatchers("/api/form", "error").permitAll();
                    request.requestMatchers("/entity/Contact", "/client-logging").permitAll();
                    request.requestMatchers("/api/login").authenticated();
                    request.requestMatchers("/admin/view-next", "/admin/delete", "/admin/logout").hasRole("ADMIN");
                    request.requestMatchers("/actuator/**").permitAll();
                }).build();
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
