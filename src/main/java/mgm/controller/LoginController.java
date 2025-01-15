package mgm.controller;

import mgm.controller.utility.ConfigProperties;
import mgm.model.dto.Result;
import mgm.service.LoginService;
import mgm.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@Controller
public class LoginController {
    private final JwtUtility jwtUtility;
    private final ConfigProperties configProperties;
    private final LoginService loginService;

    @Autowired
    public LoginController(JwtUtility jwtUtility, ConfigProperties configProperties, LoginService loginService) {
        this.jwtUtility = jwtUtility;
        this.configProperties = configProperties;
        this.loginService = loginService;
    }

    @RequestMapping(value = "/login", method  = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<Result> login(Authentication authentication) {
        String username = authentication.getName();
        boolean isTemporaryPassword = loginService.isTemporaryPassword(username);

        if (isTemporaryPassword){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Result(true,"",true));
        } else {
            String token = jwtUtility.generateToken(username);
            ResponseCookie cookie = jwtUtility.generateCookie(token, configProperties.getRequestUrl());
            return ResponseEntity.ok().
                    header( HttpHeaders.SET_COOKIE, cookie.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Result(true,"",false));
        }
    }

    @RequestMapping(value = "/error", method  = { RequestMethod.POST })
    public ResponseEntity<String> error() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(null);
    }

    /*
     * Use endpoint /custom-logout as /logout triggers default logout filter
     */
    @RequestMapping(value = "/custom-logout", method  = { RequestMethod.GET })
    public ResponseEntity<Result> logout() {
        String token = jwtUtility.generateToken(generateRandomString());
        ResponseCookie cookie = jwtUtility.generateCookie(token, configProperties.getRequestUrl());

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new Result(true,"", false));
    }

    public String generateRandomString(){
        byte[] array = new byte[20];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
