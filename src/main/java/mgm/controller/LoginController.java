package mgm.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method  = { RequestMethod.POST })
    public ResponseEntity<JSONObject> getLogin() {
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/invalid", method = { RequestMethod.POST })
    public ResponseEntity<JSONObject> invalidLoginAttempt() {
        return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }
}
