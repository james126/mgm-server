package mgm.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomUsernamePasswordFilter extends OncePerRequestFilter {
    AuthenticationManager authenticationManager;

    public CustomUsernamePasswordFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (CorsUtils.isPreFlightRequest(request)) {
            HttpHeaders headers = new HttpHeaders();
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().flush();
            return;
        }

        if (!request.getRequestURI().endsWith("/login") || !request.getMethod().equals("POST")){
            filterChain.doFilter(request, response);
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> map = mapper.readValue(request.getInputStream(), Map.class);
            String username = map.getOrDefault("username", "");
            String password = map.getOrDefault("password", "");

            UsernamePasswordAuthenticationToken token  = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
