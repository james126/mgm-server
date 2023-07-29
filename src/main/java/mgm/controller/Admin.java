package mgm.controller;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class Admin {
    private final ContactServiceImpl contactService;

    @Autowired
    public Admin(ContactServiceImpl contactService) {
        this.contactService = contactService;
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
        contactService.deleteById(id);
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
