package com.example.java_ecommerce.controller;
import com.example.java_ecommerce.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/add-review")
public class AddReviewServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AddReviewServlet.class.getName());
    private final ReviewService reviewService = new ReviewService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            String productId = req.getParameter("productId");
            String username = (String) req.getAttribute("username");
            String comment = req.getParameter("comment");
            String rating = req.getParameter("rating");

            reviewService.addReview(productId, username, comment, rating);

            logger.info("Review added by: " + username);

            resp.sendRedirect("product-details?id=" + productId);

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}