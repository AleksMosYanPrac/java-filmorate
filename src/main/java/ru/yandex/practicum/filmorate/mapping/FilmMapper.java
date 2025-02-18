package ru.yandex.practicum.filmorate.mapping;

import ru.yandex.practicum.filmorate.mapping.dto.FilmData;
import ru.yandex.practicum.filmorate.mapping.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmMapper {

    FilmData toFilmData(Film film);

    FilmInfo toFilmInfo(Film film);

    Film toFilm(FilmData filmData);
}