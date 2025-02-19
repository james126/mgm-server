package mgm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Error logging from client side
 */
@Controller
public class LoggingController {
    private final Logger angularLogger = LoggerFactory.getLogger("Angular");
    private final Logger springLogger = LoggerFactory.getLogger("Spring");

    @RequestMapping(value = "/client-logging", method = RequestMethod.POST)
    public ResponseEntity<String> unauthenticatedLogging(HttpServletRequest request) {
        logMessage(request);
            return ResponseEntity.ok().body(null);
    }

    public void logMessage(HttpServletRequest request){
        try {
            Map<String, Object> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            angularLogger.error("Angular Logger message {}", new JSONObject(requestMap));
        } catch (Exception e) {
            springLogger.error("Error logging angular message {}" , e.toString());
        }
    }

    public void logException(HttpServletRequest request, Exception exception){
        try {
            Map<String, Object> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            angularLogger.error("Angular Logger message {}", new JSONObject(requestMap));
            springLogger.error("Error logging angular message {}" , exception.toString());
        } catch (Exception e) {
            springLogger.error("Error logging exception {}" , e.toString());
        }
    }
}
