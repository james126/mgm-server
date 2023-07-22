package mgm.security;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

//1:40
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successListener(){
        return event -> {
            var auth = event.getAuthentication();
            LoggerFactory.getLogger("SecurityConfiguration").info("LOGIN SUCCESSFUL [{}] - {}", auth.getClass().getSimpleName(),
                    auth.getName());
        };
    }

    //entry point for spring security
    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests((request) -> {
                    request.requestMatchers("/").permitAll();
                    request.requestMatchers("/index").permitAll();
                    request.requestMatchers("/login").permitAll();
                    request.requestMatchers("/admin").permitAll();
                    request.requestMatchers("/account1").permitAll();
                    request.requestMatchers("/css/**").permitAll();
                    request.requestMatchers("/js/**").permitAll();
                    request.requestMatchers("/lib/**").permitAll();
                    request.requestMatchers("/image/**").permitAll();
                    request.anyRequest().authenticated();
                })
                .formLogin(form -> form.loginPage("/login").permitAll())
                .formLogin(congfigurer -> {
                    congfigurer.loginPage("/custom-login-page")
                            .passwordParameter("pw")
                            .usernameParameter("user")
                            .authenticationDetailsSource(new WebAuthenticationDetailsSource());
                })
//                .formLogin(congfigurer -> {})
//                    .apply(new RobotLoginConfigurer())
//                    .password("beep-boop")
//                    .password("boop-beep")
//                    .and()
//                .authenticationProvider(new DanielAuthenticationProvider()) //for custom authentications, use a provider not filter
                .build();

        //https://github.com/spring-projects/spring-security/blob/6.1.2/config/src/main/java/org/springframework/security/config/annotation/web/builders/FilterOrderRegistration.java
        //shows Spring filter order
    }

    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
        return http.build();
    }


    @Bean("security")
    public DataSource appDataSource() {
        //        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        //        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/postgres");
        //        dataSourceBuilder.username("postgres");
        //        dataSourceBuilder.password("password");
        //        return dataSourceBuilder.build();

        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsService = new InMemoryUserDetailsManager();

        UserDetails user = User.builder()
                .username("user1")
                .password("password")
                .disabled(false)
                .authorities("USER")
                .build();

        userDetailsService.createUser(user);

        return userDetailsService;
    }


//    @Bean
//    UserDetailsManager users(DataSource dataSource) {
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//                .disabled(false)
//                .authorities("USER", "ADMIN")
//                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        users.createUser(admin);
//        return users;
//    }

    @SuppressWarnings("deprecation")
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
