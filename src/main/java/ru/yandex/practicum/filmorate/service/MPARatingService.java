package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.film.MPARating;

import java.util.List;

public interface MPARatingService {

    MPARating getMPARatingById(long id) throws ExistException;

    List<MPARating> getAllMPARatings();
}