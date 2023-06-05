package main.controller;

import main.entity.Form;
import main.service.FormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class MainController {
    private final Logger logger;
    private final FormService formService;

    @Autowired
    public MainController(FormService formService) {
        this.formService = formService;
        logger = LoggerFactory.getLogger("console");
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String form() {
        return "contact";
    }

    @PostMapping("/form")
    public String form(Form form, Model model) {
        logger.info("Received - firstName: {}, lastName: {}", form.getFirstName(), form.getLastName());
        Optional<Form> result = formService.insertForm(form);

        if (result.isPresent()){
            model.addAttribute(result.get());
            return "confirm";
        }

        return "form";
    }
}
