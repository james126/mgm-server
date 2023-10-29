package mgm.controller.utility;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("client")
public class ConfigProperties {
    @Value("client.login-request-url")
    private String loginRequestUrl;

    @Value("client.view-next-request-url")
    private String viewNextRequestUrl;

    @Value("client.delete-request-url")
    private String deleteRequestUrl;

    @Value("client.logout-request-url")
    private String logoutRequestUrl;
}
