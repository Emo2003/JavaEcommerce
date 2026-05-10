package com.example.java_ecommerce.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtil {

    /**
     * Hashes password using SHA-256; migration to bcrypt recommended for production
     * SHA-256 is deterministic, enabling password verification and legacy migration
     */
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compares raw password against stored hash; supports legacy plaintext passwords during migration
     */
    public static boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }

        String cleanRawPassword = rawPassword.trim();
        String cleanStoredPassword = storedPassword.trim();

        // Detect if stored password is already hashed; if not, upgrade it on successful login
        if (isHashed(cleanStoredPassword)) {
            return hash(cleanRawPassword).equalsIgnoreCase(cleanStoredPassword);
        }

        return cleanRawPassword.equals(cleanStoredPassword);
    }

    /**
     * Identifies SHA-256 hashes by pattern; distinguishes hashed from plaintext passwords
     */
    public static boolean isHashed(String password) {
        if (password == null) {
            return false;
        }

        return password.trim().matches("^[a-fA-F0-9]{64}$");
    }
}