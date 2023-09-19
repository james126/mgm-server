package mgm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import jakarta.servlet.http.HttpServletRequest;
import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
public class IndexController {
    private final ContactServiceImpl contactService;

    @Autowired
    public IndexController(ContactServiceImpl enquiryService) {
        this.contactService = enquiryService;
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ResponseEntity<String> submitContactForm(HttpServletRequest request) {
        HttpStatusCode code = HttpStatusCode.valueOf(200);

            try {
                Contact contact = parseRequest(request);
                contactService.insertContact(contact);
            } catch (IOException | NullPointerException ex) {
                code = HttpStatusCode.valueOf(500);
            }

        return new ResponseEntity<>(code);
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
