package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.User;
import com.example.java_ecommerce.services.AuthService;
import com.example.java_ecommerce.services.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/ProductsMain")
public class ProductsMain extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final AuthService authService = new AuthService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getAttribute("user");
        String search = req.getParameter("search");
        int page = parsePositiveInt(req.getParameter("page"), 1);
        int pageSize = 8;

        req.setAttribute("username", user.getUsername());
        req.setAttribute("role", user.getRole());
        req.setAttribute("isAdmin", user.isAdmin());

        if (user.isAdmin()) {
            req.setAttribute("users", authService.getAllUsers(user));
        }

        if (search != null && !search.trim().isEmpty()) {
            String cleanSearch = search.trim();
            int totalItems = productService.getSearchProductsCount(cleanSearch);
            int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));
            int safePage = Math.min(page, totalPages);

            req.setAttribute("data", productService.searchProductsPage(cleanSearch, safePage, pageSize));
            req.setAttribute("search", cleanSearch);
            req.setAttribute("currentPage", safePage);
            req.setAttribute("totalPages", totalPages);

        } else {
            int totalItems = productService.getProductsCount();
            int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));
            int safePage = Math.min(page, totalPages);

            req.setAttribute("data", productService.getProductsPage(safePage, pageSize));
            req.setAttribute("currentPage", safePage);
            req.setAttribute("totalPages", totalPages);
        }

        req.getRequestDispatcher("display-products.jsp")
                .forward(req, resp);
    }

    private int parsePositiveInt(String value, int defaultValue) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}