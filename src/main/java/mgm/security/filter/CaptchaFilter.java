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
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CaptchaFilter extends OncePerRequestFilter {
    String projectID = "mr-grass-master";
    String recaptchaSiteKey = "6LcEd_soAAAAADd0IusI1vMlLeLdwXQp7XH8W-80";
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
            Event event = Event.newBuilder().setSiteKey(recaptchaSiteKey ).setToken(token).build();

            // Build the assessment request.
            CreateAssessmentRequest createAssessmentRequest = CreateAssessmentRequest.newBuilder()
                            .setParent(ProjectName.of(projectID).toString())
                            .setAssessment(Assessment.newBuilder().setEvent(event).build())
                            .build();

            Assessment result = client.createAssessment(createAssessmentRequest);

            // Check if the token is valid.
            if (!result.getTokenProperties().getValid()) {
                logger.error("The CreateAssessment call failed because the token was: {}", result.getTokenProperties().getInvalidReason().name());
                return;
            }

            // Get the risk score and the reason(s).
            for (ClassificationReason reason : result.getRiskAnalysis().getReasonsList()) {
                logger.trace(String.valueOf(reason));
            }

            float score = result.getRiskAnalysis().getScore();
            client.close();
            logger.trace("The reCAPTCHA score is: " + result.getRiskAnalysis().getScore());

            if (score < 0.7){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getOutputStream().flush();
                return;
            }
        } catch (IOException e) {
            if (client != null ) client.close();
            logger.error(String.valueOf(e));
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return  CorsUtils.isPreFlightRequest(request) || !request.getRequestURI().endsWith("/api/recaptcha");
    }
}
