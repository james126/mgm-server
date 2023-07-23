package mgm.controller;

import jakarta.servlet.http.HttpServletRequest;
import mgm.model.entity.Contact;
import mgm.service.EnquiryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
    private final EnquiryServiceImpl enquiryService;

    @Autowired
    public MainController(EnquiryServiceImpl enquiryService) {
        this.enquiryService = enquiryService;
    }

    @GetMapping("/")
    public String startup(Model model) {
        model.addAttribute("contact", new Contact());
        return "index";
    }

    @GetMapping("/index")
    public String index(final HttpServletRequest request, Model model) {
        model.addAttribute("contact", new Contact());
        System.out.println(request.getRequestURI());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/account")
    public String account(Authentication authentication) {
        authentication.getPrincipal(); //user identity (name, email) - returns an object (Google login has lots of details)
        authentication.getAuthorities(); //permissions (roles)
        authentication.getCredentials(); //password
        authentication.getDetails(); //ip
        return "account";
    }

    @GetMapping("/admin")
    public String admin(Authentication authentication) {
        //SecurityContextHolder - only available in the Thread processing the request
        //var authentication = SecurityContextHolder.getContext().getAuthentication();
        return "admin";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String form(@ModelAttribute Contact contact, Model model) {
        model.addAttribute("contact", new Contact());
        //Optional<Contact> result = enquiryService.insertEnquiry(form);
        //return result.isPresent() ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        System.out.println(contact);
        return "index";
    }
}
