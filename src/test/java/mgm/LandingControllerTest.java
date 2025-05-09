package mgm;

import mgm.controller.utility.ContactParser;
import mgm.model.entity.Contact;
import mgm.repository.ContactRepository;
import mgm.utilities.ContactBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(ContactBuilder.class)
public class LandingControllerTest {
    @Autowired
    private ContactParser contactParser;

    @Autowired
    private final List<Contact> contactList = new ArrayList<>();

    @Autowired
    private MockMvc mvc;

    @Autowired
    ContactRepository repository;

    @Test
    void testSubmitContactFormJSON() throws Exception {
        Contact expected = getContact();
        expected.setUpdate_datetime(null);

        mvc.perform(post("http://localhost:8080/contact-form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contactParser.toJsonString(expected)))
                .andExpect(status().isOk());

        Optional<Contact> retrieved = repository.findAll().stream().findFirst();
        assertTrue(retrieved.isPresent());
        Contact actual = retrieved.get();

        assertAll(() -> {
            assertEquals(expected.getFirst_name(), actual.getFirst_name());
            assertEquals(expected.getLast_name(), actual.getLast_name());
            assertEquals(expected.getEmail(), actual.getEmail());
            assertEquals(expected.getPhone(), actual.getPhone());
            assertEquals(expected.getAddress_line1(), actual.getAddress_line1());
            assertEquals(expected.getAddress_line2(), actual.getAddress_line2());
            assertEquals(expected.getMessage(), actual.getMessage());
        });
    }

    public Contact getContact() {
        Random rand = new Random();
        return contactList.get(rand.nextInt(contactList.size()));
    }
}
