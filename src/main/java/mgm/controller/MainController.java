package mgm.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Key;
import java.util.Date;

@Controller
public class MainController {
    private final ContactServiceImpl contactService;

    @Autowired
    public MainController(ContactServiceImpl enquiryService) {
        this.contactService = enquiryService;
    }

    @GetMapping("/")
    public String startup(Model model, HttpServletResponse response) {
        model.addAttribute("contact", new Contact());

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + 604800000);
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb"));
        String token = Jwts.builder()
                .setSubject("user1")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
        Cookie cookie = new Cookie("Bearer", token);
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

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
