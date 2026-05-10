package com.example.java_ecommerce.controller;

import com.radi.demo7.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/delete-review")
public class DeleteReviewServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteReviewServlet.class.getName());
    private final ReviewService reviewService = new ReviewService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            HttpSession session = req.getSession(false);

            // Not logged in
            if (session == null || session.getAttribute("username") == null) {
                resp.sendRedirect("login");
                return;
            }

            String role = (String) session.getAttribute("role");
            String reviewId = req.getParameter("reviewId");
            String productId = req.getParameter("productId");

            reviewService.deleteReview(reviewId, role);

            logger.info("Review " + reviewId + " deleted by admin");
            session.setAttribute("success", "Review deleted successfully!");

            resp.sendRedirect("product-details?id=" + productId);


        } catch (SecurityException e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}