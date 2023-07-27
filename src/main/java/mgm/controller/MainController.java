package mgm.controller;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    private final ContactServiceImpl contactService;

    @Autowired
    public MainController(ContactServiceImpl enquiryService) {
        this.contactService = enquiryService;
    }

    @GetMapping("/")
    public String startup(Model model) {
        model.addAttribute("contact", new Contact());
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("contact", new Contact());
        return "index";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitContactForm(@ModelAttribute Contact form, Model model) {
        model.addAttribute("contact", new Contact());
        contactService.insertContact(form);
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("validate", "form-control border-0");
        return "login";
    }

    @PostMapping("/invalid")
    public String invalidLogin(Model model) {
        model.addAttribute("validate", "form-control border-0 is-invalid");
        return "login";
    }
}
