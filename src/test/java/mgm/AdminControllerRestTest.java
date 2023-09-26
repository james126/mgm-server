package mgm;

import jakarta.servlet.http.Cookie;
import mgm.controller.AdminController;
import mgm.service.ContactServiceImpl;
import mgm.utilities.ContactBuilder;
import mgm.utility.JwtUtility;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest(classes = {Main.class, AdminController.class})
@Import(ContactBuilder.class)
public class AdminControllerRestTest {
    @Autowired
    AdminController controller;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvcSecurity;

    @MockBean
    ContactServiceImpl service;

    @Autowired
    JwtUtility jwtUtility;


    @BeforeEach
    public void setup() {
        mvcSecurity = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void adminViewNext() throws Exception {
        String token = jwtUtility.generateToken("user1");
        jakarta.servlet.http.Cookie cookie = new Cookie("Bearer", token);

        MvcResult value = mvcSecurity.perform(MockMvcRequestBuilders.post("http://localhost:8080/admin/view-next")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().is(HttpStatus.SC_OK)).andReturn();

        MockHttpServletResponse response = value.getResponse();
        Cookie responseCookie = response.getCookie("Bearer");
        Assertions.assertAll(() -> {
            assert responseCookie != null;
            assertNotNull(responseCookie.getValue());
            assertTrue(responseCookie.isHttpOnly());
            assertThat(responseCookie.getValue(), Matchers.instanceOf(String.class));
        });
    }

    @Test
    void adminDelete() throws Exception {
        String token = jwtUtility.generateToken("user1");
        jakarta.servlet.http.Cookie cookie = new Cookie("Bearer", token);

        MvcResult value = mvcSecurity.perform(MockMvcRequestBuilders.post("http://localhost:8080/admin/delete")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().is(HttpStatus.SC_OK)).andReturn();

        MockHttpServletResponse response = value.getResponse();
        Cookie responseCookie = response.getCookie("Bearer");
        Assertions.assertAll(() -> {
            assert responseCookie != null;
            assertNotNull(responseCookie.getValue());
            assertTrue(responseCookie.isHttpOnly());
            assertThat(responseCookie.getValue(), Matchers.instanceOf(String.class));
        });
    }

    @Test
    void invalidCredentials() throws Exception {
        String token = jwtUtility.generateToken("abc");
        jakarta.servlet.http.Cookie cookie = new Cookie("Bearer", token);

        mvcSecurity.perform(MockMvcRequestBuilders.post("http://localhost:8080/admin/delete")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().is(HttpStatus.SC_FORBIDDEN));
    }
}
