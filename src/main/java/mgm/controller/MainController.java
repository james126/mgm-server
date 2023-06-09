package mgm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
//    private final EnquiryService enquiryService;
//
//    @Autowired
//    public MainController(EnquiryService enquiryService) {
//        this.enquiryService = enquiryService;
//    }

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

    @GetMapping("/form")
    public String form() {
        return "form";
    }

//    @ResponseBody
//    @PostMapping("/form")
//    public ResponseEntity<?> form(@RequestBody Enquiry form) {
//        Optional<Contact> result = enquiryService.insertEnquiry(form);
//        return result.isPresent() ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
