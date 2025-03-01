package ru.yandex.practicum.filmorate.mapping.dto;

import lombok.Value;

@Value
public class FilmInfo {
    long id;
    String name;
    String releaseDate;
    String description;
    long duration;
    long rate;
}