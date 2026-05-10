package com.example.java_ecommerce.dao;

import com.radi.demo7.models.Review;
import com.radi.demo7.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {

    public static void add(Review review) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO reviews(product_id, username, comment, rating) VALUES (?, ?, ?, ?)")) {

            ps.setInt(1, review.getProductId());
            ps.setString(2, review.getUsername());
            ps.setString(3, review.getComment());
            ps.setInt(4, review.getRating());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to add review", e);
        }
    }
    public static void delete(int reviewId) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM reviews WHERE id = ?")) {

            ps.setInt(1, reviewId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete review", e);
        }
    }

    public static List<Review> findByProductId(int productId) {
        List<Review> reviews = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM reviews WHERE product_id=? ORDER BY created_at DESC")) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();

                    review.setId(rs.getInt("id"));
                    review.setProductId(rs.getInt("product_id"));
                    review.setUsername(rs.getString("username"));
                    review.setComment(rs.getString("comment"));
                    review.setRating(rs.getInt("rating"));
                    review.setCreatedAt(rs.getTimestamp("created_at"));

                    reviews.add(review);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load reviews", e);
        }

        return reviews;
    }
}