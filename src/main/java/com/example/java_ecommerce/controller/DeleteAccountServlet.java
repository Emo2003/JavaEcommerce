package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.User;
import com.example.java_ecommerce.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/delete-account")
public class DeleteAccountServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteAccountServlet.class.getName());
    private final AuthService authService = new AuthService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            User user = (User) req.getAttribute("user");

            // Delete current user account from database; prevents admin self-deletion
            authService.deleteOwnAccount(user);

            // Clear authentication token to invalidate JWT
            Cookie cookie = new Cookie("token", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");

            resp.addCookie(cookie);

            // Invalidate session to log user out immediately
            HttpSession session = req.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            logger.info("Account deleted: " + user.getUsername());

            resp.sendRedirect(req.getContextPath() + "/login.jsp");

        } catch (IllegalArgumentException | SecurityException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}