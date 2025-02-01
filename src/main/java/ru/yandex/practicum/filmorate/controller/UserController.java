package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("${filmorate.endpoints.users}")
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    User createUser(@RequestBody User user) throws ConstraintViolationException, IllegalArgumentException {
        User newUser = userService.create(user);
        log.info("User created with ID: {}", newUser.getId());
        return newUser;
    }

    @PutMapping
    @ResponseStatus(OK)
    User updateUser(@RequestBody User user) throws ConstraintViolationException, NotFoundException {
        User updatedUser = userService.update(user);
        log.info("User updated with ID: {}", updatedUser.getId());
        return updatedUser;
    }

    @GetMapping
    @ResponseStatus(OK)
    List<User> getAllUsers() {
        return userService.list();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Map<String, String>> onConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> body = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            body.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        log.error("BeanValidation fail: {}", body);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Map<String, String>> onIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.error("IllegalStateException: {}", exception.getMessage());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Map<String, String>> onNotFoundException(NotFoundException exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.error("NotFoundException: {}", exception.getMessage());
        return new ResponseEntity<>(body, NOT_FOUND);
    }
}