package mgm;

import jakarta.servlet.http.Cookie;
import mgm.controller.LoginController;
import mgm.service.ContactServiceImpl;
import mgm.utilities.ContactBuilder;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest(classes = Main.class)
@Import(ContactBuilder.class)
public class LoginControllerTest {
    private static String cookie = "";

    @Autowired
    LoginController controller;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    ContactServiceImpl service;

    @BeforeAll
    static void initialSetup() throws IOException {
        String cookieFilePath = "src/test/resources/jwt_cookie.json";
        Path path = Paths.get(cookieFilePath);
        assumeTrue(Files.exists(path), "Path not found: " + cookieFilePath);
        File file = new File(cookieFilePath);
        String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
        cookie = new JSONObject(content).toString();
    }

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
        JSONObject obj = new JSONObject(cookie);
        Cookie cookie = new Cookie("Bearer", obj.getString("Bearer"));

        mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/custom-logout")
                        .cookie(cookie))
                .andExpect(status().is(HttpStatus.SC_OK));
    }
}
