package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${filmorate.endpoints.genres}")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{genreId}")
    @ResponseStatus(OK)
    Genre getNameById(@PathVariable long genreId) throws ExistException {
        return genreService.getGenreById(genreId);
    }

    @GetMapping
    @ResponseStatus(OK)
    List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }
}