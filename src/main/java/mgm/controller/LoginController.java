package mgm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("validate", "form-control border-0");
        return "login";
    }

    @RequestMapping(value = "/invalid", method = { RequestMethod.GET, RequestMethod.POST })
    public String invalidLoginAttempt(Model model) {
        model.addAttribute("validate", "form-control border-0 is-invalid");
        return "login";
    }
}
