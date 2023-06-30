package mgm;

import mgm.model.dto.Enquiry;
import mgm.model.entity.Contact;
import mgm.service.EnquiryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(EnquiryContactBuilder.class)
@ActiveProfiles(value = "test")
public class DatabaseApplicationTest {
    @Autowired
    private EnquiryContactBuilder contactBuilder;

    @Autowired
    private EnquiryServiceImpl enquiryService;

    private final List<Contact> expectedContactList = new ArrayList<>();
    private final List<Enquiry> enquiryList = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        expectedContactList.addAll(contactBuilder.getContactList());
        enquiryList.addAll(contactBuilder.getEnquiryList());
        enquiryList.forEach(enquiry -> {
            enquiryService.insertEnquiry(enquiry);
        });
    }

    @AfterEach
    public void tearDown(){
        expectedContactList.forEach(contact -> {
            enquiryService.deleteById(contact.getId());
        });
    }

    @Test
    public void testGetAllEnquiries() {
        List<Contact> contactList = enquiryService.getAllEnquiries();
        assertNotNull(contactList);
        assertEquals(contactList.size(), expectedContactList.size());
        for (int i=0; i < expectedContactList.size(); i++){
            assertEquals(expectedContactList.get(i), contactList.get(i));
        }
    }

    @Test
    public void testInsertEnquiry(){
        int index = new Random().nextInt(5);
        Enquiry enquiry = enquiryList.get(index);
        Optional<Contact> result = enquiryService.insertEnquiry(enquiry);
        assertTrue(result.isPresent());
        assertAll(() -> {
            assertEquals(enquiry.getFirst_name(), result.get().getFirst_name());
            assertEquals(enquiry.getLast_name(), result.get().getLast_name());
            assertEquals(enquiry.getEmail(), result.get().getEmail());
            assertEquals(enquiry.getPhone(), result.get().getPhone());
            assertEquals(enquiry.getAddress_line1(), result.get().getAddress_line1());
            assertEquals(enquiry.getAddress_line2(), result.get().getAddress_line2());
            assertEquals(enquiry.getMessage(), result.get().getMessage());
        });
    }

    @Test
    public void testDeleteById(){
        int index = new Random().nextInt(5);
        Contact contact = expectedContactList.remove(index);
        enquiryService.deleteById(contact.getId());
        List<Contact> contactList = enquiryService.getAllEnquiries();
        assertFalse(contactList.contains(contact));
    }
}
