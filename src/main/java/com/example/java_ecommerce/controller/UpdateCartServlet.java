package com.radi.demo7.controller;

import com.radi.demo7.models.CartItem;
import com.radi.demo7.util.CartUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/update-cart")
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idText = request.getParameter("id");
        String action = request.getParameter("action");

        int id;

        try {
            id = Integer.parseInt(idText);
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Invalid cart item id");
            response.sendRedirect("Cart.jsp");
            return;
        }

        if (!"inc".equals(action) && !"dec".equals(action)) {
            request.getSession().setAttribute("error", "Invalid cart action");
            response.sendRedirect("Cart.jsp");
            return;
        }

        HttpSession session = request.getSession();

        List<CartItem> cart = CartUtil.getCart(session);
        for (CartItem item : cart) {
            if (item.getId() == id) {

                if ("inc".equals(action)) {
                    item.increment();
                } else {
                    if (item.getQuantity() > 1) {
                        item.decrement();
                    }
                }

                break;
            }
        }

        session.setAttribute("cart", cart);

        response.sendRedirect("Cart.jsp");
    }
}