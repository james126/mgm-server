package mgm.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {



    // Build Login REST API
    @PostMapping("/next")
    public String authenticate(HttpServletResponse response){
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + 604800000);
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb"));
        String token = Jwts.builder()
                .setSubject("user1")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
        Cookie cookie = new Cookie("Bearer", token);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return "admin";
    }
}
