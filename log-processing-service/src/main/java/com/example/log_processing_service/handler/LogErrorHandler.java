package com.example.log_processing_service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogErrorHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(LogErrorHandler.class);
    
    public void handleProcessingError(Exception e, String messageInfo) {
        logger.error("Lỗi xử lý message: {} - Chi tiết: {}", messageInfo, e.getMessage());
        
    }
    
    public void handleDatabaseError(Exception e, String messageInfo) {
        logger.error("Lỗi lưu trữ message: {} - Chi tiết: {}", messageInfo, e.getMessage());
        
    }
}