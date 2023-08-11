package mgm;

import mgm.utility.DateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Main.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DateTimeTest {

    @Autowired
    private DateTime dateTime;

    @Mock
    private DateTime mockDateTime;

    @Test
    @DisplayName("Get zone from application.yml")
    public void getZone() {
        when(mockDateTime.getZone()).thenReturn("Pacific/Auckland");
        assertEquals(mockDateTime.getZone(), dateTime.getZone());
    }

    @Test
    @DisplayName("Verify zone is supported")
    public void validZone() {
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        assertTrue(zoneIds.contains(dateTime.getZone()));
    }

    @Test
    public void getDate() {
       given(mockDateTime.getDate()).willReturn(Date.from(ZonedDateTime.now(ZoneId.of("Pacific/Auckland")).
                truncatedTo(ChronoUnit.SECONDS).toInstant()));

        assertEquals(mockDateTime.getDate(), dateTime.getDate());
    }

    @Test
    @DisplayName("Milliseconds have been removed")
    public void datePrecision() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.getDate());
        int milliseconds = calendar.get(Calendar.MILLISECOND);
        assertEquals(0, milliseconds);
    }

    @Test
    @DisplayName("Exception thrown when zone is null")
    public void zoneNotSet() {
        dateTime.setZone(null);
        assertThrows(NullPointerException.class, () -> dateTime.getDate());
    }
}
