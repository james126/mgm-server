package mgm;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(classes = Main.class)
public class StringSanitiserTest {

    private File htmlFile;
    private String filePath = "src/test/resources/google.html";
    private String htmlRegex = "<[^>]*>";
    private String javascriptRegex = "<script[^>]*>|function";

    @BeforeEach
    void setup() throws IOException {
        Path path = Paths.get(filePath);
        assumeTrue(Files.exists(path), "Path not found: " + filePath);
        htmlFile = new File(filePath);
    }

    @Test
    public void removeHtmlFromString() throws IOException {
        String dirty = String.valueOf(Jsoup.parse(htmlFile));
        String clean = Jsoup.clean(dirty, Safelist.none());
        assertTrue(true);

        Matcher matcher = Pattern.compile(htmlRegex).matcher(dirty);
        assertTrue(matcher.find());

        matcher = Pattern.compile(htmlRegex).matcher(clean);
        assertFalse(matcher.find());
    }

    @Test
    public void removeJavaScriptFromString() throws IOException {
        String dirty = String.valueOf(Jsoup.parse(htmlFile));
        String clean = Jsoup.clean(dirty, Safelist.none());

        Matcher matcher = Pattern.compile(javascriptRegex).matcher(dirty);
        assertTrue(matcher.find());

        matcher = Pattern.compile(javascriptRegex).matcher(clean);
        assertFalse(matcher.find());
    }
}
