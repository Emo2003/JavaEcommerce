package com.example.java_ecommerce.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.java_ecommerce.models.User;

import java.util.Date;

public class JwtUtil {

    // Load JWT secret from environment; use default for development only
    private static final String SECRET = System.getenv().getOrDefault("JWT_SECRET", "change_this_dev_secret");
    // 1-hour token expiration for session persistence while limiting token lifetime
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    /**
     * Generates JWT token with user role for authorization checks without database queries
     */
    public static String generate(User user) {
        return JWT.create()
                .withClaim("user", user.getUsername())
                .withClaim("role", user.getRole())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    /**
     * Verifies token signature and expiration; returns null if invalid to prevent unauthorized access
     */
    public static String verify(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);

            return jwt.getClaim("user").asString();

        } catch (JWTVerificationException e) {
            // Token tampered or expired - return null to trigger re-authentication
            return null;
        }
    }
}