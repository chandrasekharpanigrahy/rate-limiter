package com.sekhar.rate.limiter;

import lombok.val;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RateLimiterFilter implements Filter {
    private final RateLimiterService rateLimiterService;

    public RateLimiterFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        val clientId = ((HttpServletRequest) request).getHeader("X-API-Key");
        rateLimiterService.updateForRequest(clientId);
        boolean limitExceeded = rateLimiterService.isLimitExceeded(clientId, 2L, 10L);
        if(limitExceeded) {
            ((HttpServletResponse)response).setStatus(429);
            // Retry-After can be included in response to tell user how much user has to wait
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
