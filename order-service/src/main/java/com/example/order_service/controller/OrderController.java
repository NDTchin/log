package com.example.order_service.controller;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.exception.PaymentException;
import com.example.order_service.service.LoggingService;
import com.example.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final LoggingService loggingService;

    public OrderController(OrderService orderService, LoggingService loggingService) {
        this.orderService = orderService;
        this.loggingService = loggingService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            OrderResponse orderResponse = orderService.createOrder(orderRequest);
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (PaymentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("status", "FAILED");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            UUID orderId = UUID.randomUUID();
            String errorMessage = String.format(
                "Payment failed for order ID: %s. Reason: %s",
                orderId,
                e.getMessage()
            );
            loggingService.sendErrorLog(errorMessage);
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred");
            errorResponse.put("status", "FAILED");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}