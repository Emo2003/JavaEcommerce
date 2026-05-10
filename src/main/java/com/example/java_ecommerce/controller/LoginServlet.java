package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.User;
import com.example.java_ecommerce.services.AuthService;
import com.example.java_ecommerce.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private final AuthService authService = new AuthService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            // Authenticate user against hashed password in database
            User user = authService.login(username, password);

            // Generate JWT token for stateless subsequent requests
            String token = JwtUtil.generate(user);

            // Store JWT in HttpOnly cookie to prevent XSS token theft
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            resp.addCookie(cookie);

            // Create session for immediate authenticated access without JWT verification
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            logger.info("User logged in: " + user.getUsername());
            resp.sendRedirect(req.getContextPath() + "/ProductsMain");

        } catch (IllegalArgumentException e) {
            // Display validation errors to user (invalid credentials)
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}