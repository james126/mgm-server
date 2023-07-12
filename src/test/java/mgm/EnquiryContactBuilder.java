package mgm;

import mgm.model.dto.Enquiry;
import mgm.model.entity.Contact;
import mgm.service.DateTime;
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
public class EnquiryContactBuilder{
    @Autowired
    private final DateTime dateTime;

    private final List<Enquiry> enquiryList = new ArrayList<>();

    private final List<Contact> contactList = new ArrayList<>();

    private int primaryKeyCounter = 1;

    public EnquiryContactBuilder(DateTime zone) throws IOException {
        dateTime = zone;

        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/enquiry_information.csv"))){
            String line = null;
            reader.readLine(); //skip the first line

            do{
                line = reader.readLine();
                if (line != null){
                    String[] enquiryString = line.split(",");
                    if (enquiryString.length > 1){
                        Enquiry enquiry = mapEnquiryString(enquiryString);
                        enquiryList.add(enquiry);
                        Contact contact = mapEnquiry(enquiry);
                        contactList.add(contact);
                    }
                }
            } while (line != null);
        }
    }

    public Enquiry mapEnquiryString(String[] enquiryString){
        Enquiry e = new Enquiry();
        e.setFirst_name(enquiryString[0]);
        e.setLast_name(enquiryString[1]);
        e.setEmail(enquiryString[2]);
        e.setPhone(enquiryString[3]);
        e.setAddress_line1(enquiryString[4]);
        e.setAddress_line2(enquiryString[5]);
        e.setMessage(enquiryString[6]);
        return e;
    }

    public Contact mapEnquiry(Enquiry form){
        Contact c = new Contact();
        c.setId(primaryKeyCounter++);
        c.setFirst_name(form.getFirst_name());
        c.setLast_name(form.getLast_name());
        c.setEmail(form.getEmail());
        c.setPhone(form.getPhone());
        c.setAddress_line1(form.getAddress_line1());
        c.setAddress_line2(form.getAddress_line2());
        c.setMessage(form.getMessage());
        c.setUpdate_datetime(dateTime.getDate());
        return c;
    }

    @Bean
    List<Contact> getContactList(){
        return contactList;
    }

    @Bean
    List<Enquiry> getEnquiryList(){
        return enquiryList;
    }
}
