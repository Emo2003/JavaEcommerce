package com.example.java_ecommerce.models;

public class CartItem {

    private int id;
    private String name;
    private double price;
    private int quantity;

    public CartItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = 1;
    }

    public double getTotal() {
        return price * quantity;
    }

    public void increment() {
        quantity++;
    }

    public void decrement() {
        if (quantity > 1) {
            quantity--;
        }
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}