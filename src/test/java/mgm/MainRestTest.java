package mgm;

import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import mgm.utilities.ContactBuilder;
import mgm.utilities.ContactParser;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@Import(ContactBuilder.class)
public class MainRestTest {
    @Autowired
    private ContactParser contactParser;

    @Autowired
    private final List<Contact> contactList = new ArrayList<>();

    @Autowired
    private MockMvc mvc;

    @MockBean
    ContactServiceImpl service;

    @Captor
    ArgumentCaptor<Contact> contactCaptor;

    @Test
    void testStartup() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testIndex() throws Exception {
        mvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testSubmitContactFormUrlEncoded() throws Exception {
        Contact contact = getContact();
        contact.setId(null);
        contact.setUpdate_datetime(null);
        doNothing().when(service).insertContact(contact);

        mvc.perform(post("http://localhost:8080/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .param("first_name", contact.getFirst_name())
                        .param("last_name", contact.getLast_name())
                        .param("email", contact.getEmail())
                        .param("phone", contact.getPhone())
                        .param("address_line1", contact.getAddress_line1())
                        .param("address_line2", contact.getAddress_line2())
                        .param("message", contact.getMessage()))
                .andExpect(status().isOk());
        verify(service, times(1)).insertContact(any(Contact.class));
        verify(service).insertContact(contactCaptor.capture());
        Contact captured = contactCaptor.getValue();
        assertAll(() -> {
            assertEquals(contact.getFirst_name(), captured.getFirst_name());
            assertEquals(contact.getLast_name(), captured.getLast_name());
            assertEquals(contact.getEmail(), captured.getEmail());
            assertEquals(contact.getPhone(), captured.getPhone());
            assertEquals(contact.getAddress_line1(), captured.getAddress_line1());
            assertEquals(contact.getAddress_line2(), captured.getAddress_line2());
            assertEquals(contact.getMessage(), captured.getMessage());
        });
    }

    @Test
    void testSubmitContactFormJSON() throws Exception {
        Contact contact = getContact();
        contact.setId(null);
        contact.setUpdate_datetime(null);
        doNothing().when(service).insertContact(contact);

        mvc.perform(post("http://localhost:8080/form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(contactParser.toJSON(contact)))
                .andExpect(status().isOk());

        verify(service, times(1)).insertContact(any(Contact.class));
        verify(service).insertContact(contactCaptor.capture());
        Contact captured = contactCaptor.getValue();
        assertAll(() -> {
            assertNull(captured.getFirst_name());
            assertNull(captured.getLast_name());
            assertNull(captured.getEmail());
            assertNull(captured.getPhone());
            assertNull(captured.getAddress_line1());
            assertNull(captured.getAddress_line2());
            assertNull(captured.getMessage());
        });
    }

    public Contact getContact() {
        Random rand = new Random();
        return contactList.get(rand.nextInt(contactList.size()));
    }
}
