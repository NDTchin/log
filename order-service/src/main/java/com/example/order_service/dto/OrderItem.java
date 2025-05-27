package com.example.order_service.dto;

public class OrderItem {
    private Long productId;
    private int quantity;
    private double price;

    // Constructors
    public OrderItem() {
    }

    public OrderItem(Long productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    // Helper method to calculate total price for this item
    public double getTotalPrice() {
        return quantity * price;
    }
}
