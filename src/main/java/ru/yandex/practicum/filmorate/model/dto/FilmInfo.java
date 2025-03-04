package ru.yandex.practicum.filmorate.model.dto;

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