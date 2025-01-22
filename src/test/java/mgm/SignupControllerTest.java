package mgm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mgm.model.dto.Signup;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class SignupControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    static Signup data;
    static JsonNode jsonNode;

    @BeforeAll
    static void initialSetup() throws IOException {
        data = new Signup();
        String filePath = "src/test/resources/form_data.json";
        ObjectMapper objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(new File(filePath));
        data.setUsername(jsonNode.get("username").asText());
        data.setEmail(jsonNode.get("email").asText());
        data.setPassword(jsonNode.get("password").asText());
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testSignup() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", String.valueOf(data.getUsername()));
        map.put("email", String.valueOf(data.getEmail()));
        map.put("password", String.valueOf(data.getPassword()));

        String expected = "{\"outcome\":true,\"error\":\"\",\"temporaryPassword\":false}";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(map).toString())).andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, responseBody);
    }

    @Test
    public void testUsernameTaken() throws Exception {
        String expected = "{\"outcome\":true,\"error\":\"\",\"temporaryPassword\":false}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/username-taken")
                .param("username", "user1"))
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, responseBody);
    }

    @Test
    public void testUsernameNotTaken() throws Exception {
        String expected = "{\"outcome\":false,\"error\":\"\",\"temporaryPassword\":false}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/username-taken")
                        .param("username", "user9"))
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, responseBody);
    }

    @Test
    public void testUsernameTakenException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/username-taken"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testEmailTaken() throws Exception {
        String expected = "{\"outcome\":true,\"error\":\"\",\"temporaryPassword\":false}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/email-taken")
                        .param("email", "test1@test.com"))
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, responseBody);
    }

    @Test
    public void testEmailNotTaken() throws Exception {
        String expected = "{\"outcome\":false,\"error\":\"\",\"temporaryPassword\":false}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/email-taken")
                        .param("email", "test9@test.com"))
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(expected, responseBody);
    }

    @Test
    public void testEmailTakenException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/email-taken"))
                .andExpect(status().isInternalServerError());
    }
}
