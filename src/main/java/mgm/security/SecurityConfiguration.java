package mgm.security;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
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

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var configurer = new LoginConfigurer();

            return http. authorizeHttpRequests((request) -> {
                        request.requestMatchers("/").permitAll();
                        request.requestMatchers("/index").permitAll();
                        request.requestMatchers("/login").permitAll();
                        request.requestMatchers("/css/**").permitAll();
                        request.requestMatchers("/js/**").permitAll();
                        request.requestMatchers("/lib/**").permitAll();
                        request.requestMatchers("/image/**").permitAll();
                        request.requestMatchers("/entity/**").permitAll();
                        request.requestMatchers("/admin").hasRole("ADMIN");
                        request.requestMatchers("/account").hasRole("USER");
                        request.requestMatchers("/form").permitAll();
                    })

//                    .authenticationProvider(new LoginAuthenticationProvider(new ArrayList<>()))
                    .formLogin(configure -> {
                                configure.loginPage("/login");
                                configure.loginProcessingUrl("/login");
                        configure.successHandler(new MySimpleUrlAuthenticationSuccessHandler());
                            }).logout().logoutSuccessUrl("/index").and().build();

    }

    @Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
        return http.build();
    }

    @Bean
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
    public UserDetailsService userDetailsService(DataSource dataSource) {
        UserDetails user = User.builder()
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
        users.createUser(user);
        users.createUser(user2);
        return users;
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
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successListener() {
        return event -> {
            var auth = event.getAuthentication();
            LoggerFactory.getLogger("SecurityConfiguration").info("LOGIN SUCCESSFUL [{}] - {}", auth.getClass().getSimpleName(),
                    auth.getName());
        };
    }
}
