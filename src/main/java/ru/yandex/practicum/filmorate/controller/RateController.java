package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.service.RateService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("${filmorate.endpoints.films}")
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class RateController {

    private final RateService rateService;

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(OK)
    public void likeFilm(@PathVariable long filmId, @PathVariable long userId) throws ExistException {
        rateService.putLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(OK)
    public void unlikeFilm(@PathVariable long filmId, @PathVariable long userId) throws ExistException {
        rateService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(OK)
    public List<FilmInfo> getPopular(@RequestParam(defaultValue = "${filmorate.film.popular.count}") long count) {
        return rateService.listPopularFilms(count);
    }
}