package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.User;
import com.example.java_ecommerce.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/delete-product")
public class DeleteProductServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteProductServlet.class.getName());
    private final ProductService productService = new ProductService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            User user = (User) req.getAttribute("user");
            String id = req.getParameter("id");

            productService.deleteProduct(id, user);

            logger.info("Product deleted by: " + user.getUsername());

            // set session feedback message for UI
            HttpSession session = req.getSession();
            session.setAttribute("success", "Product deleted successfully!");

            resp.sendRedirect("ProductsMain");

        } catch (IllegalArgumentException | SecurityException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}