package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ExistException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(ExistException.class)
    ResponseEntity<Map<String, String>> onExistException(ExistException exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("ExistException: {}", exception.getMessage());
        return new ResponseEntity<>(body, NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Map<String, String>> onConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> body = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            body.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        log.info("BeanValidation fail: {}", body);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> onConstraintViolation(MethodArgumentNotValidException exception) {
        Map<String, String> body = new HashMap<>();
        for (FieldError violation : exception.getBindingResult().getFieldErrors()) {
            body.put(violation.getField(), violation.getDefaultMessage());
        }
        log.info("MethodArgumentValidation fail: {}", body);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Map<String, String>> onRuntimeException(RuntimeException exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Internal server exception");
        log.info("RuntimeException: {}", exception.getMessage());
        return new ResponseEntity<>(body, INTERNAL_SERVER_ERROR);
    }
}