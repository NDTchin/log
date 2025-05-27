package com.example.order_service.service;

import com.example.order_service.dto.OrderItem;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.exception.PaymentException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    
    private final LoggingService loggingService;
    
    public OrderService(LoggingService loggingService) {
        this.loggingService = loggingService;
    }
    
    public OrderResponse createOrder(OrderRequest orderRequest) {
        UUID orderId = UUID.randomUUID();
        double totalAmount = calculateTotalAmount(orderRequest);
        
        // Validate payment method
        validatePayment(orderId, orderRequest);
        
        // Create successful order response
        OrderResponse response = new OrderResponse(
            orderId,
            orderRequest.getCustomerId(),
            orderRequest.getItems(),
            totalAmount,
            "CREATED"
        );
        
        // Log success message
        String successMessage = String.format(
            "Order created successfully. Order ID: %s, Customer ID: %d, Total amount: %.2f",
            orderId,
            orderRequest.getCustomerId(),
            totalAmount
        );
        loggingService.sendInfoLog(successMessage);
        
        return response;
    }
    
    private double calculateTotalAmount(OrderRequest orderRequest) {
        return orderRequest.getItems().stream()
            .mapToDouble(OrderItem::getTotalPrice)
            .sum();
    }
    
    private void validatePayment(UUID orderId, OrderRequest orderRequest) {
        String paymentMethod = orderRequest.getPaymentMethod();
        
        // Validate payment method
        if (!"creditCard".equals(paymentMethod)) {
            String errorMessage = String.format(
                "Payment failed for order ID: %s. Reason: Unsupported payment method: %s",
                orderId,
                paymentMethod
            );
            loggingService.sendErrorLog(errorMessage);
            throw new PaymentException("Unsupported payment method: " + paymentMethod);
        }
        
        // Validate credit card
        String creditCardNumber = orderRequest.getCreditCardNumber();
        if (creditCardNumber == null || creditCardNumber.replaceAll("[^0-9]", "").length() != 16) {
            String errorMessage = String.format(
                "Payment failed for order ID: %s. Reason: Invalid credit card number",
                orderId
            );
            loggingService.sendErrorLog(errorMessage);
            throw new PaymentException("Invalid credit card number");
        }
    }
}
