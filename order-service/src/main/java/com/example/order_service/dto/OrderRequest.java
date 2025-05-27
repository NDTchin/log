package com.example.order_service.dto;

import java.util.List;

public class OrderRequest {
    private Long customerId;
    private List<OrderItem> items;
    private String paymentMethod;
    private String creditCardNumber;

    // Constructors
    public OrderRequest() {
    }

    public OrderRequest(Long customerId, List<OrderItem> items, String paymentMethod, String creditCardNumber) {
        this.customerId = customerId;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.creditCardNumber = creditCardNumber;
    }

    // Getters and Setters
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}