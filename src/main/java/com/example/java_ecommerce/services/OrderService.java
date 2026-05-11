package com.radi.demo7.services;

import com.radi.demo7.dao.OrderDao;
import com.radi.demo7.models.Order;
import com.radi.demo7.models.User;

import java.util.List;

public class OrderService {

    private final OrderDao orderDao = new OrderDao();

    public List<Order> getAllOrders(User currentUser) {
        requireAdmin(currentUser);
        return orderDao.getAllOrders();
    }

    public void createOrder(String username, double total) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Invalid user for checkout");
        }

        if (total <= 0) {
            throw new IllegalArgumentException("Order total must be greater than 0");
        }

        Order order = new Order();
        order.setUsername(username.trim());
        order.setTotal(total);
        order.setStatus("PENDING");

        int createdId = orderDao.createOrder(order);

        if (createdId <= 0) {
            throw new IllegalArgumentException("Could not place order");
        }

    }

    public void updateOrderStatus(String idText, String status, User currentUser) {
        requireAdmin(currentUser);

        int id;

        try {
            id = Integer.parseInt(idText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid order id");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid order id");
        }

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Order status is required");
        }

        String cleanStatus = status.trim().toUpperCase();

        if (!"ACCEPTED".equals(cleanStatus) && !"REJECTED".equals(cleanStatus)) {
            throw new IllegalArgumentException("Invalid order status");
        }

        boolean success = orderDao.updateStatus(id, cleanStatus);

        if (!success) {
            throw new IllegalArgumentException("Order not found or update failed");
        }
    }

    private void requireAdmin(User currentUser) {
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new SecurityException("Only admin can perform this action");
        }
    }
}


