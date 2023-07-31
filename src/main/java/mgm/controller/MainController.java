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
    public String submitContactForm(Contact contact, Model model) {
        model.addAttribute("contact", new Contact());
        try {
            contactService.insertContact(contact);
        } catch (NullPointerException ignored){}
        return "index";
    }
}
