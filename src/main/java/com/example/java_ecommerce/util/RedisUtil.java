package com.example.java_ecommerce.util;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    // Redis connection details from environment; uses localhost for development
    private static final String REDIS_HOST = System.getenv().getOrDefault("REDIS_HOST", "localhost");
    private static final int REDIS_PORT = Integer.parseInt(System.getenv().getOrDefault("REDIS_PORT", "6379"));
    /**
     * Creates new Redis connection for rate limiting and product caching
     * Creates fresh connection per request; consider connection pooling for production
     * @return Jedis connection instance
     */
    public static Jedis get() {
        return new Jedis(REDIS_HOST, REDIS_PORT);
    }
}