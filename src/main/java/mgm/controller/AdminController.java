package mgm.controller;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import mgm.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
public class AdminController {
    private final ContactServiceImpl contactService;
    private final JwtUtility jwtUtility;

    @Autowired
    public AdminController(ContactServiceImpl contactService, JwtUtility jwtUtility) {
        this.contactService = contactService;
        this.jwtUtility = jwtUtility;
    }

    @RequestMapping(value = "/admin/view-next", method = RequestMethod.POST)
    public ResponseEntity<Contact> viewNextContactForm(@RequestBody Integer id, Authentication authentication) {
        String token = jwtUtility.generateToken(authentication.getName());
        ResponseCookie jwt = jwtUtility.generateCookie(token);
        Optional<Contact> result = contactService.getNextContactForm(id);

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, jwt.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.orElse(null));
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST)
    public ResponseEntity<Contact> deleteContactForm(@RequestBody Integer id, Authentication authentication) {
        String token = jwtUtility.generateToken(authentication.getName());
        ResponseCookie jwt = jwtUtility.generateCookie(token);
        contactService.deleteById(id);
        Optional<Contact> result = contactService.getNextContactForm(id);

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, jwt.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.orElse(null));
    }
}
