package mgm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import mgm.model.dto.Result;
import mgm.model.entity.Users;
import mgm.service.SignupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class SignupController {
    private final SignupServiceImpl signupService;
    private final LoggingController logger;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public SignupController(SignupServiceImpl signupService, LoggingController logger) {
        this.signupService = signupService;
        this.logger = logger;
    }

    @RequestMapping(value="/signup", method = { RequestMethod.POST })
    public Object signup(HttpServletRequest request){
        Result result = new Result(true);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(request.getInputStream(), Map.class);
            String username = map.get("username");
            String email = map.get("email");
            String password = map.get("password");
            password = passwordEncoder.encode(password);

            Users user = new Users();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setEnabled(true);

            signupService.register(user);
        } catch (Exception e) {
            result.outcome = false;
            logger.logException(request, e);
            return ResponseEntity.status(500).body(null);
        }
        return  ResponseEntity.ok(result);
    }

    @RequestMapping(value="/username-taken", method = { RequestMethod.GET })
    public Object usernameTaken(HttpServletRequest request){
        Result result = new Result(true);
        try {
            String username = request.getParameter("username");
            result.outcome = signupService.usernameTaken(username);
        } catch (Exception e) {
            logger.logException(request, e);
            return ResponseEntity.status(500).body(null);
        }
        return  ResponseEntity.ok(result);
    }

    @RequestMapping(value="/email-taken", method = { RequestMethod.GET })
    public Object emailTaken(HttpServletRequest request){
        Result result = new Result(true);
        try {
            String email = request.getParameter("email");
            result.outcome = signupService.emailTaken(email);
        } catch (Exception e) {
            logger.logException(request, e);
            return ResponseEntity.status(500).body(null);
        }
        return  ResponseEntity.ok(result);
    }
}
