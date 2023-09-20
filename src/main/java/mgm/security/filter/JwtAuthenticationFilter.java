package mgm.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mgm.service.CustomUserDetailsService;
import mgm.utility.JwtUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger("Security");

    private final Set<String> cachePaths = new HashSet<>(Arrays.asList("/view-next", "/delete"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // get JWT token from http request
            String token = getTokenFromRequest(request);

            // validate token
            if (StringUtils.hasText(token) && jwtUtility.validateToken(token)) {

                // get username from token
                String username = jwtUtility.getUsername(token);

                // load the user associated with token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.info("Cannot filter JWT token: [{}]", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return cachePaths.stream().noneMatch(path -> request.getRequestURI().endsWith(path));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> token = Arrays.stream(cookies).filter(cookie -> "Bearer".equals(cookie.getName())).findFirst();

        return token.map(Cookie::getValue).orElse(null);
    }
}
