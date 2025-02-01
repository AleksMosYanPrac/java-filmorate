package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film add(Film film) throws ValidationException;

    Film update(Film film) throws ValidationException;

    List<Film> list();
}