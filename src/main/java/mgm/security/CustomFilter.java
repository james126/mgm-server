package mgm.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CustomFilter extends OncePerRequestFilter {

    private final String HEADER_NAME = "x-robot-password";

    private final AuthenticationManager authenticationManager;

    public CustomFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("FILTER");


        HttpServletRequest req = (HttpServletRequest) request;
        //System.out.println("CustomFilter : " + req);

        //0 Should we execute filter?
//        if (!Collections.list(request.getHeaderNames()).contains(HEADER_NAME)){
//            filterChain.doFilter(request, response); //finishes filter chain
//            return;
//        }

        //1 Authentication Decision
        try {
            var password = request.getHeader(HEADER_NAME);
            var authRequest = LoginAuthentication.unauthenticated(password);
            var authentication = authenticationManager.authenticate(authRequest);
            var newContext = SecurityContextHolder.createEmptyContext();
            //newContext.setAuthentication(UsernamePasswordAuthenticationToken.authenticated());
            newContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(newContext);
            filterChain.doFilter(request, response); //finishes filter chain
            return;
        } catch (AuthenticationException e){
            //Not authenticated
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-type", "text/plain;charset=utf-8");
            response.getWriter().println(e.getMessage());
            return;
        }

        //curl localhost:8080/login -H "x-robot-password: beep-boop" -v
    }
}

