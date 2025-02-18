package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface RateService {

    void putLike(long filmId, long userId) throws ExistException;

    void removeLike(long filmId, long userId) throws ExistException;

    List<Film> listPopularFilms(long listSize);
}