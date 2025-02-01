package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Требования к полям класса:
 * <p> 1. название не может быть пустым;
 * <p> 2. максимальная длина описания — 200 символов;
 * <p> 3. дата релиза — не раньше 28 декабря 1895 года;
 * <p> 4. продолжительность фильма должна быть положительным числом.
 */
@Data
@Builder(toBuilder = true)
public class Film {
    private Long id;
    private String name;
    private LocalDate releaseDate;
    private String description;
    private long duration;
}