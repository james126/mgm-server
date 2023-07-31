package mgm.utilities;

import mgm.model.entity.Contact;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ContactParser {
    public String toJSON(Contact contact) throws JSONException {
        Map<String, String> map = new HashMap<>();
        //map.put("id", String.valueOf(contact.getId()));
        map.put("first_name", contact.getFirst_name());
        map.put("last_name", contact.getLast_name());
        map.put("email", contact.getEmail());
        map.put("phone", contact.getPhone());
        map.put("address_line1", contact.getAddress_line1());
        map.put("address_line2", contact.getAddress_line2());
        map.put("message", contact.getMessage());
        return new JSONObject(map).toString();
    }
}
