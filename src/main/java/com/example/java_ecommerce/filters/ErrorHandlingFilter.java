package com.example.java_ecommerce.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/*")
public class ErrorHandlingFilter implements Filter {

    private static final Logger logger = Logger.getLogger(ErrorHandlingFilter.class.getName());

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        try {
            chain.doFilter(req, res);
        } catch (IllegalArgumentException | SecurityException e) {
            // Handle validation and authorization errors with user-friendly messages
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);

        } catch (Exception e) {
            HttpServletRequest request = (HttpServletRequest) req;

            // Log unhandled errors for debugging; avoid exposing details to users
            logger.log(Level.SEVERE, "Unhandled error on path: " + request.getRequestURI(), e);

            // Show generic error message to prevent information leakage
            req.setAttribute("error", "Something went wrong. Please try again later.");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}