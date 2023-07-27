package mgm.service.sanitise;

import mgm.model.entity.Contact;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class StringSanitiser {

    public synchronized Contact sanitise(Contact contact){
        String clean = removeHtml(contact.getFirst_name());
        contact.setFirst_name(clean);

        clean = removeHtml(contact.getLast_name());
        contact.setLast_name(clean);

        clean = removeHtml(contact.getEmail());
        contact.setEmail(clean);

        clean = removeHtml(contact.getPhone());
        contact.setPhone(clean);

        clean = removeHtml(contact.getAddress_line1());
        contact.setAddress_line1(clean);

        clean = removeHtml(contact.getAddress_line2());
        contact.setAddress_line2(clean);

        clean = removeHtml(contact.getMessage());
        contact.setMessage(clean);

        return contact;
    }

    public String removeHtml(String dirty){
        return Jsoup.clean(dirty, Safelist.none());
    }
}
