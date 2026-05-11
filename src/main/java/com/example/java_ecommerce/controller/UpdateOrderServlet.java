package com.radi.demo7.controller;

import com.radi.demo7.models.User;
import com.radi.demo7.services.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/update-order")
public class UpdateOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User currentUser = (User) request.getAttribute("user");
            String id = request.getParameter("id");
            String status = request.getParameter("status");

            orderService.updateOrderStatus(id, status, currentUser);
            request.getSession().setAttribute("success", "Order #" + id + " updated successfully!");

            response.sendRedirect("admin-orders");

        } catch (IllegalArgumentException | SecurityException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}