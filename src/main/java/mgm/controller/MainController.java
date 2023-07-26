package mgm.controller;

import mgm.controller.mock.ContactBuilder;
import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MainController {
    private final ContactServiceImpl contactService;

    @Autowired
    public MainController(ContactServiceImpl enquiryService, ContactBuilder builder) {
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

    //    @ResponseBody
    //    @GetMapping("/account")
    //    public String account(Authentication authentication) {
    //        authentication.getPrincipal(); //user identity (name, email) - returns an object (Google login has lots of details)
    //        authentication.getAuthorities(); //permissions (roles)
    //        authentication.getName(); //password
    //        authentication.getDetails(); //ip
    //        return "account " + authentication.getName();
    //    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String contactForm(@ModelAttribute Contact form, Model model) {
        model.addAttribute("contact", new Contact());
        contactService.insertContact(form);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin")
    public String adminGet(Model model) {
        Optional<Contact> result = contactService.findByMinId();
        if (result.isPresent()) {
            model.addAttribute("contact", result);
            model.addAttribute("id", result.get().getId());
        }

        return "admin";
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public String viewContact(Model model, @RequestParam(name = "id", defaultValue = "0") int id) {
        Optional<Contact> result = contactService.findById(id + 1);
        if (result.isPresent()) {
           model.addAttribute("contact", result);
           model.addAttribute("id", result.get().getId());
        }

        if (result.isEmpty()){
            result = contactService.findByMinId();
            if (result.isPresent()) {
                model.addAttribute("contact", result);
                model.addAttribute("id", result.get().getId());
            }
        }

        return "admin";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteContact(Model model, @RequestParam(name = "id") int id) {
        Optional<Contact> result = contactService.findById(id + 1);
        if (result.isPresent()) {
            model.addAttribute("contact", result);
            model.addAttribute("id", result.get().getId());
        }

        if (result.isEmpty()){
            result = contactService.findByMinId();
            if (result.isPresent()) {
                model.addAttribute("contact", result);
                model.addAttribute("id", result.get().getId());
            }
        }

        return "admin";
    }

    @GetMapping("/account")
    public String account() {
        return "account";
    }
}
