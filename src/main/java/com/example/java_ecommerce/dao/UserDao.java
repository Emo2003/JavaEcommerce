package com.example.java_ecommerce.dao;

import com.example.java_ecommerce.models.User;
import com.example.java_ecommerce.util.DBConnection;
import com.example.java_ecommerce.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    /**
     * Authenticates user by username and password; uses prepared statements to prevent SQL injection
     * Automatically migrates plaintext passwords to hashed format on successful login
     * @return User if credentials valid, null otherwise
     */
    public static User findByUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String cleanUsername = username.trim();
        String cleanPassword = password.trim();

        try (Connection c = DBConnection.getConnection();
             // Uses TRIM in query to handle accidental whitespace in username column
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM users WHERE TRIM(username)=? ORDER BY id ASC")) {

            ps.setString(1, cleanUsername);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String storedPassword = rs.getString("password");

                    // Compare raw input against stored hash or plaintext (legacy support)
                    if (PasswordUtil.matches(cleanPassword, storedPassword)) {
                        User user = mapUser(rs);

                        // Automatic password migration: hash plaintext passwords on first successful login
                        if (!PasswordUtil.isHashed(storedPassword)) {
                            updatePassword(user.getId(), PasswordUtil.hash(cleanPassword));
                        }

                        return user;
                    }
                }
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Failed to validate user", e);
        }
    }

    /**
     * Retrieves user by username; returns null if not found
     * Used after JWT verification to ensure user still exists
     */
    public static User findByUsername(String username) {
        if (username == null) {
            return null;
        }

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM users WHERE TRIM(username)=? ORDER BY id ASC LIMIT 1")) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Failed to find user", e);
        }
    }

    /**
     * Loads all users; admin only via authorization check in service layer
     */
    public static List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users ORDER BY id ASC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load users", e);
        }

        return users;
    }


    public static boolean create(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO users(username, password, role) VALUES (?, ?, ?)")) {

            ps.setString(1, username.trim());
            // Hash password before storage to prevent plaintext exposure
            ps.setString(2, PasswordUtil.hash(password));
            ps.setString(3, "USER");

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Silently return false on duplicate username (unique constraint violation)
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    /**
     * Deletes user by username; prevents admin deletion as safeguard
     * Authorization check performed by service layer before calling this
     * @return true if user deleted, false otherwise
     */
    public static boolean deleteByUsername(String username) {
        if (username == null) {
            return false;
        }

        try (Connection c = DBConnection.getConnection();
             // Database-level protection: only delete non-admin users
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM users WHERE TRIM(username)=? AND UPPER(role) <> 'ADMIN'")) {

            ps.setString(1, username.trim());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Deletes user by ID; admin protection enforced at database level
     * Used by admin to delete other users
     * @return true if user deleted, false otherwise
     */
    public static boolean deleteById(int id) {
        try (Connection c = DBConnection.getConnection();
             // Database-level protection: only delete non-admin users
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM users WHERE id=? AND UPPER(role) <> 'ADMIN'")) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Internal: updates user password after hashing; used for password migrations
     */
    private static void updatePassword(int userId, String hashedPassword) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE users SET password=? WHERE id=?")) {

            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update password", e);
        }
    }

    /**
     * Internal: maps ResultSet row to User object; intentionally excludes password
     */
    private static User mapUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(null);  // Never expose password outside DAO
        user.setRole(rs.getString("role"));

        return user;
    }
}