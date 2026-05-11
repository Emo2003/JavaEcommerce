package com.example.java_ecommerce.dao;


import com.example.java_ecommerce.models.Order;
import com.example.java_ecommerce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDao {

    private static final Logger logger = Logger.getLogger(OrderDao.class.getName());

    public List<Order> getAllOrders() {

        List<Order> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM orders ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUsername(rs.getString("username"));
                o.setTotal(rs.getDouble("total"));
                o.setStatus(rs.getString("status"));

                list.add(o);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load orders", e);
            throw new RuntimeException("Failed to load orders", e);
        }

        return list;
    }

    public int createOrder(Order order) {

        int id = -1;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO orders(username, total, status) VALUES(?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, order.getUsername());
            ps.setDouble(2, order.getTotal());
            ps.setString(3, order.getStatus());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create order", e);
            throw new RuntimeException("Failed to create order", e);
        }

        return id;
    }

    public boolean updateStatus(int id, String status) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE orders SET status=? WHERE id=?")) {

            ps.setString(1, status);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update order status", e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }
}
