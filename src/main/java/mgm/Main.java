package mgm;


import mgm.security.filter.CustomHeaderFilter;
import mgm.security.filter.JwtAuthenticationFilter;
import mgm.service.EmailService;
import mgm.utility.JwtUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties({JwtUtility.class, CustomHeaderFilter.class, JwtAuthenticationFilter.class, EmailService.class})
public class Main extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
