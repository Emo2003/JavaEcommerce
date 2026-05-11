package com.example.java_ecommerce.controller;

import com.example.java_ecommerce.models.CartItem;
import com.example.java_ecommerce.models.Product;
import com.example.java_ecommerce.services.ProductService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idText = request.getParameter("id");
        int id;

        try {
            id = Integer.parseInt(idText);
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Invalid product id");
            response.sendRedirect("ProductsMain");
            return;
        }

        Product product;

        try {
            product = productService.getProductDetails(id);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect("ProductsMain");
            return;
        }

        HttpSession session = request.getSession();
        List<com.example.java_ecommerce.models.CartItem> cart = com.example.java_ecommerce.util.CartUtil.getCart(session);

        boolean exists = false;

        for (CartItem item : cart) {
            if (item.getId() == id) {
                item.increment();
                exists = true;
                break;
            }
        }

        if (!exists) {
            cart.add(new CartItem(product.getId(), product.getName(), product.getPrice()));
        }

        session.setAttribute("cart", cart);
        session.setAttribute("success",
                "🛒 Product added to cart");
        response.sendRedirect("ProductsMain");
    }
}