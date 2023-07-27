package mgm.security;

import mgm.security.securityreferrence.CustomFilter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@ConfigurationProperties(prefix = "spring.datasource")
@ConfigurationPropertiesScan
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final boolean useEmbeddedDatabase = true;
//    @Value("${spring.datasource.url}")
//    private String url;
//    @Value("${spring.datasource.username}")
//    private String username;
//    @Value("${spring.datasource.password}")
//    private String password;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests((request) -> {
                    request.requestMatchers("/", "/index", "/form").permitAll();
                    request.requestMatchers("/login", "/invalid").permitAll();
                    request.requestMatchers("/css/**", "/js/**", "/lib/**", "/image/**").permitAll();
                    request.requestMatchers("/entity/**").permitAll();
                    request.requestMatchers("/admin", "/view", "delete").hasRole("ADMIN");
                }).formLogin(configure -> {
                    configure.loginPage("/login");
                    configure.loginProcessingUrl("/login");
                    configure.successHandler(new LoginSuccessHandler());
                    configure.failureForwardUrl("/invalid");
                }).logout().logoutSuccessUrl("/index")
                .and().exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and().addFilterBefore(new CustomFilter(http.getSharedObject(AuthenticationManager.class)), DisableEncodeUrlFilter.class)
                .build();
    }

    @Bean
    public DataSource appDataSource() {
//        if (useEmbeddedDatabase) {
            return new EmbeddedDatabaseBuilder()
                    .setType(H2)
                    .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                    .build();
//        } else {
//            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
//            dataSourceBuilder.url(url);
//            dataSourceBuilder.username(username);
//            dataSourceBuilder.password(password);
//            return dataSourceBuilder.build();
//        }
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        UserDetails user1 = User.builder()
                .username("user1")
                .password("$2a$10$wUtdYp0GXHF5xXdICpmgDuP5kdxCILDTE9X1MJoUAFjZWsco5LeEm")
                .disabled(false)
                .authorities("ROLE_ADMIN")
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password("$2a$10$wUtdYp0GXHF5xXdICpmgDuP5kdxCILDTE9X1MJoUAFjZWsco5LeEm")
                .disabled(false)
                .authorities("ROLE_USER")
                .build();

        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user1);
        users.createUser(user2);
        return users;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        if (useEmbeddedDatabase) {
            //Log for testing/development
            var auth = success.getAuthentication();
            LoggerFactory.getLogger("SecurityConfiguration").info("LOGIN SUCCESSFUL [{}] - {}", auth.getClass().getSimpleName(),
                    auth.getName());
        }
    }
}
