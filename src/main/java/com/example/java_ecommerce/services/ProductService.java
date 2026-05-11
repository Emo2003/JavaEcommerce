package com.example.java_ecommerce.services;



import com.example.java_ecommerce.dao.ProductDao;
import com.example.java_ecommerce.models.Product;
import com.example.java_ecommerce.models.User;

import java.util.List;

public class ProductService {

    public List<Product> getProductsPage(int page, int pageSize) {
        return ProductDao.getPage(page, pageSize);
    }

    public List<Product> searchProductsPage(String keyword, int page, int pageSize) {
        if (keyword == null || keyword.isBlank()) {
            return ProductDao.getPage(page, pageSize);
        }

        return ProductDao.searchPage(keyword.trim(), page, pageSize);
    }

    public int getProductsCount() {
        return ProductDao.countAll();
    }

    public int getSearchProductsCount(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return ProductDao.countAll();
        }

        return ProductDao.countSearch(keyword.trim());
    }

    public Product getProductDetails(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product id");
        }

        Product product = ProductDao.findById(id);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        return product;
    }

    public void addProduct(String name, String priceText, String description, User user) {
        if (user == null) {
            throw new SecurityException("Authentication is required");
        }

        if (!user.isAdmin()) {
            throw new SecurityException("Only admins can add products");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid price");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        Product p = new Product();
        p.setName(name.trim());
        p.setPrice(price);
        p.setDescription(description.trim());

        ProductDao.add(p);
    }

    public Product getProductForEdit(String idText, User user) {
        requireAdmin(user);

        int id;

        try {
            id = Integer.parseInt(idText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid product id");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product id");
        }

        Product product = ProductDao.findById(id);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        return product;
    }

    public void updateProduct(String idText, String name, String priceText, String description, User user) {
        requireAdmin(user);

        int id;
        double price;

        try {
            id = Integer.parseInt(idText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid product id");
        }

        try {
            price = Double.parseDouble(priceText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid price");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product id");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        ProductDao.update(id, name.trim(), price, description.trim());
    }

    public void deleteProduct(String idText, User user) {
        requireAdmin(user);

        int id;

        try {
            id = Integer.parseInt(idText);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid product id");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product id");
        }

        ProductDao.delete(id);
    }

    private void requireAdmin(User user) {
        if (user == null || !user.isAdmin()) {
            throw new SecurityException("Only admin can perform this action");
        }
    }
}