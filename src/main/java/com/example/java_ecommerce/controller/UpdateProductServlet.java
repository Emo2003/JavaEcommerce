package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.User;
import com.example.java_ecommerce.services.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/update-product")
public class UpdateProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException, ServletException {

        try {
            User user = (User) request.getAttribute("user");
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String priceText = request.getParameter("price");
            String description = request.getParameter("description");

            // Update product; authorization and validation enforced in service
            // Cache invalidated after successful update to reflect changes
            productService.updateProduct(id, name, priceText, description, user);

            HttpSession session = request.getSession();
            session.setAttribute("success", "Product updated successfully!");

            response.sendRedirect("ProductsMain");

        } catch (IllegalArgumentException | SecurityException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}