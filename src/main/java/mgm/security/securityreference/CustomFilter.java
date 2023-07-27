package mgm.security.secdev;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

public class CustomFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public CustomFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("HTTP REQUEST");
        String path = request.getPathInfo();

        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("HEADER");
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
        }

        System.out.println("BODY");
        String contentType = request.getContentType();
        MediaType type= StringUtils.hasText(contentType)? MediaType.valueOf(contentType):null;
        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest((HttpServletRequest) request);
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

        if (formHttpMessageConverter.canRead(MultiValueMap.class,type)) {
            MultiValueMap<String, String> read = formHttpMessageConverter.read(null, serverHttpRequest);
            System.out.println(read);
        }

        System.out.println("**************************************************************");

        filterChain.doFilter(request, response);


//        try {
//            var password = request.getHeader(HEADER_NAME);
//            var authRequest = RobotAuthentication.unauthenticated(password);
//            var authentication = authenticationManager.authenticate(authRequest);
//            var newContext = SecurityContextHolder.createEmptyContext();
//            //newContext.setAuthentication(UsernamePasswordAuthenticationToken.authenticated());
//            newContext.setAuthentication(authentication);
//            SecurityContextHolder.setContext(newContext);
//            filterChain.doFilter(request, response); //finishes filter chain
//            return;
//        } catch (AuthenticationException e){
//            //Not authenticated
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            response.setCharacterEncoding("utf-8");
//            response.setHeader("Content-type", "text/plain;charset=utf-8");
//            response.getWriter().println(e.getMessage());
//            return;
//        }

    }
}

