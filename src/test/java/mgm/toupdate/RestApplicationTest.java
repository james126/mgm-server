package mgm.toupdate;

import mgm.ContactBuilder;
import mgm.Main;
import mgm.model.entity.Contact;
import mgm.service.ContactServiceImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@Import(ContactBuilder.class)
public class RestApplicationTest {

    @Autowired
    private final List<Contact> contactList = new ArrayList<>();

    @Autowired
    private MockMvc mvc;

    @MockBean
    ContactServiceImpl service;

    @Test
    void testGetRoot() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")))
                .andExpect(view().name("index"));
    }

    @Test
    void testGetIndex() throws Exception {
        mvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")))
                .andExpect(view().name("index"));
    }

    @Test
    void testGetLogin() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")))
                .andExpect(view().name("login"));
    }

    @Test
    void testPostFormStatusOk() throws Exception {
        Contact contact = getContact();
        mvc.perform(post("http://localhost:5000/form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(getEnquiryJsonString(contact)))
                .andExpect(status().is(200));
    }

    @Test
    void testPostFormWithoutCsrf() throws Exception {
        Contact contact = getContact();
        mvc.perform(post("http://localhost:5000/form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getEnquiryJsonString(contact)))
                .andExpect(status().is(403));
    }

    public Contact getContact() {
        Random rand = new Random();
        return contactList.get(rand.nextInt(contactList.size()));
    }

    public String getEnquiryJsonString(Contact contact) {
        Map<String, String> map = new HashMap<>();
        //map.put("id", String.valueOf(contact.getId()));
        map.put("first_name", contact.getFirst_name());
        map.put("last_name", contact.getLast_name());
        map.put("email", contact.getEmail());
        map.put("phone", contact.getPhone());
        map.put("address_line1", contact.getAddress_line1());
        map.put("address_line2", contact.getAddress_line2());
        map.put("message", contact.getMessage());
        return new JSONObject(map).toString();
    }
}
