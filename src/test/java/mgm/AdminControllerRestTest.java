package mgm;

import jakarta.servlet.http.Cookie;
import mgm.controller.AdminController;
import mgm.service.ContactServiceImpl;
import mgm.utilities.ContactBuilder;
import mgm.utilities.CustomAuthentication;
import mgm.utilities.CustomModel;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
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
    CustomModel model;
    @Autowired
    CustomAuthentication customAuthentication;

    @BeforeEach
    public void setup() {
        mvcSecurity = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

       customAuthentication.setName("user1");
    }

    @Test
    void adminPageTestForwardUrl() throws Exception {
        mvcSecurity.perform(formLogin("http://localhost:8080/login")
                        .user("user1")
                        .password("password"))
                .andExpect(forwardedUrl("/admin"))
                .andExpect(status().is(HttpStatus.SC_OK));
    }

    @Test
    void adminPageTestJwtCookie() throws Exception {
//        MvcResult value = mvcSecurity.perform(formLogin("http://localhost:8080/login")
//                        .user("user1")
//                        .password("password"))
//                .andExpect(forwardedUrl("/admin"))
//                .andExpect(status().is(HttpStatus.SC_OK)).andReturn();
//
//        MockHttpServletResponse response = value.getResponse();
//
//        controller.adminPage(model, customAuthentication, response);
//
//        Cookie cookie = response.getCookie("Bearer");
//        Assertions.assertAll(() -> {
//            assertNotNull(cookie.getValue());
//            assertTrue(cookie.isHttpOnly());
//            assertThat(cookie.getValue(), Matchers.instanceOf(String.class));
//        });
    }
}
