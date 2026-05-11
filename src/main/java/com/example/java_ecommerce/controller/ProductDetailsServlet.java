package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.Product;
import com.example.java_ecommerce.services.ProductService;
import com.example.java_ecommerce.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/product-details")
public class ProductDetailsServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ProductDetailsServlet.class.getName());

    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String idParameter = req.getParameter("id");

            if (idParameter == null || idParameter.isBlank()) {
                req.setAttribute("error", "Product id is required");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
                return;
            }

            int id = Integer.parseInt(idParameter);

            Product product = productService.getProductDetails(id);

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviewService.getProductReviews(id));

            req.getRequestDispatcher("product-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid product id");
            req.getRequestDispatcher("error.jsp").forward(req, resp);

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to open product details page", e);

            req.setAttribute("error", "Could not load product details right now. Please try again later.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}