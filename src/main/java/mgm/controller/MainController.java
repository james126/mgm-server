package mgm.controller;

import jakarta.servlet.http.HttpServletRequest;
import mgm.model.dto.Enquiry;
import mgm.model.entity.Contact;
import mgm.service.EnquiryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class MainController {
    private final EnquiryServiceImpl enquiryService;

    @Autowired
    public MainController(EnquiryServiceImpl enquiryService) {
        this.enquiryService = enquiryService;
    }

    @GetMapping("/index")
    public String index(final HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @GetMapping("/account")
    public String account(Authentication authentication) {
        authentication.getPrincipal(); //user identity (name, email) - returns an object (Google login has lots of details)
        authentication.getAuthorities(); //permissions (roles)
        authentication.getCredentials(); //password
        authentication.getDetails(); //ip
        return "account " + authentication.getName();
    }

    @GetMapping("/admin")
    public String admin() {
        //SecurityContextHolder - only available in the Thread processing the request
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return "admin";
    }

    @ResponseBody
    @PostMapping("/form")
    public ResponseEntity<?> form(@RequestBody Enquiry form) {
        Optional<Contact> result = enquiryService.insertEnquiry(form);
        return result.isPresent() ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
