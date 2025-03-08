package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    Set<Genre> findByIdList(Set<Long> idList);

    Optional<Genre> findById(long id);

    List<Genre> findAll();
}