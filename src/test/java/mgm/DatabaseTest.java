package mgm;

import jakarta.validation.ConstraintViolationException;
import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import mgm.utilities.ContactBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.exparity.hamcrest.date.InstantMatchers.within;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test using external Postgres database
 */
@SpringBootTest(classes = Main.class)
@Import(ContactBuilder.class)
public class DatabaseTest {

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
    public void testInsertContact() {
        //Delete all database records
        List<Contact> contactList = contactService.getAllContacts();
        contactList.forEach(contact -> {
            assertNotNull(contact.getId());
            contactService.deleteById(contact.getId());
        });
        List<Contact> deleteResult = contactService.getAllContacts();
        assertTrue(deleteResult.isEmpty());

        //Insert a single record
        int id = getRandomId();
        Contact expected = getMockedContactById(id);
        //Id is auto-incremented in database
        expected.setId(null);
        contactService.insertContact(expected);

        List<Contact> resultList = contactService.getAllContacts();
        assertEquals(1, resultList.size());
        assertAll(() -> {
            Contact actual = resultList.get(0);
            assertEquals(actual.getFirst_name(), expected.getFirst_name());
            assertEquals(actual.getLast_name(), expected.getLast_name());
            assertEquals(actual.getEmail(), expected.getEmail());
            assertEquals(actual.getPhone(), expected.getPhone());
            assertEquals(actual.getAddress_line1(), expected.getAddress_line1());
            assertEquals(actual.getAddress_line2(), expected.getAddress_line2());
            assertEquals(actual.getMessage(), expected.getMessage());
            assertThat(actual.getUpdate_datetime().toInstant(), within(1, ChronoUnit.MINUTES,
                    expected.getUpdate_datetime().toInstant()));
        });
    }

    @Test
    public void testInsertEmptyContact() {
        //Delete all database records
        List<Contact> contactList = contactService.getAllContacts();
        contactList.forEach(contact -> {
            assertNotNull(contact.getId());
            contactService.deleteById(contact.getId());
        });
        List<Contact> deleteResult = contactService.getAllContacts();
        assertTrue(deleteResult.isEmpty());

        //Insert an empty record
        Contact empty = new Contact();
        assertThrows(ConstraintViolationException.class, () -> contactService.insertContact(empty));
    }

    @Test
    public void testInsertNullContact() {
        assertThrows(NullPointerException.class, () -> contactService.insertContact(null));
    }

    @Test
    public void testInsertContactWithDuplicateId() {
        //Delete all database records
        List<Contact> contactList = contactService.getAllContacts();
        contactList.forEach(contact -> {
            assertNotNull(contact.getId());
            contactService.deleteById(contact.getId());
        });
        List<Contact> deleteResult = contactService.getAllContacts();
        assertTrue(deleteResult.isEmpty());

        int id = getRandomId();
        Contact contact = getMockedContactById(id);
        contact.setId(1);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> contactService.insertContact(contact));
    }

    @Test
    public void testDeleteById() {
        Integer id = getRandomId();
        assertNotNull(id);
        contactService.deleteById(id);
        List<Contact> contactList = contactService.getAllContacts();
        contactList.forEach(contact -> {
            assertThat(hasProperty("id"), is(not(id)));
        });
    }

    @Test
    public void testDeleteByNull() {
        assertThrows(NullPointerException.class, () -> contactService.deleteById(null));
    }

    @Test
    public void testDeleteByUnassignedId() {
        assertDoesNotThrow(() -> contactService.deleteById(Integer.MAX_VALUE));
    }

    @Test
    public void testFindByMinId() {
        Optional<Contact> contact = contactService.findByMinId();
        assertTrue(contact.isPresent());
    }

    @Test
    public void testFindByMinIdWhenNoRecords() {
        //Delete all database records
        List<Contact> contactList = contactService.getAllContacts();
        contactList.forEach(contact -> {
            assertNotNull(contact.getId());
            contactService.deleteById(contact.getId());
        });

        Optional<Contact> contact = contactService.findByMinId();
        assertFalse(contact.isPresent());
    }

    @Test
    public void testGetNextContactForm() {
        Optional<Contact> contact = contactService.getNextContactForm(0);
        assertTrue(contact.isPresent());
    }

    @Test
    public void testGetNextContactFormWithInvalidId() {
        Optional<Contact> contact = contactService.getNextContactForm(Integer.MIN_VALUE);
        assertTrue(contact.isPresent());
    }

    @Test
    public void testGetNextContactFormNull() {
        assertThrows(NullPointerException.class, () -> contactService.getNextContactForm(null));
    }

    public int getRandomId() {
        int min = 1, max = 5;
        return new Random().nextInt(max - min + 1) + min;
    }

    public Contact getMockedContactById(int id) {
        return contactList.get(id - 1);
    }
}
