package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

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
    public FilmData addFilm(@RequestBody @Valid @NotNull FilmData film) throws ExistException {
        return filmService.add(film);
    }

    @PutMapping
    @ResponseStatus(OK)
    @Validated(OnUpdate.class)
    public FilmData updateFilm(@RequestBody @Valid @NotNull FilmData film) throws ExistException {
        return filmService.update(film);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<FilmData> getAllFilms() {
        return filmService.list();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(OK)
    public FilmInfo findFilmById(@PathVariable long filmId) throws ExistException {
        return filmService.getById(filmId);
    }
}