package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface FilmStorage {
    Film add(Film film);

    Optional<Film> findById(long id);

    Film update(Film film);

    List<Film> getAll();

    boolean contains(Predicate<Film> predicate);
}