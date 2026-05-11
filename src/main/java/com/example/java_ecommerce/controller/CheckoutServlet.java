package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.CartItem;
import com.example.java_ecommerce.services.OrderService;
import com.example.java_ecommerce.util.CartUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        List<CartItem> cart = CartUtil.getCart(session);

        String username = (String) session.getAttribute("username");

        if (username == null || username.isBlank()) {
            session.setAttribute("error", "You must be logged in to checkout");
            response.sendRedirect("login.jsp");
            return;
        }

        if (cart.isEmpty()) {
            session.setAttribute("error", "Cart is empty");
            response.sendRedirect("Cart.jsp");
            return;
        }

        double total = CartUtil.getTotalPrice(cart);

        try {
            orderService.createOrder(username, total);
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect("Cart.jsp");
            return;
        }

        // clear cart properly
        session.removeAttribute("cart");

        session.setAttribute("success", "Order placed successfully");

        response.sendRedirect("Cart.jsp");
    }
}