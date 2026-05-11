package com.example.java_ecommerce.dao;

import com.example.java_ecommerce.models.Product;
import com.example.java_ecommerce.util.DBConnection;
import com.example.java_ecommerce.util.RedisUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDao {

    private static final Logger logger = Logger.getLogger(ProductDao.class.getName());
    private static final String PRODUCTS_CACHE_KEY = "products";
    private static final int CACHE_SECONDS = 60;

    public static List<Product> getAll() {

        Gson gson = new Gson();

        // 1. Try Redis
        try (Jedis redis = RedisUtil.get()) {
            String cached = redis.get(PRODUCTS_CACHE_KEY);

            if (cached != null && !cached.isEmpty()) {
                return gson.fromJson(cached,
                        new TypeToken<List<Product>>() {}.getType());
            }
        } catch (Exception e) {
            logger.warning("Redis read failed: " + e.getMessage());
        }

        // 2. DB
        List<Product> list = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM products ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load products", e);
        }

        // 3. Cache
        try (Jedis redis = RedisUtil.get()) {
            redis.setex(PRODUCTS_CACHE_KEY, 60, gson.toJson(list));
        } catch (Exception e) {
            logger.warning("Redis write failed: " + e.getMessage());
        }

        return list;
    }

    public static Product findById(int id) {
        Gson gson = new Gson();
        String detailsKey = productDetailsKey(id);

        try (Jedis redis = RedisUtil.get()) {
            String cached = redis.get(detailsKey);

            if (cached != null && !cached.isEmpty()) {
                return gson.fromJson(cached, Product.class);
            }
        } catch (Exception e) {
            logger.warning("Redis read failed: " + e.getMessage());
        }

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM products WHERE id=?")) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = mapProduct(rs);

                    try (Jedis redis = RedisUtil.get()) {
                        redis.setex(detailsKey, CACHE_SECONDS, gson.toJson(product));
                    } catch (Exception e) {
                        logger.warning("Redis write failed: " + e.getMessage());
                    }

                    return product;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load product", e);
        }

        return null;
    }

    public static List<Product> getPage(int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safePageSize;

        List<Product> list = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM products ORDER BY id DESC LIMIT ? OFFSET ?")) {

            ps.setInt(1, safePageSize);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load paged products", e);
        }

        return list;
    }

    public static List<Product> searchPage(String keyword, int page, int pageSize) {
        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePage - 1) * safePageSize;

        List<Product> list = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM products WHERE name LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?")) {

            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, safePageSize);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to search paged products", e);
        }

        return list;
    }

    public static int countAll() {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM products");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException("Failed to count products", e);
        }
    }

    public static int countSearch(String keyword) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM products WHERE name LIKE ?")) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return 0;

        } catch (Exception e) {
            throw new RuntimeException("Failed to count searched products", e);
        }
    }


    public static void add(Product p) {

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO products(name, price, description) VALUES (?, ?, ?)")) {

            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setString(3, p.getDescription());

            ps.executeUpdate();
            clearAllProductCache();

        } catch (Exception e) {
            throw new RuntimeException("Add product failed", e);
        }
    }

    public static void delete(int id) {

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM products WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            clearProductCacheById(id);

        } catch (Exception e) {
            throw new RuntimeException("Delete failed", e);
        }
    }

    public static void update(int id, String name, double price, String desc) {

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE products SET name=?, price=?, description=? WHERE id=?")) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setString(3, desc);
            ps.setInt(4, id);

            ps.executeUpdate();
            clearProductCacheById(id);

        } catch (Exception e) {
            throw new RuntimeException("Update failed", e);
        }
    }

    private static Product mapProduct(ResultSet rs) throws SQLException {

        Product p = new Product();

        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setPrice(rs.getDouble("price"));
        p.setDescription(rs.getString("description"));

        return p;
    }

    private static void clearAllProductCache() {

        try (Jedis redis = RedisUtil.get()) {
            redis.del(PRODUCTS_CACHE_KEY);

            for (String key : redis.keys("product:*")) {
                redis.del(key);
            }
        } catch (Exception e) {
            logger.warning("Cache clear failed: " + e.getMessage());
        }
    }

    private static void clearProductCacheById(int id) {
        try (Jedis redis = RedisUtil.get()) {
            redis.del(PRODUCTS_CACHE_KEY);
            redis.del(productDetailsKey(id));
        } catch (Exception e) {
            logger.warning("Cache clear failed: " + e.getMessage());
        }
    }

    private static String productDetailsKey(int id) {
        return "product:" + id;
    }
}