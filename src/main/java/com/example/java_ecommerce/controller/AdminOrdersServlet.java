package com.radi.demo7.controller;

import com.radi.demo7.models.User;
import com.radi.demo7.services.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin-orders")
public class AdminOrdersServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User currentUser = (User) request.getAttribute("user");
            request.setAttribute("orders", orderService.getAllOrders(currentUser));

        } catch (IllegalArgumentException | SecurityException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("admin_orders.jsp")
                .forward(request, response);
    }
}