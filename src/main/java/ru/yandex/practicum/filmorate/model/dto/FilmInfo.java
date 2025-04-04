package ru.yandex.practicum.filmorate.model.dto;

import lombok.Value;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.MPARating;

import java.util.Set;

@Value
public class FilmInfo {
    long id;
    String name;
    String releaseDate;
    String description;
    long duration;
    long rate;
    Set<Genre> genres;
    MPARating mpa;
}