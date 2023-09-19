package mgm;

import mgm.security.WebConfig;
import mgm.utility.JwtUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtUtility.class, WebConfig.class})
public class Main{

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
