package mgm;

import mgm.service.DateTime;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DateUnitTests {

    @Mock
    private DateTime mockDateTime;

    @Autowired
    private DateTime dateTime;

    @AfterAll
    public void tearDown() {
        dateTime.setZone("Pacific/Auckland");
    }

    @Test
    @Order(1)
    @DisplayName("Get zone from application.yml")
    public void getZone() {
        given(mockDateTime.getZone()).willReturn("Pacific/Auckland");
        String zone = dateTime.getZone();
        assertEquals(mockDateTime.getZone(), zone);
    }

    @Test
    @Order(2)
    @DisplayName("Verify zone is supported")
    public void validZone() {
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        String zone = dateTime.getZone();
        assertTrue(zoneIds.contains(zone));
    }

    @Test
    @Order(3)
    public void getDate() {
        given(mockDateTime.getDate()).willReturn(Date.from(ZonedDateTime.now(ZoneId.of("Pacific/Auckland")).
                truncatedTo(ChronoUnit.SECONDS).toInstant()));
        assertEquals(mockDateTime.getDate(), dateTime.getDate());
    }

    @Test
    @Order(4)
    @DisplayName("Milliseconds have been removed")
    public void datePrecision() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.getDate());
        int milliseconds = calendar.get(Calendar.MILLISECOND);
        assertEquals(0, milliseconds);
    }

    @Test
    @Order(5)
    @DisplayName("Exception thrown when zone is null")
    public void zoneNotSet() {
        dateTime.setZone(null);
        assertThrows(NullPointerException.class, () -> dateTime.getDate());
    }
}
