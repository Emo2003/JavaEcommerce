package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SignupServlet.class.getName());
    private final AuthService authService = new AuthService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            // Validate input and create new user with hashed password
            authService.signup(username, password);
            logger.info("New user signed up: " + username);

            // Redirect to login after successful registration for authentication
            resp.sendRedirect(req.getContextPath() + "/login.jsp");

        } catch (IllegalArgumentException e) {
            // Display validation errors (duplicate username, weak password, etc.)
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);
        }
    }
}