package mgm;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.exparity.hamcrest.date.InstantMatchers.within;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
@Import(ContactBuilder.class)
public class DatabaseApplicationTest {

    @Autowired
    private ContactServiceImpl contactService;

    @Autowired
    private final List<Contact> contactList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        contactList.forEach(form -> {
            int id = form.getId();
            form.setId(null);
            contactService.insertContact(form);
            form.setId(id);
        });
    }

    @AfterEach
    public void tearDown() {
        contactList.forEach(contact -> {
            contactService.deleteById(contact.getId());
        });
    }

    @Test
    public void testGetById() {
        int id = getRandomId();
        Contact contact = getMockedContactById(id);
        Optional<Contact> result = contactService.findById(id);
        assertTrue(result.isPresent());
        assertAll(() -> {
            assertEquals(contact.getId(), result.get().getId());
            assertEquals(contact.getFirst_name(), result.get().getFirst_name());
            assertEquals(contact.getLast_name(), result.get().getLast_name());
            assertEquals(contact.getEmail(), result.get().getEmail());
            assertEquals(contact.getPhone(), result.get().getPhone());
            assertEquals(contact.getAddress_line1(), result.get().getAddress_line1());
            assertEquals(contact.getAddress_line2(), result.get().getAddress_line2());
            assertEquals(contact.getMessage(), result.get().getMessage());
            MatcherAssert.assertThat(contact.getUpdate_datetime().toInstant(), within(1, ChronoUnit.HOURS,
                    result.get().getUpdate_datetime().toInstant()));
        });
    }

    @Test
    public void testDeleteById() {
        int id = getRandomId();
        Contact contact = getMockedContactById(id);
        contactService.deleteById(id);
        List<Contact> contactList = contactService.getAllContacts();
        assertFalse(contactList.contains(contact));
    }

    @Test
    public void testInsertEnquiry() {
        List<Contact> contactList = contactService.getAllContacts();

        contactList.forEach(contact -> contactService.deleteById(contact.getId()));
        List<Contact> deleteResult = contactService.getAllContacts();
        assertTrue(deleteResult.isEmpty());

        contactList.forEach(contact -> {
            int id = contact.getId();
            contact.setId(null);
            contactService.insertContact(contact);
            contact.setId(id);
        });
        List<Contact> insertResult = contactService.getAllContacts();
        assertEquals(insertResult.size(), 5);
    }

    public int getRandomId() {
        int min = 1, max = 5;
        return new Random().nextInt(max - min + 1) + min;
    }

    public Contact getMockedContactById(int id) {
        return contactList.get(id - 1);
    }
}
