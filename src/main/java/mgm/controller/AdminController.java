package mgm.controller;

import jakarta.servlet.http.HttpServletResponse;
import mgm.controller.utility.ContactParser;
import mgm.model.dto.FormId;
import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import mgm.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
public class AdminController {
    private final ContactServiceImpl contactService;
    private final JwtUtility jwtUtility;
    private final ContactParser contactParser;

    @Autowired
    public AdminController(ContactServiceImpl contactService, JwtUtility jwtUtility, ContactParser contactParser) {
        this.contactService = contactService;
        this.jwtUtility = jwtUtility;
        this.contactParser = contactParser;
    }

    @RequestMapping(value = "/admin", method = { RequestMethod.GET, RequestMethod.POST })
    public String adminPage(Model model, Authentication authentication, HttpServletResponse response) {
        String username = authentication.getName();
        String token = jwtUtility.generateToken(username);
        ResponseCookie cookie = jwtUtility.generateCookie(token);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        Optional<Contact> result = contactService.findByMinId();
        addToModel(model, result);

        return "admin";
    }

    @RequestMapping(value = "/admin/view-next", method = RequestMethod.POST)
    public ResponseEntity<String> viewNextContactForm(@RequestBody FormId formId, Authentication authentication) {
        Optional<Contact> result = contactService.getNextContactForm(formId.getId());
        String token = jwtUtility.generateToken(authentication.getName());
        ResponseCookie jwt = jwtUtility.generateCookie(token);

        Contact contact = result.orElseGet(Contact::new);
        String contactString = contactParser.toJsonString(contact);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwt.toString()).body(contactString);
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContactForm(@RequestBody FormId formId, Authentication authentication) {
        contactService.deleteById(formId.getId());
        Optional<Contact> result = contactService.getNextContactForm(formId.getId());
        String token = jwtUtility.generateToken(authentication.getName());

        ResponseCookie jwt = jwtUtility.generateCookie(token);
        Contact contact = result.orElseGet(Contact::new);
        String contactString = contactParser.toJsonString(contact);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwt.toString()).body(contactString);
    }

    private void addToModel(Model model, Optional<Contact> result) {
        if (result.isPresent()) {
            model.addAttribute("contact", result);
            model.addAttribute("id", result.get().getId());
        } else {
            model.addAttribute("contact", new Contact());
            model.addAttribute("id", Integer.valueOf(0));
        }
    }
}
