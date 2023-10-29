package mgm.controller;

import mgm.controller.utility.ConfigProperties;
import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import mgm.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    private final ConfigProperties configProperties;

    @Autowired
    public AdminController(ContactServiceImpl contactService, JwtUtility jwtUtility, ConfigProperties configProperties) {
        this.contactService = contactService;
        this.jwtUtility = jwtUtility;
        this.configProperties = configProperties;
    }

    @RequestMapping(value = "/admin/view-next", method = RequestMethod.POST)
    public ResponseEntity<Contact> viewNextContactForm(@RequestBody Integer id, Authentication authentication) {
        String token = jwtUtility.generateToken(authentication.getName());
        ResponseCookie jwt = jwtUtility.generateCookie(token, configProperties.getViewNextRequestUrl());
        Optional<Contact> result = contactService.getNextContactForm(id);

        return ResponseEntity.status(200).
                header(HttpHeaders.SET_COOKIE, jwt.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.orElse(null));
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST)
    public ResponseEntity<Contact> deleteContactForm(@RequestBody Integer id, Authentication authentication) {
        String token = jwtUtility.generateToken(authentication.getName());
        ResponseCookie jwt = jwtUtility.generateCookie(token, configProperties.getDeleteRequestUrl());
        contactService.deleteById(id);
        Optional<Contact> result = contactService.getNextContactForm(id);

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, jwt.toString())
                .body(result.orElse(null));
    }
}
