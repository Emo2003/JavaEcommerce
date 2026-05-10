package com.example.java_ecommerce.filters;

import com.example.java_ecommerce.dao.UserDao;
import com.example.java_ecommerce.models.User;
import com.example.java_ecommerce.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        // Allow unauthenticated access to login, signup, and public resources
        if (isPublicPath(path)) {
            chain.doFilter(req, res);
            return;
        }

        // Check for existing session first to avoid unnecessary JWT verification
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") instanceof User sessionUser) {
            // Authorization: verify user is not attempting unauthorized admin operations
            if (isAdminPath(path) && !sessionUser.isAdmin()) {
                req.setAttribute("error", "Only admin can access this endpoint");
                req.getRequestDispatcher("/error.jsp").forward(req, res);
                return;
            }

            // Set user context for downstream servlets
            request.setAttribute("user", sessionUser);
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("role", sessionUser.getRole());

            chain.doFilter(req, res);
            return;
        }

        // Attempt stateless authentication via JWT token from cookie
        String token = getTokenFromCookies(request);

        if (token == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Verify JWT signature and claims; returns null if invalid or expired
        String username = JwtUtil.verify(token);

        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Ensure verified user still exists in database; handles deleted accounts
        User user = UserDao.findByUsername(username);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Authorization: verify user is not attempting unauthorized admin operations
        if (isAdminPath(path) && !user.isAdmin()) {
            req.setAttribute("error", "Only admin can access this endpoint");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
            return;
        }

        // Establish session from JWT token for subsequent requests; reduces JWT verification overhead
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("user", user);
        newSession.setAttribute("username", user.getUsername());
        newSession.setAttribute("role", user.getRole());

        // Set request attributes for current request handlers
        request.setAttribute("user", user);
        request.setAttribute("username", user.getUsername());
        request.setAttribute("role", user.getRole());

        chain.doFilter(req, res);
    }

    /**
     * Identifies publicly accessible paths; unauthenticated users can only access these
     */
    private boolean isPublicPath(String path) {
        return path.endsWith("login.jsp") ||
                path.endsWith("signup.jsp") ||
                path.endsWith("error.jsp") ||
                path.endsWith("/login") ||
                path.endsWith("/signup") ||
                path.contains("/css/") ||
                path.contains("/js/") ||
                path.contains("/images/");
    }

    /**
     * Identifies admin-only protected paths; authorization enforced for these endpoints
     */
    private boolean isAdminPath(String path) {
        return path.endsWith("/add-product") ||
                path.endsWith("/delete-product") ||
                path.endsWith("/edit-product") ||
                path.endsWith("/update-product") ||
                path.endsWith("/admin-orders") ||
                path.endsWith("/update-order") ||
                path.contains("/admin/") ||
                path.endsWith("/delete-review");
    }

    /**
     * Extracts JWT token from cookies; typically set by LoginServlet as HttpOnly for security
     */
    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie c : cookies) {
            if ("token".equals(c.getName())) {
                return c.getValue();
            }
        }

        return null;
    }
}