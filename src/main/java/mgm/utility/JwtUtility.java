package mgm.utility;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Methods for generating, validating, extracting info from JSON Web Tokens
 */
@ConfigurationProperties("app.jwt")
@Component
public class JwtUtility {

    private static final Logger logger = LoggerFactory.getLogger("Security");

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-milliseconds}")
    private long jwtExpirationDate;

    // generate JWT token
    public String generateToken(String username){
//        Date currentDate = new Date();
//        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        Clock clock = DefaultClock.INSTANCE;
        Instant currentDate = clock.now().toInstant();
        Instant expireDate = currentDate.plus(1, ChronoUnit.HOURS);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(currentDate))
                .setExpiration(Date.from(expireDate))
                .signWith(key())
                .compact();
        return token;
    }

    public ResponseCookie generateCookie(String jwtString, String requestUrl){
        ResponseCookie cookie = ResponseCookie.from("Bearer", jwtString)
                .httpOnly(true)
                .domain(requestUrl)
                .secure(true) // Cookie transmitted only over https then set to true
                .maxAge(Duration.ofHours(1))
                .sameSite("Lax")  // SameSite=Lax, secure cookies have to be sent over HTTPS
                .path("/")
                .build();

        return cookie;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // get username from Jwt token
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    // validate Jwt token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.info("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.info("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.info("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.info("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
