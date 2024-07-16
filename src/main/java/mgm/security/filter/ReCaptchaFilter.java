package mgm.security.filter;

import com.google.cloud.recaptchaenterprise.v1.RecaptchaEnterpriseServiceClient;
import com.google.recaptchaenterprise.v1.Assessment;
import com.google.recaptchaenterprise.v1.CreateAssessmentRequest;
import com.google.recaptchaenterprise.v1.Event;
import com.google.recaptchaenterprise.v1.ProjectName;
import com.google.recaptchaenterprise.v1.RiskAnalysis.ClassificationReason;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@ConfigurationProperties("recaptcha")
public class ReCaptchaFilter extends OncePerRequestFilter {
    @Value("${recaptcha.path}")
    private String recaptchaPath;
    @Value("${recaptcha.projectID}")
    private String projectID;
    @Value("${recaptcha.key.contactForm}")
    private String contactFormKey;
    @Value("${recaptcha.key.login}")
    private String loginKey;
    String token = "";
    Logger logger = LoggerFactory.getLogger("Security");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Create the reCAPTCHA client.
        RecaptchaEnterpriseServiceClient client = null;
        try  {
            client = RecaptchaEnterpriseServiceClient.create();

            token = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);

            // Set the properties of the event to be tracked.
            Event event = Event.newBuilder().setSiteKey(contactFormKey).setToken(token)
            .build();

            // Build the assessment request.
            CreateAssessmentRequest createAssessmentRequest = CreateAssessmentRequest.newBuilder()
                            .setParent(ProjectName.of(projectID).toString())
                            .setAssessment(Assessment.newBuilder().setEvent(event).build())
                            .build();

            Assessment result = client.createAssessment(createAssessmentRequest);

            // Check if the token is valid.
            if (!result.getTokenProperties().getValid()) {
                logger.error("The CreateAssessment call failed because the token was: {}", result.getTokenProperties().getInvalidReason().name());
                client.close();
                return;
            }

            // Get the risk score and the reason(s).
            for (ClassificationReason reason : result.getRiskAnalysis().getReasonsList()) {
                logger.trace(String.valueOf(reason));
            }

            float score = result.getRiskAnalysis().getScore();
            request.setAttribute("score", score);
            client.close();
            logger.trace("The reCAPTCHA score is: " + result.getRiskAnalysis().getScore());

        } catch (IOException e) {
            if (client != null ) client.close();
            logger.error(String.valueOf(e));
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return  CorsUtils.isPreFlightRequest(request) || !request.getRequestURI().endsWith(recaptchaPath);
    }
}
