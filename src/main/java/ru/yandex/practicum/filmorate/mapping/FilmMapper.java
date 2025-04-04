package ru.yandex.practicum.filmorate.mapping;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.FilmData;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.film.Film;

public interface FilmMapper {

    Film toFilm(FilmData filmData) throws ExistException;

    FilmData toFilmData(Film film);

    FilmInfo toFilmInfo(Film film);
}