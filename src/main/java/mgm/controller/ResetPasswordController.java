package mgm.controller;

import jakarta.servlet.http.HttpServletRequest;
import mgm.service.ResetPasswordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ResetPasswordController {
    private final ResetPasswordImpl passwordService;

    @Autowired
    public ResetPasswordController(ResetPasswordImpl passwordService) {
        this.passwordService = passwordService;
    }

    @RequestMapping(value = "/forgot-pass", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> submitContactForm(HttpServletRequest request, @RequestBody String email) {
        Map<String, Object> response = new HashMap<>();

        try{
            boolean exists = passwordService.forgotPassword(email);
            response.put("outcome", exists);
            response.put("error", null);
        } catch (Exception e){
            if (!response.containsKey("outcome")) {
                response.put("outcome", false);
            }
            response.put("error", e.toString());
        }

        return ResponseEntity.ok(response);
    }
}
