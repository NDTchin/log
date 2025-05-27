package com.example.order_service.service;

import com.example.order_service.dto.LogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public LoggingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void sendInfoLog(String message) {
        LogMessage logMessage = new LogMessage("INFO", message);
        sendToRabbitMQ(logMessage);
        logger.info(message);
    }
    
    public void sendErrorLog(String message) {
        LogMessage logMessage = new LogMessage("ERROR", message);
        sendToRabbitMQ(logMessage);
        logger.error(message);
    }
    
    private void sendToRabbitMQ(LogMessage logMessage) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, logMessage);
            logger.debug("Sent log message to RabbitMQ: {}", logMessage.getMessage());
        } catch (Exception e) {
            logger.error("Failed to send log message to RabbitMQ", e);
        }
    }
}
