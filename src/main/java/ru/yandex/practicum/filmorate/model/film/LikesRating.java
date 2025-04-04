package ru.yandex.practicum.filmorate.model.film;

import lombok.Value;

import java.util.Set;

@Value
public class LikesRating {

    Long filmId;
    Set<Long> usersId;

    public long getRating() {
        return usersId.size();
    }

    public boolean addLike(Long userId) {
        return usersId.add(userId);
    }

    public boolean deleteLike(Long userId) {
        return usersId.remove(userId);
    }
}