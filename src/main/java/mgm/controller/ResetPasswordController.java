package mgm.controller;

import jakarta.servlet.http.HttpServletRequest;
import mgm.model.dto.LoginData;
import mgm.model.dto.Result;
import mgm.service.EmailService;
import mgm.service.ResetPasswordImpl;
import mgm.utility.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ResetPasswordController {
    private final ResetPasswordImpl passwordImpl;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;
    private final LoggingController logger;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public ResetPasswordController(ResetPasswordImpl passwordService, PasswordGenerator passwordGenerator,@Qualifier("customEmailService") EmailService emailService, LoggingController logger) {
        this.passwordImpl = passwordService;
        this.passwordGenerator = passwordGenerator;
        this.emailService = emailService;
        this.logger = logger;
    }

    @RequestMapping(value = "/new-pass", method = RequestMethod.POST)
    public ResponseEntity<Result> newPassword(HttpServletRequest request, @RequestBody LoginData data) {
        boolean outcome = false;
        try {
            this.passwordImpl.updatePassword(passwordEncoder.encode(data.password), data.username);
            outcome = this.passwordImpl.existsByUsername(data.username);
        } catch (Exception e) {
            logger.logException(request, e);
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Result(false,e.getMessage(), false));
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Result(outcome,"", false));
    }

    @RequestMapping(value = "/forgot-pass", method = RequestMethod.POST)
    public Object forgotPassword(HttpServletRequest request, @RequestBody String email) {

        try {
            boolean exists = passwordImpl.forgotPassword(email);
            if (exists) {
                temporaryPassword(email);
            }
        } catch (Exception e) {
            logger.logException(request, e);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Result(false,e.getMessage(), false));
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Result(true,"", true));
    }

    public void temporaryPassword(String email) {
        String password = passwordGenerator.generatePassword(12, true, true, true, true);
        passwordImpl.submitTempPassword(passwordEncoder.encode(password), email);
        emailService.sendEmail(email, "Mr Grass Master", password);
    }
}
