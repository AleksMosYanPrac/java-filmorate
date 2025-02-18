package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.dto.FilmData;
import ru.yandex.practicum.filmorate.mapping.dto.FilmInfo;
import ru.yandex.practicum.filmorate.mapping.FilmMapper;
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
    private final FilmMapper filmMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    @Validated(OnCreate.class)
    public FilmData addFilm(@RequestBody @Valid @NotNull FilmData film) throws ExistException {
        return filmMapper.toFilmData(
                filmService.add(filmMapper.toFilm(film))
        );
    }

    @PutMapping
    @ResponseStatus(OK)
    @Validated(OnUpdate.class)
    public FilmData updateFilm(@RequestBody @Valid @NotNull FilmData film) throws ExistException {
        return filmMapper.toFilmData(
                filmService.update(filmMapper.toFilm(film))
        );
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<FilmData> getAllFilms() {
        return filmService.list()
                .stream()
                .map(filmMapper::toFilmData)
                .toList();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(OK)
    public FilmInfo findFilmById(@PathVariable long filmId) throws ExistException {
        return filmMapper.toFilmInfo(
                filmService.getById(filmId)
        );
    }
}