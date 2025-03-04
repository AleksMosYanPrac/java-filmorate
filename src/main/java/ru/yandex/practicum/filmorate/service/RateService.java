package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;

import java.util.List;

public interface RateService {

    void putLike(long filmId, long userId) throws ExistException;

    void removeLike(long filmId, long userId) throws ExistException;

    List<FilmInfo> listPopularFilms(long listSize);
}