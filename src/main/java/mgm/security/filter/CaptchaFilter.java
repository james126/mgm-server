package mgm.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class CaptchaFilter extends OncePerRequestFilter {
    // TO-DO: Replace the token and reCAPTCHA action variables before running the sample.
    String projectID = "mr-grass-master";
    String recaptchaSiteKey = "6Ld7EvcoAAAAAHBucylwxA6hxMhWdcv0UfsOaUBH";
    String token = "";
    Logger logger = LoggerFactory.getLogger("Security");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Create the reCAPTCHA client.
        // TODO: Cache the client generation code (recommended) or call client.close() before exiting the method.
        try  {
            RecaptchaEnterpriseServiceClient client = RecaptchaEnterpriseServiceClient.create();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(request.getInputStream(), Map.class);
            token = map.getOrDefault("recaptcha", "");

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
            // For more information on interpreting the assessment, see:
            // https://cloud.google.com/recaptcha-enterprise/docs/interpret-assessment
            for (ClassificationReason reason : result.getRiskAnalysis().getReasonsList()) {
                logger.trace(String.valueOf(reason));
            }

            float score = result.getRiskAnalysis().getScore();
            client.close();
            logger.info("The reCAPTCHA score is: " + result.getRiskAnalysis().getScore());
            if (score < 0.7){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getOutputStream().flush();
                return;
            }
        } catch (IOException e) {
            logger.error(String.valueOf(e));
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return  CorsUtils.isPreFlightRequest(request) || request.getRequestURI().endsWith("/api/recaptcha");
    }
}
