package com.lamiel.store;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Applies rate limits per visitor IP so bots/scripts can't spam the site.
 * - Admin login: only 5 attempts per 15 minutes (blocks password guessing)
 * - Add to cart: only 10 per minute (blocks cart-spam bots)
 * - Everything else: 60 requests per minute (general anti-spam ceiling)
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiter loginLimiter = new RateLimiter(5, 15 * 60_000L);
    private final RateLimiter cartLimiter = new RateLimiter(10, 60_000L);
    private final RateLimiter generalLimiter = new RateLimiter(60, 60_000L);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = getClientIp(request);
        String path = request.getRequestURI();
        String method = request.getMethod();

        RateLimiter limiter;
        if (path.equals("/admin/login") && method.equalsIgnoreCase("POST")) {
            limiter = loginLimiter;
        } else if (path.equals("/cart/add") && method.equalsIgnoreCase("POST")) {
            limiter = cartLimiter;
        } else {
            limiter = generalLimiter;
        }

        if (!limiter.isAllowed(ip)) {
            response.setStatus(429); // 429 Too Many Requests
            response.setContentType("text/plain");
            response.getWriter().write("Too many requests. Please slow down and try again in a bit.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Render (and most cloud hosts) sit behind a proxy, so the real visitor IP
    // is in the X-Forwarded-For header rather than the raw connection address.
    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
