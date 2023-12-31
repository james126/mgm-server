package mgm.utilities;

import mgm.model.entity.Contact;
import mgm.utility.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TestConfiguration
@EnableConfigurationProperties(DateTime.class)
public class ContactBuilder {

    @Autowired
    private final DateTime dateTime;

    @Bean
    List<Contact> getContactList(){
        return contactList;
    }

    private final List<Contact> contactList = new ArrayList<>();

    public ContactBuilder(DateTime zone) throws IOException {
        dateTime = zone;

        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/mock_db_records.csv"))){
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
