package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.RateService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmMapper filmMapper;

    @Override
    public void putLike(long filmId, long userId) throws ExistException {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);

        if (film.likeItBy(user)) {
            log.info("Film with id:{} has been liked by User with id{}", filmId, userId);
            filmStorage.update(film);
        } else {
            log.info("Film with id:{} already liked by User with id{}", filmId, userId);
        }
    }

    @Override
    public void removeLike(long filmId, long userId) throws ExistException {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);

        if (film.unlikeItBy(user)) {
            log.info("Film with id:{} been unliked by User with id{}", filmId, userId);
            filmStorage.update(film);
        } else {
            log.info("Film with id:{} already unliked by User with id{}", filmId, userId);
        }
    }

    @Override
    public List<FilmInfo> listPopularFilms(long listSize) {
        return filmStorage.sortByRateAndLimitResult(listSize)
                .stream()
                .map(filmMapper::toFilmInfo)
                .toList();
    }
}