package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {

    Genre getGenreById(long id) throws ExistException;

    Set<Genre> getGenresByIdList(Set<Long> idList) throws ExistException;

    List<Genre> getAllGenres();
}