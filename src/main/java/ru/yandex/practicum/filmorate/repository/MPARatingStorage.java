package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.film.MPARating;

import java.util.List;
import java.util.Optional;

public interface MPARatingStorage {

    Optional<MPARating> findById(long id);

    List<MPARating> findAll();
}