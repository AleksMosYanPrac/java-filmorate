package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.film.LikesRating;

public interface LikesRatingStorage {

    LikesRating findById(Long filmId);

    LikesRating save(LikesRating rating);
}