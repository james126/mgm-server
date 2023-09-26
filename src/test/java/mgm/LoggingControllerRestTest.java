package mgm;

import mgm.utilities.ContactBuilder;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@Import(ContactBuilder.class)
public class LoggingControllerRestTest {
    private static String messageFilePath = "src/test/resources/logging_message.json";
    private static String errorMessage = "";

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    static void setup() throws IOException {
        Path path = Paths.get(messageFilePath);
        assumeTrue(Files.exists(path), "Path not found: " + messageFilePath);
        File file = new File(messageFilePath);
        String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
        errorMessage = new JSONObject(content).toString();
    }

    @Test
    void logMessage() throws Exception {
        mvc.perform(post("http://localhost:8080/client-logging")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(errorMessage))
                .andExpect(status().isOk());
    }
}
