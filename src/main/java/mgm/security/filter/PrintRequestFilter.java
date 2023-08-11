package mgm.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

@Component
public class PrintRequestFilter extends OncePerRequestFilter {
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("**************************************************************");

        System.out.println("HTTP REQUEST");
        System.out.println("\t" + request.getMethod() + " " + request.getRequestURL());

        System.out.println("HEADER:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("\t" + headerName + ":" + request.getHeader(headerName));
        }

        System.out.println("BODY:");
        if ("application/x-www-form-urlencoded".equals(request.getContentType())) {
            String contentType = request.getContentType();
            MediaType type = StringUtils.hasText(contentType) ? MediaType.valueOf(contentType) : null;
            ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

            if (formHttpMessageConverter.canRead(MultiValueMap.class, type)) {
                MultiValueMap<String, String> read = formHttpMessageConverter.read(null, serverHttpRequest);
                read.forEach((k, v) -> {
                    System.out.println("\t" + k + "=" + v);
                });
            }
        }

        if ("application/json".equals(request.getContentType())) {
            try {
                //JSON BODY
                Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = requestMap.get("username");
                String password = requestMap.get("password");
                System.out.println("username: " + username);
                System.out.println("password: " + password);

            } catch (Exception e) {}
        }

        System.out.println("**************************************************************");

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return ("application/json".equals(request.getContentType()));
    }
}

