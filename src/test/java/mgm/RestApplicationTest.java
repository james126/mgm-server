package mgm;

import mgm.model.dto.Enquiry;
import mgm.model.entity.Contact;
import mgm.service.EnquiryServiceImpl;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(EnquiryContactBuilder.class)
public class RestApplicationTest {
    @Autowired
    private final List<Enquiry> enquiryList = new ArrayList<>();

    @Autowired
    private final List<Contact> contactList = new ArrayList<>();

    @Autowired
    private MockMvc mvc;

    @MockBean
    EnquiryServiceImpl service;

    @Test
    void testGetIndex() throws Exception {
        mvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")))
                .andExpect(view().name("index"));
    }

    @Test
    void testGetServices() throws Exception {
        mvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")))
                .andExpect(view().name("services"));
    }

    @Test
    void testGetAbout() throws Exception {
        mvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")))
                .andExpect(view().name("about"));
    }

    @Test
    void testGetContact() throws Exception {
        mvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/html;charset=UTF-8")))
                .andExpect(view().name("contact"));
    }

    @Test
    void testPostFormStatusOk() throws Exception {
        Enquiry enquiry = getEnquiry();

        when(service.insertEnquiry(enquiry)).thenReturn(Optional.of(contactList.get(0)));
        mvc.perform(post("http://localhost:5000/form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getEnquiryJsonString(enquiry)))
                .andExpect(status().is(200));

        verify(service, times(1)).insertEnquiry(enquiry);
    }

    @Test
    void testPostFormStatusInvalidRequest() throws Exception {
        mvc.perform(post("http://localhost:5000/form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().is(400));
    }

    @Test
    void testPostFormStatusInternalServerError() throws Exception {
        Enquiry enquiry = getEnquiry();

        when(service.insertEnquiry(enquiry)).thenReturn(Optional.empty());
        mvc.perform(post("http://localhost:5000/form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getEnquiryJsonString(enquiry)))
                .andExpect(status().is(500));

        verify(service, times(1)).insertEnquiry(enquiry);
    }

    public Enquiry getEnquiry() {
        Random rand = new Random();
        return enquiryList.get(rand.nextInt(enquiryList.size()));
    }

    public String getEnquiryJsonString(Enquiry enquiry) {
        Map<String, String> map = new HashMap<>();
        map.put("first_name", enquiry.getFirst_name());
        map.put("last_name", enquiry.getLast_name());
        map.put("email", enquiry.getEmail());
        map.put("phone", enquiry.getPhone());
        map.put("address_line1", enquiry.getAddress_line1());
        map.put("address_line2", enquiry.getAddress_line2());
        map.put("message", enquiry.getMessage());
        return new JSONObject(map).toString();
    }
}
