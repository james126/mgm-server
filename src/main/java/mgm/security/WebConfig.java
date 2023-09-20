package mgm.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties("client.domain")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${client.domain}")
    private String clientDomain;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(clientDomain)
                .allowedMethods("POST")
                .allowCredentials(true)
                .allowedHeaders("content-type");
    }
}
