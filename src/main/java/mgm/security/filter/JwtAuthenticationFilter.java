package mgm.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mgm.security.CustomUserDetailsService;
import mgm.utility.JwtUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties("authenticate.path")
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${authenticate.path[0]}")
    private String path0;

    @Value("${authenticate.path[1]}")
    private String path1;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger("Security");

    Set<String> cachePaths = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (cachePaths == null){
            cachePaths = new HashSet<>(Arrays.asList(path0, path1));
        }

        if (cachePaths.stream().noneMatch(path -> request.getRequestURI().endsWith(path))){
            filterChain.doFilter(request, response);
            return;
        }

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

    private String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> token = Arrays.stream(cookies).filter(cookie -> "Bearer".equals(cookie.getName())).findFirst();

        return token.map(Cookie::getValue).orElse(null);
    }
}
