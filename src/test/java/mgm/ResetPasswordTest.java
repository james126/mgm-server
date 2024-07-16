package mgm;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class ResetPasswordTest {
    @Autowired
    private MockMvc mvc;

    public String mockValidEmail = "test1@test.com";
    public String mockInvalidEmail = "test@test.com";

    @BeforeAll
    static void setup() {

    }

    @Test
    @DisplayName("Endpoint /forgot-pass - successfully finds an account matching the provided email address")
    public void forgotPassSuccess() throws Exception {
        String email = mockValidEmail;
        mvc.perform(post("http://localhost:8080/forgot-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(email))
                .andExpect(jsonPath("$.outcome").value(true));
    }

    @Disabled
    @Test
    @DisplayName("Endpoint /forgot-pass - sends an email to the provided email address")
    public void forgotPassEmail() throws Exception {
        mvc.perform(post("http://localhost:8080/forgot-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject(mockValidEmail).toString()))
                .andExpect(jsonPath("$.outcome").value(true));
    }

    @Disabled
    @Test
    @DisplayName("Endpoint /forgot-pass - unsuccessfully finds an account matching the provided email address")
    public void forgotPassFailure() {

    }

    @Disabled
    @Test
    @DisplayName("Endpoint /forgot-pass - throws an exception when looking for an account that matches the provided email")
    public void forgotPassException() {

    }

}
