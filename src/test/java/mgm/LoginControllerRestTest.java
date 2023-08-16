package mgm;

import mgm.controller.LoginController;
import mgm.service.ContactServiceImpl;
import mgm.utilities.ContactBuilder;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@SpringBootTest(classes = Main.class)
@Import(ContactBuilder.class)
public class LoginControllerRestTest {

    @Autowired
    LoginController controller;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    ContactServiceImpl service;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void getLoginLoadView() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void invalidLoginAttemptIncorrectUsernamePassword() throws Exception {
        mvc.perform(formLogin("http://localhost:8080/login")
                .user("abc")
                .password("123"))
                .andExpect(forwardedUrl("/invalid"))
                .andExpect(status().is(HttpStatus.SC_OK));
    }
}
