package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static ru.yandex.practicum.filmorate.validation.ValidationGroup.*;

@Slf4j
@Validated
@RestController
@RequestMapping("${filmorate.endpoints.films}")
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Validated(OnCreate.class)
    public Film addFilm(@RequestBody @Valid @NotNull Film film) throws ExistException {
        return filmService.add(film);
    }

    @PutMapping
    @ResponseStatus(OK)
    @Validated(OnUpdate.class)
    public Film updateFilm(@RequestBody @Valid @NotNull Film film) throws ExistException {
        return filmService.update(film);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Film> getAllFilms() {
        return filmService.list();
    }

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
}