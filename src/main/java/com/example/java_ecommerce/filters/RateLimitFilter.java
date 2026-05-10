package com.example.java_ecommerce.filters;

import com.example.java_ecommerce.util.RedisUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.logging.Logger;

@WebFilter("/*")
public class RateLimitFilter implements Filter {

    private static final Logger logger = Logger.getLogger(RateLimitFilter.class.getName());

    // Allow 30 requests per 10-second window per user/IP
    private static final int MAX_REQUESTS = 30;
    private static final int WINDOW_SECONDS = 10;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        // Bypass rate limiting for static resources to avoid unnecessary Redis calls
        if (isStaticResource(path)) {
            chain.doFilter(req, res);
            return;
        }

        String ip = request.getRemoteAddr();
        HttpSession session = request.getSession(false);
        Object username = session == null ? null : session.getAttribute("username");
        // Use username if authenticated, fallback to IP to rate limit per user or anonymous visitor
        String subject = username == null ? ("ip:" + ip) : ("user:" + username);
        String key = "rate:" + subject;

        try (Jedis jedis = RedisUtil.get()) {
            // Increment counter in 10-second sliding window; Redis ensures atomic operations
            long count = jedis.incr(key);

            // Set TTL on first request within window; simplistic sliding window implementation
            if (count == 1) {
                jedis.expire(key, WINDOW_SECONDS);
            }

            if (count > MAX_REQUESTS) {
                logger.warning("Rate limit exceeded for subject: " + subject);

                // Return 429 (Too Many Requests) to indicate rate limiting
                response.setStatus(429);
                response.setContentType("text/plain");
                response.getWriter().println("Too many requests. Please try again later.");
                return;
            }

        } catch (Exception e) {
            // Graceful degradation: allow request if Redis fails; fallback to no rate limiting
            logger.warning("Redis rate limit failed: " + e.getMessage());
        }

        chain.doFilter(req, res);
    }

    private boolean isStaticResource(String path) {
        return path.contains("/css/") ||
                path.contains("/js/") ||
                path.contains("/images/");
    }
}