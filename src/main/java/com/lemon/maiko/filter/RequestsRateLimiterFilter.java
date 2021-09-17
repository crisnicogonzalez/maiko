package com.lemon.maiko.filter;

import com.lemon.maiko.core.services.ApiRateLimitService;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestsRateLimiterFilter implements javax.servlet.Filter {

    private static final String USER_API_ID_HEADER = "User-Api-Id";
    private final ApiRateLimitService apiRateLimitService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestsRateLimiterFilter.class);

    public RequestsRateLimiterFilter(ApiRateLimitService apiRateLimitService) {
        this.apiRateLimitService = apiRateLimitService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            final String userApiId = ((HttpServletRequest) request).getHeader(USER_API_ID_HEADER);
            if (userApiId == null) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpStatus.BAD_REQUEST_400);
                httpResponse.getWriter().print("User-Api-Id header is mandatory");
            } else if (this.apiRateLimitService.userHaveReachedRateLimit(userApiId)) {
                LOGGER.error("User {} have reached request limit", userApiId);
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS_429);
                httpResponse.getWriter().print("Too many requests");
            } else {
                chain.doFilter(request, response); // This signals that the request should pass this filter
            }
        }
    }

}
