package com.radi.demo7.controller;

import com.radi.demo7.models.CartItem;
import com.radi.demo7.util.CartUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/remove-from-cart")
public class RemoveFromCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idText = request.getParameter("id");
        int id;

        try {
            id = Integer.parseInt(idText);
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Invalid cart item id");
            response.sendRedirect("Cart.jsp");
            return;
        }

        HttpSession session = request.getSession();

        List<CartItem> cart = CartUtil.getCart(session);
        cart.removeIf(item -> item.getId() == id);

        session.setAttribute("cart", cart);
        session = request.getSession();
        session.setAttribute("success", "Product deleted successfully!");

        response.sendRedirect("Cart.jsp");
    }
}