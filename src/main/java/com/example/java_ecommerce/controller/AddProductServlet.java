package com.example.java_ecommerce.controller;

import com.radi.demo7.models.User;
import com.radi.demo7.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/add-product")
public class AddProductServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AddProductServlet.class.getName());
    private final ProductService productService = new ProductService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            User user = (User) req.getAttribute("user");

            String name = req.getParameter("name");
            String price = req.getParameter("price");
            String description = req.getParameter("description");

            productService.addProduct(name, price, description, user);

            logger.info("Product added by: " + user.getUsername());

            resp.sendRedirect("ProductsMain");

        } catch (IllegalArgumentException | SecurityException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }}