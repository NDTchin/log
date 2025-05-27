package com.example.order_service.dto;

import java.util.List;
import java.util.UUID;

public class OrderResponse {
    private UUID orderId;
    private Long customerId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status;

    // Constructors
    public OrderResponse() {
    }

    public OrderResponse(UUID orderId, Long customerId, List<OrderItem> items, double totalAmount, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
