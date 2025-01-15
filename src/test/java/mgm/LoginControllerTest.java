package mgm;

import jakarta.servlet.http.Cookie;
import mgm.utilities.ContactBuilder;
import mgm.utility.JwtUtility;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest(classes = Main.class)
@Import(ContactBuilder.class)
public class LoginControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    JwtUtility jwtUtility;

        @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void invalidLoginAttempt() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", "quickBrownFoxquickBrownFox");
        map.put("password", "Under#Tree1Under#Tree1");

        assertThrows(AccessDeniedException.class, () ->
                mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(map).toString())));
    }

    @Test
    public void validLoginAttempt() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", "quickBrownFox");
        map.put("password", "Under#Tree1");

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(map).toString()))
                .andExpect(status().is(HttpStatus.SC_OK));
    }

    @Test
    public void Logout() throws Exception {
        String username = "quickBrownFox";
        String token = jwtUtility.generateToken(username);
        Cookie cookie = new Cookie("Bearer", token);

        mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/custom-logout")
                        .cookie(cookie))
                .andExpect(status().is(HttpStatus.SC_OK));
    }
}
