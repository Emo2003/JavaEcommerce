package com.example.java_ecommerce.services;



import com.example.java_ecommerce.dao.ReviewDao;
import com.example.java_ecommerce.models.Review;

import java.util.List;

public class ReviewService {

    public void addReview(String productIdText, String username, String comment, String ratingText) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("User is required");
        }

        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("Review comment is required");
        }

        int productId;
        int rating;

        try {
            productId = Integer.parseInt(productIdText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid product id");
        }

        try {
            rating = Integer.parseInt(ratingText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid rating");
        }

        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product id");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = new Review();

        review.setProductId(productId);
        review.setUsername(username);
        review.setComment(comment.trim());
        review.setRating(rating);

        ReviewDao.add(review);
    }
    public void deleteReview(String reviewIdText, String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new SecurityException("Only admins can delete reviews");
        }

        int reviewId;
        try {
            reviewId = Integer.parseInt(reviewIdText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid review id");
        }

        if (reviewId <= 0) {
            throw new IllegalArgumentException("Invalid review id");
        }

        ReviewDao.delete(reviewId);
    }

    public List<Review> getProductReviews(int productId) {
        return ReviewDao.findByProductId(productId);
    }
}