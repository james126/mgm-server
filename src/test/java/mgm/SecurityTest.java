package mgm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(EnquiryContactBuilder.class)
public class SecurityTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void endpointIndexAuthorized() throws Exception {
        this.mvc.perform(get("/index"))
                .andExpect(status().isOk());
    }

    @Test
    void endpointLoginAuthorized() throws Exception {
        this.mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

}
