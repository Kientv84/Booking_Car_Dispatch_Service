package com.service.dispatch.exceptions;


import java.util.HashMap;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice Đánh dấu class bắt Exception cho toàn bộ controller bên trong phạm vi của
 * dự án ( global ) Ngoại lệ này xảy ra khi các tham số arguments trong các phương thức không thỏa
 * mản các buộc được xác định ví dụ, @Notnull & @Validate. Khi dùng anotation @ExceptionHandler thì
 * đối số truyền vào phải là kiểu ngoại lệ dạng oject nên .class sẽ giúp lấy đối tượng class của một
 * lớp cụ thể Mục đích là khi một class xuất hiện exception trong logic thì class bắt lỗi có khai
 * báo @Anotation là @RestControllerAdvice sẽ bắt lại và truyền vào các method sử lý exception vớt
 * Anotation @ExceptionHandler và xe ex thuộc loại nào để log ra cho phù hợp.
 */
@RestControllerAdvice // Đánh dấu class bắt Exception cho toàn bộ controller bên trong phạm vi của
// dự án ( global )
public class GlobalExceptionHandler {
    private MessageSource messageSource;

    @ExceptionHandler(
            MethodArgumentNotValidException
                    .class) // Catch Exception chung các hàm có ràng buộc validate hợp lệ.
    public ResponseEntity<Map<String, String>> handleValidateExeption(
            MethodArgumentNotValidException
                    ex) { // Khai bảo kiểu trả về cuủa hàm là ResponseEntity<Map<String, String>>
        // Tức là map các key và value của exception đó ví dụ: lỗi ở email not null -> email: not null
        Map<String, String> response =
                new HashMap<>(); // Hashmap là một class của thể của Map và được triển khai theo kiểu băm (
        // hash ) -> new hashMap tạo ra một đối tượng hashmap mới dùng để chưa cặp
        // key-value
        // Tạo một "bộ nhớ tạm" để lưu các thông báo lỗi liên quan đến các trường trong dữ liệu đầu vào
        // của API theo dạng key và value
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> response.put(error.getField(), error.getDefaultMessage()));
        // getBindingResults Truy cập vào đối tượng chứa kết quả ràng buộc dữ liệu đầu vào.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class) // cho biết nó sẽ bắt tất cả các ngoại lệ thông thường.
    public ResponseEntity<Map<String, String>> hanleGeneralException(Exception ex) {
        Map<String, String> response =
                new HashMap<>(); // Vậy khi lấy được key của ex thì tiến hành băm key đó thành một số (
        // hashcode ) để thực hiện lưu tạm thời

        response.put("Internal sever error: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DispatchServiceException.class)
    public ResponseEntity<Map<String, String>> handleAccountServiceException(
            DispatchServiceException ex) {
        Map<String, String> response = new HashMap<>();

        response.put("errorCode: ", ex.getErrorCode());
        response.put("errorMessage: ", ex.getMessageCode());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

