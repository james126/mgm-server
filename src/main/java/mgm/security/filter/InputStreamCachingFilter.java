package mgm.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mgm.security.caching.CachedBodyHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class InputStreamCachingFilter extends OncePerRequestFilter {
    private final Set<String> cachePaths = new HashSet<>(Arrays.asList("/view-next", "/delete", "/form"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
        filterChain.doFilter(cachedBodyHttpServletRequest, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return cachePaths.stream().noneMatch(path -> request.getRequestURI().endsWith(path));
    }
}
