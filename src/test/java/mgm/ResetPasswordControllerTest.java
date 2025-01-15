package mgm;

import mgm.model.entity.Users;
import mgm.repository.UsersRepository;
import mgm.service.EmailService;
import mgm.service.ResetPasswordImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ResetPasswordControllerTest {
    @MockBean()
    @Qualifier("customEmailService")
    EmailService emailService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ResetPasswordImpl passwordImpl;

    @Autowired
    private static MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    public String mockValidEmail = "test3@test.com";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Endpoint /forgot-pass - finds an account by email address")
    public void findAccountByEmail() {
        String email = mockValidEmail;
        boolean exists = passwordImpl.forgotPassword(email);
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Endpoint /forgot-pass - resets password")
    public void forgotPassSuccess() throws Exception {
        String email = mockValidEmail;
        Users user = usersRepository.findByEmail(email).get();
        String passBefore = user.getPassword();

        mvc.perform(post("http://localhost:8080/forgot-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(email))
                .andExpect(jsonPath("$.outcome").value(true));

        user = usersRepository.findByEmail(email).get();
        String passAfter = user.getPassword();
        Assertions.assertNotEquals(passBefore, passAfter);
    }

    @Test
    @DisplayName("Endpoint /forgot-pass - sends an email with the new password")
    public void forgotPassEmail() throws Exception {
        mvc.perform(post("http://localhost:8080/forgot-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockValidEmail))
                        .andExpect(jsonPath("$.outcome").value(true))
                        .andExpect(jsonPath("$.temporaryPassword").value(true));

        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Endpoint /new-pass - updates an account password given username and new password")
    public void newPassSuccess() throws Exception {
        Users user = usersRepository.findByEmail(mockValidEmail).get();
        String password = user.getPassword();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", user.getUsername());
        jsonObject.put("password", "randomPassword!1");

        mvc.perform(post("http://localhost:8080/new-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(jsonPath("$.outcome").value(true));

        Users updatedUser = usersRepository.findByEmail(mockValidEmail).get();
        String updatedPassword = updatedUser.getPassword();
        Assertions.assertNotEquals(password, updatedPassword);
    }

    @Test
    @DisplayName("Endpoint /new-pass - returns outcome false when trying to update an account that doesn't exist")
    public void newPassError() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "user9");
        jsonObject.put("password", "randomPassword!1");

        mvc.perform(post("http://localhost:8080/new-pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(jsonPath("$.outcome").value(false));
    }
}
