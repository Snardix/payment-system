package com.example.auth_service.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    //ошибки авторизации / регистрации
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthException(AuthException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    //Ошибки валидации DTO (@NotBlank, @Email, @Size и т.д.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return buildError(HttpStatus.BAD_REQUEST, message);
    }


    //Ошибка доступа (Spring Security)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleForbidden(AccessDeniedException ex) {
        return buildError(HttpStatus.FORBIDDEN, "Access denied");
    }


    //Все неизвестные / технические ошибки
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }


    //Универсальный JSON-ответ
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(status).body(body);
    }
}
