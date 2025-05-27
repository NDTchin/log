package com.example.log_processing_service.service;

import com.example.log_processing_service.dto.LogMessage;
import com.example.log_processing_service.entity.LogEntity;
import com.example.log_processing_service.handler.LogErrorHandler;
import com.example.log_processing_service.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LogProcessingService {
    
    private static final Logger logger = LoggerFactory.getLogger(LogProcessingService.class);
    private final LogRepository logRepository;
    private final LogErrorHandler errorHandler;
    
    public LogProcessingService(LogRepository logRepository, LogErrorHandler errorHandler) {
        this.logRepository = logRepository;
        this.errorHandler = errorHandler;
    }
    
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processLogMessage(LogMessage logMessage) {
        if (logMessage == null) {
            logger.warn("Nhận được log message rỗng");
            return;
        }
        
        logger.info("Nhận được log message: {}", logMessage);
        
        LocalDateTime timestamp = null;
        
        try {
            // Parse timestamp
            timestamp = LocalDateTime.parse(
                logMessage.getTimestamp(), 
                DateTimeFormatter.ISO_DATE_TIME
            );
        } catch (Exception e) {
            timestamp = LocalDateTime.now();
            errorHandler.handleProcessingError(e, "Không thể chuyển đổi timestamp");
        }
        
        try {
            // Lưu vào cơ sở dữ liệu
            LogEntity logEntity = new LogEntity(
                logMessage.getLevel(),
                logMessage.getMessage(),
                timestamp,
                logMessage.getServiceName()
            );
            
            logRepository.save(logEntity);
            logger.debug("Đã lưu log vào cơ sở dữ liệu");
            
            // Kiểm tra nếu là ERROR log thì gửi thông báo
            if ("ERROR".equalsIgnoreCase(logMessage.getLevel())) {
                notifyTechTeam(logMessage.getMessage());
            }
        } catch (Exception e) {
            errorHandler.handleDatabaseError(e, "Không thể lưu log vào database");
        }
    }
    
    private void notifyTechTeam(String errorMessage) {
        // Trong ứng dụng thực tế, đây sẽ là nơi gửi email, SMS hoặc thông báo
        String notification = "Error log detected. Sending notification to tech team: " + errorMessage;
        System.out.println(notification);
        logger.warn(notification);
    }
}