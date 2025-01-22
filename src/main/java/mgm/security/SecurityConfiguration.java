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

    @Autowired
    ReCaptchaFilter reCaptchaFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Filter order org.springframework.security.config.annotation.web.builders.FilterOrderRegistration

        return http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(cachingFilter, DisableEncodeUrlFilter.class)
                .addFilterAfter(printRequestsFilter, DisableEncodeUrlFilter.class)
                .addFilterBefore(customHeaderFilter, WebAsyncManagerIntegrationFilter.class)
                .addFilterAfter(new CustomUsernamePasswordFilter(customAuthenticationManager), SecurityContextHolderFilter.class)
                .addFilterBefore(reCaptchaFilter, HeaderWriterFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, HeaderWriterFilter.class)
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests((request) -> {
                    request.requestMatchers("/contact-form", "/recaptcha", "error").permitAll();
                    request.requestMatchers("/sign-up", "/username-taken", "/email-taken").permitAll();
                    request.requestMatchers("/forgot-pass", "/new-pass").permitAll();
                    request.requestMatchers("/client-logging", "/actuator/**").permitAll();
                    request.requestMatchers("/login").authenticated();
                    request.requestMatchers("/custom-logout").hasRole("ADMIN");
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
