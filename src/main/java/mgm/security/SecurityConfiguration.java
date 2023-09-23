package mgm.security;

import mgm.security.filter.*;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
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
    CustomCorsFilter customCorsFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable()
                .addFilterBefore(cachingFilter, DisableEncodeUrlFilter.class)
                .addFilterAfter(printRequestsFilter, DisableEncodeUrlFilter.class)
                .addFilterBefore(customCorsFilter, WebAsyncManagerIntegrationFilter.class)
                .addFilterAfter(new CustomUsernamePasswordFilter(customAuthenticationManager), SecurityContextHolderFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, HeaderWriterFilter.class)
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests((request) -> {
                    request.requestMatchers("/", "/index", "/form", "invalid").permitAll();
                    request.requestMatchers("/css/**", "/js/**", "/lib/**", "/image/**").permitAll();
                    request.requestMatchers("/entity/Contact").permitAll();
                    request.requestMatchers("admin/view-next", "/admin/delete").hasRole("ADMIN");
                    request.requestMatchers("/login").authenticated();
                })
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
