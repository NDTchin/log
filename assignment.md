**Đề bài: Xây dựng hệ thống xử lý log tập trung cho ứng dụng thương mại điện tử sử dụng Spring Boot và RabbitMQ**

**Mô tả:**

Bạn hãy xây dựng một hệ thống xử lý log tập trung cho một ứng dụng thương mại điện tử. Hệ thống bao gồm 2 microservice:

1.  **Order Service (Log Producer):**
    *   Mô phỏng việc tạo đơn hàng và phát sinh log messages dựa trên kết quả xử lý.
    *   Gửi các log message này lên RabbitMQ exchange.
2.  **Log Processing Service (Log Consumer):**
    *   Nhận log messages từ RabbitMQ queue.
    *   Xử lý và lưu trữ log messages vào MongoDB.
    *   Gửi thông báo cho đội ngũ kỹ thuật khi gặp log message có level ERROR.

**I. Order Service (Log Producer):**

1.  **REST Endpoint:**
    *   Tạo một REST endpoint `/orders` (POST method) để mô phỏng việc tạo đơn hàng.
    *   Request body (dữ liệu đầu vào) có định dạng JSON như sau:
        ```json
        {
          "customerId": 123,
          "items": [
            {"productId": 456, "quantity": 2, "price": 25.0},
            {"productId": 789, "quantity": 1, "price": 100.0}
          ],
          "paymentMethod": "creditCard",
          "creditCardNumber": "1234-5678-9012-3456"
        }
        ```
        *   `customerId`: ID của khách hàng.
        *   `items`: Danh sách các sản phẩm trong đơn hàng.
            *   `productId`: ID của sản phẩm.
            *   `quantity`: Số lượng sản phẩm.
            *   `price`: Giá của sản phẩm.
        *   `paymentMethod`: Phương thức thanh toán (ví dụ: `"creditCard"`).
        *   `creditCardNumber`: (Chỉ khi `paymentMethod` là `"creditCard"`) Số thẻ tín dụng.

2.  **Điều kiện thành công/thất bại:**
    *   **Thành công (INFO log):**
        *   **Điều kiện:**
            *   Tất cả các sản phẩm trong đơn hàng đều có đủ số lượng trong kho (bỏ qua việc kiểm tra này).
            *   `paymentMethod` hợp lệ (`"creditCard"`).
            *   Nếu `paymentMethod` là `"creditCard"`:
                *   `creditCardNumber` phải hợp lệ (mô phỏng bằng cách kiểm tra độ dài phải bằng 16 ký tự).
        *   **Log Message:**
            *   Level: `INFO`
            *   Nội dung:
                ```
                "Order created successfully. Order ID: [orderId], Customer ID: [customerId], Total amount: [totalAmount]"
                ```
                *   `[orderId]`:  ID của đơn hàng (UUID).
                *   `[customerId]`: ID của khách hàng.
                *   `[totalAmount]`: Tổng giá trị đơn hàng.
    *   **Thất bại (ERROR log):**
        *   **Điều kiện:**
            *   Bất kỳ điều kiện nào ở trên không thỏa mãn.
        *   **Log Message:**
            *   Level: `ERROR`
            *   Nội dung:
                ```
                "Payment failed for order ID: [orderId]. Reason: [reason]"
                ```
                *   `[orderId]`: ID của đơn hàng.
                *   `[reason]`: Lý do thất bại.

3.  **Sending Log Message to RabbitMQ:**
    *   Sử dụng `RabbitTemplate` để gửi log messages lên RabbitMQ exchange.
    *   Message body phải là một JSON string chứa thông tin về log message:
        ```json
        {
          "level": "INFO"|"ERROR",
          "message": "...",
          "timestamp": "...",
          "serviceName": "order-service"
        }
        ```

4.  **Response:**
    *   Trả về HTTP status code 201 (Created).
    *   Response body có thể chứa thông tin về đơn hàng.

**II. Log Processing Service (Log Consumer):**

1.  Sử dụng `@RabbitListener` để lắng nghe log messages từ RabbitMQ queue.
2.  **Xử lý và lưu trữ:**
    *   Lưu trữ **tất cả** log messages vào MySQL.
    *   Nếu log message có level `ERROR`:
        *   In ra console thông báo: `"Error log detected. Sending notification to tech team: [message]"`.

**III. RabbitMQ Configuration:**

1.  Khai báo exchange và queue trong code (sử dụng `@Bean`).
2.  Sử dụng exchange type `fanout`.

**IV. General Requirements:**

1.  Sử dụng Maven hoặc Gradle để quản lý dependency và cấu trúc project (2 modules: `order-service`, `log-processing-service`).
2.  Sử dụng SLF4J cho logging.
3.  Cấu hình RabbitMQ và MySQL connection thông qua `application.properties`.
4.  Xử lý exception (RabbitMQ, MySQL).

**Thang điểm:**

*   **2 điểm:** Setting project làm việc được với rabbitMQ.
*   **3 điểm:** Order Service:
    *   Endpoint hoạt động, tạo log message đúng level và định dạng.
    *   Gửi log message lên RabbitMQ.
    *   Xử lý đủ 2 trường hợp thành công và lỗi.  
*   **3 điểm:** Log Processing Service:
    *   Nhận log message từ RabbitMQ.
    *   Lưu trữ log vào MySQL.
    *   Xử lý parse log ERROR và in thông báo.
*   **1 điểm:** Cấu hình RabbitMQ (fanout exchange).
*   **1 điểm:** Logging và Error Handling đầy đủ.
