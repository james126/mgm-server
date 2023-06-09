//package mgm.service;
//
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//
//@ConfigurationProperties("date.zone")
//@Data
//public class DateTime {
//    @Value("${date.zone}")
//    private String zone;
//
//    public Date getDate(){
//        return Date.from(ZonedDateTime.now(ZoneId.of(zone)).truncatedTo(ChronoUnit.SECONDS).toInstant());
//    }
//}
