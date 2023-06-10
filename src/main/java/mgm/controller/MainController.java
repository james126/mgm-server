package mgm.controller;

import mgm.model.dto.Enquiry;
import mgm.model.entity.Contact;
import mgm.service.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class MainController {
    private final EnquiryService enquiryService;

    @Autowired
    public MainController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
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
    public String contact() {
        return "contact";
    }

    @ResponseBody
    @PostMapping("/form")
    public ResponseEntity<?> form(@RequestBody Enquiry form) {
        Optional<Contact> result = enquiryService.insertEnquiry(form);
        return result.isPresent() ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
