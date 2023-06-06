//package main;
//
//import main.model.Form;
//import main.repository.FormRepository;
//import org.junit.jupiter.api.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import utility.RandomStringUtility;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class MainControllerIntegrationTests {
//    private String firstName;
//    private String lastName;
//    private List<Form> forms;
//    private Logger logger;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private FormRepository repository;
//
//    @BeforeAll
//    void init() {
//        firstName = RandomStringUtility.generateRandomString(10);
//        lastName = RandomStringUtility.generateRandomString(10);
//        logger = LoggerFactory.getLogger("console");
//    }
//
//    @AfterAll
//    void cleanUp() {
//        Optional<Form> optionalForm = forms.stream()
//                .filter(form -> Objects.equals(form.getFirstName(), firstName) && Objects.equals(form.getLastName(), lastName)).findFirst();
//        repository.deleteById(optionalForm.get().getId());
//    }
//
//    @Test
//    @DisplayName("Mock POST form request")
//    public void postForm() throws Exception {
//        mvc.perform(post("http://localhost:8080/form").contentType(MediaType.APPLICATION_JSON)
//                .param("firstName", firstName)
//                .param("lastName", lastName));
//
//        forms = repository.findAll();
//        assertThat(forms).extracting(Form::getFirstName).contains(firstName);
//        assertThat(forms).extracting(Form::getLastName).contains(lastName);
//        logger.info("Test - firstName: {}, lastName: {}", firstName, lastName);
//    }
//}
