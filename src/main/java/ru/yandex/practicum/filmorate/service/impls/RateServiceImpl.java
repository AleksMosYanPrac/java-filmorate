package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.film.LikesRating;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.LikesRatingStorage;
import ru.yandex.practicum.filmorate.repository.UserStorage;
import ru.yandex.practicum.filmorate.service.RateService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final LikesRatingStorage likesRatingStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmMapper filmMapper;

    @Override
    public void putLike(long filmId, long userId) throws ExistException {
        checkExistence(filmId, userId);
        LikesRating rating = likesRatingStorage.findById(filmId);

        if (rating.addLike(userId)) {
            likesRatingStorage.save(rating);
            log.info("Film with id:{} has been liked by User with id{}", filmId, userId);
        } else {
            log.info("Film with id:{} already liked by User with id{}", filmId, userId);
        }
    }

    @Override
    public void removeLike(long filmId, long userId) throws ExistException {
        checkExistence(filmId, userId);
        LikesRating rating = likesRatingStorage.findById(filmId);

        if (rating.deleteLike(userId)) {
            likesRatingStorage.save(rating);
            log.info("Film with id:{} been unliked by User with id{}", filmId, userId);
        } else {
            log.info("Film with id:{} didn't been liked by User with id{}", filmId, userId);
        }
    }

    @Override
    public List<FilmInfo> listPopularFilms(long listSize) {
        return filmStorage.sortByRateAndLimitResult(listSize)
                .stream()
                .map(filmMapper::toFilmInfo)
                .toList();
    }

    private void checkExistence(long filmId, long userId) throws ExistException {
        if (!filmStorage.exist(filmId)) {
            throw new ExistException("Film not exist with ID:" + filmId);
        }
        if (!userStorage.exist(userId)) {
            throw new ExistException("User not exist with ID:" + userId);
        }
    }
}