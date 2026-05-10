package com.example.java_ecommerce.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // Load database credentials from environment variables with dev defaults
    private static final String DB_HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String DB_NAME = System.getenv().getOrDefault("DB_NAME", "ecommerce");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASS = System.getenv().getOrDefault("DB_PASS", "root");

    /**
     * Creates fresh MySQL connection per request; consider connection pooling for production
     * @throws RuntimeException if connection fails
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(
                    "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME,
                    DB_USER,
                    DB_PASS
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
