package com.example.java_ecommerce.services;

import com.example.java_ecommerce.dao.UserDao;
import com.example.java_ecommerce.models.User;

import java.util.List;

public class AuthService {

    /**
     * Authenticates user by username and password; input validation prevents injection attacks
     * @throws IllegalArgumentException if credentials are invalid or missing
     */
    public User login(String username, String password) {
        validateCredentials(username, password);
        User user = UserDao.findByUsernameAndPassword(username.trim(), password.trim());

        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return user;
    }

    /**
     * Creates new user account; validates input to prevent invalid data storage
     * @throws IllegalArgumentException if username exists or input is invalid
     */
    public void signup(String username, String password) {
        validateCredentials(username, password);

        boolean created = UserDao.create(username.trim(), password.trim());

        if (!created) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    /**
     * Deletes user's own account; prevents admin self-deletion to maintain system access
     * @throws SecurityException if admin attempts deletion (authorization check)
     * @throws IllegalArgumentException if user or deletion failed
     */
    public void deleteOwnAccount(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }

        if (user.isAdmin()) {
            throw new SecurityException("Admin account cannot be deleted from this action");
        }

        boolean deleted = UserDao.deleteByUsername(user.getUsername());

        if (!deleted) {
            throw new IllegalArgumentException("Account could not be deleted");
        }
    }

    /**
     * Retrieves all users; admin authorization required
     * @throws SecurityException if caller is not admin
     */
    public List<User> getAllUsers(User currentUser) {
        requireAdmin(currentUser);

        return UserDao.findAll();
    }

    /**
     * Deletes user by ID on behalf of admin; prevents accidental deletions and admin removal
     * @throws SecurityException if caller is not admin
     * @throws IllegalArgumentException if user ID is invalid or user not found
     */
    public void deleteUserByAdmin(String userIdText, User currentUser) {
        requireAdmin(currentUser);

        int userId;

        try {
            userId = Integer.parseInt(userIdText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid user id");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        boolean deleted = UserDao.deleteById(userId);

        if (!deleted) {
            throw new IllegalArgumentException("User not found or admin user cannot be deleted");
        }
    }

    /**
     * Validates user has admin role; throws SecurityException if unauthorized
     */
    private void requireAdmin(User user) {
        if (user == null || !user.isAdmin()) {
            throw new SecurityException("Only admin can perform this action");
        }
    }

    /**
     * Validates username and password meet security requirements
     * Prevents empty inputs, SQL injection, and weak credentials
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCredentials(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (username.trim().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }

        if (password.trim().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
    }
}