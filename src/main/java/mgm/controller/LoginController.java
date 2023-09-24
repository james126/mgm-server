package mgm.controller;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import mgm.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller
public class LoginController {
    private final ContactServiceImpl contactService;
    private final JwtUtility jwtUtility;

    @Autowired
    public LoginController(ContactServiceImpl contactService, JwtUtility jwtUtility) {
        this.contactService = contactService;
        this.jwtUtility = jwtUtility;
    }

    @RequestMapping(value = "/login", method  = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<Contact> login(Authentication authentication) {
        String username = authentication.getName();
        String token = jwtUtility.generateToken(username);
        ResponseCookie cookie = jwtUtility.generateCookie(token);
        Optional<Contact> result = contactService.findByMinId();

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, cookie.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.orElse(null));
    }

    @RequestMapping(value = "/admin/logout", method  = { RequestMethod.GET })
    public ResponseEntity logout() {
        String token = jwtUtility.generateToken(generateRandomString());
        ResponseCookie cookie = jwtUtility.generateCookie(token);

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, cookie.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(null);
    }

    public String generateRandomString(){
        byte[] array = new byte[20]; // length is bounded by 20
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
