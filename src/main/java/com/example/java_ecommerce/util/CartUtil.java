package com.radi.demo7.util;

import com.radi.demo7.models.CartItem;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class CartUtil {

    public static List<CartItem> getCart(HttpSession session) {

        Object cartObj = session.getAttribute("cart");

        if (cartObj == null) {
            List<CartItem> cart = new ArrayList<>();
            session.setAttribute("cart", cart);
            return cart;
        }

        return (List<CartItem>) cartObj;
    }

    public static double getTotalPrice(List<CartItem> cart) {

        if (cart == null || cart.isEmpty()) return 0;

        double total = 0;

        for (CartItem item : cart) {
            total += item.getTotal();
        }

        return total;
    }
}