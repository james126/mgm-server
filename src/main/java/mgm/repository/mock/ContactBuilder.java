package mgm.repository.mock;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import mgm.utility.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ContactBuilder {

    private final ContactServiceImpl contactService;

    private final DateTime dateTime;

    public List<Contact> getContactList(){
        return contactList;
    }

    private final List<Contact> contactList = new ArrayList<>();

    @Autowired
    public ContactBuilder(ContactServiceImpl contactService, DateTime zone) throws IOException {
        this.contactService = contactService;
        dateTime = zone;

        InputStream inputStream = getClass().getResourceAsStream("/mock_db_records.csv");
        assert inputStream != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            String line = null;
            reader.readLine(); //skip the first line

            do{
                line = reader.readLine();
                if (line != null){
                    String[] fields = line.split(",");
                    if (fields.length > 1){
                        Contact contact = mapContact(fields);
                        contactList.add(contact);
                    }
                }
            } while (line != null);
        }

        addMockDataToDatabase();
    }

    public void addMockDataToDatabase(){
        getContactList().forEach(contact -> {
            contact.setId(null);
            contactService.insertContact(contact);
        });
    }

    public Contact mapContact(String[] fields){
        Contact c = new Contact();
        c.setId(Integer.valueOf(fields[0]));
        c.setFirst_name(fields[1]);
        c.setLast_name(fields[2]);
        c.setEmail(fields[3]);
        c.setPhone(fields[4]);
        c.setAddress_line1(fields[5]);
        c.setAddress_line2(fields[6]);
        c.setMessage(fields[7]);
        c.setUpdate_datetime(dateTime.getDate());
        return c;
    }
}
