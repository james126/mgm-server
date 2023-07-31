package mgm.controller;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class Admin {
    private final ContactServiceImpl contactService;

    @Autowired
    public Admin(ContactServiceImpl contactService) {
        this.contactService = contactService;
    }
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("validate", "form-control border-0");
        return "login";
    }

    @RequestMapping(value = "/invalid", method = { RequestMethod.GET, RequestMethod.POST })
    public String invalidLoginAttempt(Model model) {
        model.addAttribute("validate", "form-control border-0 is-invalid");
        return "login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        Optional<Contact> result = contactService.findByMinId();
        addToModel(model, result);
        return "admin";
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public String viewNextContactForm(Model model, @RequestParam(name = "id", defaultValue = "0") int id) {
        Optional<Contact> result = contactService.getNextContactForm(id);
        addToModel(model, result);
        return "admin";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteContactForm(Model model, @RequestParam(name = "id") int id) {
        try {
            contactService.deleteById(id);
        } catch (NullPointerException ignored){}
        Optional<Contact> result = contactService.getNextContactForm(id);
        addToModel(model, result);
        return "admin";
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
