package mgm;

import mgm.service.ContactServiceImpl;
import mgm.utilities.ContactBuilder;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@Import(ContactBuilder.class)
public class AdminRestTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ContactServiceImpl service;

    @Test
    void testGetLogin() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testGetLoginWithValidUserDetails() throws Exception {
        mvc.perform(post("http://localhost:8080/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .param("username", "user1")
                        .param("password", "password"))
                .andExpect(status().is(302)) //response.sendRedirect(targetUrl);
                .andExpect(redirectedUrl("/admin"));

    }

    @Test
    void testGetLoginWithInvalidUserDetails() throws Exception {
        mvc.perform(post("http://localhost:8080/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .param("username", "user1")
                        .param("password", "123"))
                .andExpect(status().is(200)) //configure.failureForwardUrl("/invalid");
                .andExpect(forwardedUrl("/invalid"));

    }

    @Test
    void testInvalidLoginAttempt() throws Exception {
        mvc.perform(get("/invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testAdminPage() throws Exception {
        when(service.findByMinId()).thenReturn(Optional.empty());

        mvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(service, times(1)).findByMinId();
    }

    @Test
    void testViewNextContactForm() throws Exception {
        when(service.getNextContactForm(1)).thenReturn(Optional.empty());

        mvc.perform(post("/view")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(toJSON("1")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(service, times(1)).getNextContactForm(1);
    }

    @Test
    void testDeleteContactForm() throws Exception {
        when(service.getNextContactForm(1)).thenReturn(Optional.empty());
        doNothing().when(service).deleteById(1);


        mvc.perform(post("/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(toJSON("1")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(service, times(1)).deleteById(1);
        verify(service, times(1)).getNextContactForm(1);
    }

    public String toJSON(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        return new JSONObject(map).toString();
    }
}
