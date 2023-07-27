package mgm.controller;

import mgm.model.entity.Contact;
import mgm.service.ContactProcessorImpl;
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
    private final ContactProcessorImpl contactProcessor;

    @Autowired
    public Admin(ContactServiceImpl contactService, ContactProcessorImpl contactProcessor) {
        this.contactService = contactService;
        this.contactProcessor = contactProcessor;
    }

    @GetMapping("/admin")
    public String adminGet(Model model) {
        Optional<Contact> result = contactService.findByMinId();
        return getString(model, result);
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public String viewNextContactForm(Model model, @RequestParam(name = "id", defaultValue = "0") int id) {
        Optional<Contact> result = contactProcessor.getNextContactForm(id);
        return getString(model, result);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteContact(Model model, @RequestParam(name = "id") int id) {
        contactService.deleteById(id);
        Optional<Contact> result = contactProcessor.getNextContactForm(id);
        return getString(model, result);
    }

    private String getString(Model model, Optional<Contact> result) {
        if (result.isPresent()) {
            model.addAttribute("contact", result);
            model.addAttribute("id", result.get().getId());
        } else {
            model.addAttribute("contact", new Contact());
            model.addAttribute("id", Integer.valueOf(0));
        }

        return "admin";
    }
}
