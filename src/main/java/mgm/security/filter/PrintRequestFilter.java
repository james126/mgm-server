package mgm.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

@Component
public class PrintRequestFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger("Security");

    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("**************************************************************");

        logger.info("HTTP Request");
        logger.info("\t {} {}", request.getMethod(), request.getRequestURL());

        logger.info("Header:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("\t {} : {}", headerName, request.getHeader(headerName));
        }

        logger.info("Body:");
        if ("application/x-www-form-urlencoded".equals(request.getContentType())) {
            String contentType = request.getContentType();
            MediaType type = StringUtils.hasText(contentType) ? MediaType.valueOf(contentType) : null;
            ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

            if (formHttpMessageConverter.canRead(MultiValueMap.class, type)) {
                MultiValueMap<String, String> read = formHttpMessageConverter.read(null, serverHttpRequest);
                read.forEach((k, v) -> {
                    logger.info("\t {} = {}", k, v);
                });
            }
        }

        if ("application/json".equals(request.getContentType())) {
            try {
                //JSON BODY
                Map<String, Object> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                requestMap.forEach((k,v) -> {
                    logger.info("\t {} : {}", k, v);
                });
            } catch (MismatchedInputException e) {
                String result = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
                logger.info("\t {}", result);
            } catch (Exception e){
                logger.error("\tError parsing JSON body {}" , e.getMessage());
            }
        }

        logger.info("**************************************************************");

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return (!"application/json".equals(request.getContentType()));
//    }
}

