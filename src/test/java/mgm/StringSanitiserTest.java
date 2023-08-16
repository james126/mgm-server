package mgm;

import mgm.utility.StringSanitiser;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest()
public class StringSanitiserTest {

    @Autowired
    StringSanitiser stringSanitiser;

    private static File htmlFile;
    private static String filePath = "src/test/resources/google.html";
    private String htmlRegex = "<[^>]*>";
    private String javascriptRegex = "<script[^>]*>|function";

    @BeforeAll
    static void setup() {
        Path path = Paths.get(filePath);
        assumeTrue(Files.exists(path), "Path not found: " + filePath);
        htmlFile = new File(filePath);
    }

    @Test
    public void cleanStringRemoveHtml() throws IOException {
        String dirty = String.valueOf(Jsoup.parse(htmlFile));
        String clean = stringSanitiser.cleanString(dirty);

        Matcher matcher = Pattern.compile(htmlRegex).matcher(dirty);
        assertTrue(matcher.find());

        matcher = Pattern.compile(htmlRegex).matcher(clean);
        assertFalse(matcher.find());
    }

    @Test
    public void cleanStringRemoveJavaScript() throws IOException {
        String dirty = String.valueOf(Jsoup.parse(htmlFile));
        String clean = stringSanitiser.cleanString(dirty);

        Matcher matcher = Pattern.compile(javascriptRegex).matcher(dirty);
        assertTrue(matcher.find());

        matcher = Pattern.compile(javascriptRegex).matcher(clean);
        assertFalse(matcher.find());
    }
}
