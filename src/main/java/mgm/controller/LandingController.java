package mgm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LandingController {
    private final ContactServiceImpl contactService;
    private final LoggingController logger;

    @Autowired
    public LandingController(ContactServiceImpl enquiryService, LoggingController logger) {
        this.contactService = enquiryService;
        this.logger = logger;
    }


    @RequestMapping(value = "/contact-form", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Boolean>> submitContactForm(HttpServletRequest request) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            Contact contact = parseRequest(request);
            contactService.insertContact(contact);
            response.put("success", Boolean.TRUE);

        } catch (IOException | NullPointerException ex) {
            logger.logException(request, ex);
            response.put("success", Boolean.FALSE);
        }

        return  ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/recaptcha", method = RequestMethod.POST)
    @ResponseBody
    public Object recaptcha(HttpServletRequest request) {
        float score = (float) request.getAttribute("score");
        Map<String, Float> map = new HashMap<>();
        map.put("score", score);
        return map;
    }

    public Contact parseRequest(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(request.getInputStream(), Map.class);
        Contact contact = new Contact();
        contact.setFirst_name(map.get("first_name"));
        contact.setLast_name(map.get("last_name"));
        contact.setEmail(map.get("email"));
        contact.setPhone(map.get("phone"));
        contact.setAddress_line1(map.get("address_line1"));
        contact.setAddress_line2(map.get("address_line2"));
        contact.setMessage(map.get("message"));
        return contact;
    }
}
