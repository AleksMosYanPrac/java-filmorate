package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.RateService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final FilmService filmService;
    private final UserService userService;

    @Override
    public void putLike(long filmId, long userId) throws ExistException {
        Film film = filmService.getById(filmId);
        User user = userService.getById(userId);

        if (film.likeItBy(user)) {
            log.info("Film with id:{} has been liked by User with id{}", filmId, userId);
            filmService.update(film);
        } else {
            log.info("Film with id:{} already liked by User with id{}", filmId, userId);
        }
    }

    @Override
    public void removeLike(long filmId, long userId) throws ExistException {
        Film film = filmService.getById(filmId);
        User user = userService.getById(userId);

        if (film.unlikeItBy(user)) {
            log.info("Film with id:{} been unliked by User with id{}", filmId, userId);
            filmService.update(film);
        } else {
            log.info("Film with id:{} already unliked by User with id{}", filmId, userId);
        }
    }

    @Override
    public List<Film> listPopularFilms(long listSize) {
        return filmService.list().stream()
                .sorted(Comparator.comparingLong(Film::rate).reversed())
                .limit(listSize)
                .toList();
    }
}