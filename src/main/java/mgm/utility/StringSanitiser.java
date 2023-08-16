package mgm.utility;

import mgm.model.entity.Contact;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class StringSanitiser {

    public synchronized Contact sanitise(Contact contact){
        String clean = cleanString(contact.getFirst_name());
        contact.setFirst_name(clean);

        clean = cleanString(contact.getLast_name());
        contact.setLast_name(clean);

        clean = cleanString(contact.getEmail());
        contact.setEmail(clean);

        clean = cleanString(contact.getPhone());
        contact.setPhone(clean);

        clean = cleanString(contact.getAddress_line1());
        contact.setAddress_line1(clean);

        clean = cleanString(contact.getAddress_line2());
        contact.setAddress_line2(clean);

        clean = cleanString(contact.getMessage());
        contact.setMessage(clean);

        return contact;
    }

    public String cleanString(String dirty){
        return dirty!= null ? Jsoup.clean(dirty, Safelist.none()) : null;
    }
}
