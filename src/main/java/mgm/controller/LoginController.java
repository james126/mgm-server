package mgm.controller;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import mgm.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

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
}
