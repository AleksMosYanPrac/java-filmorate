package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.impls.FilmServiceImpl;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    public FilmController() {
        this.filmService = new FilmServiceImpl();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        Film newFilm = filmService.add(film);
        logger.info("Added film with ID:{}", film.getId());
        return newFilm;
    }

    @PutMapping
    @ResponseStatus(OK)
    public Film updateFilm(@RequestBody Film film) throws ValidationException, NotFoundException {
        if (film.getId() == null) {
            ValidationException exception = new ValidationException("Data should have ID, provided: null");
            logger.error("Update film fail", exception);
            throw exception;
        }
        Film updatedFilm = filmService.update(film);
        logger.info("Updated film with ID:{}", film.getId());
        return updatedFilm;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Film> getAllFilms() {
        return filmService.list();
    }
}