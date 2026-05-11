package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.Product;
import com.example.java_ecommerce.models.User;
import com.example.java_ecommerce.services.ProductService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/edit-product")
public class EditProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getAttribute("user");
            String id = request.getParameter("id");
            Product product = productService.getProductForEdit(id, user);

            request.setAttribute("product", product);

            request.getRequestDispatcher("edit_product.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException | SecurityException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}