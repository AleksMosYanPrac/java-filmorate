package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.FilmMapper;
import ru.yandex.practicum.filmorate.mapping.IdMapper;
import ru.yandex.practicum.filmorate.model.dto.FilmInfo;
import ru.yandex.practicum.filmorate.model.film.LikesRating;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.LikesRatingStorage;
import ru.yandex.practicum.filmorate.service.RateService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final LikesRatingStorage likesRatingStorage;
    private final IdMapper idMapper;
    private final FilmStorage filmStorage;
    private final FilmMapper filmMapper;

    @Override
    public void putLike(long filmId, long userId) throws ExistException {

        LikesRating rating = likesRatingStorage.findById(idMapper.toFilmId(filmId));

        if (rating.addLike(idMapper.toUserId(userId))) {
            likesRatingStorage.save(rating);
            log.info("Film with id:{} has been liked by User with id{}", filmId, userId);
        } else {
            log.info("Film with id:{} already liked by User with id{}", filmId, userId);
        }
    }

    @Override
    public void removeLike(long filmId, long userId) throws ExistException {

        LikesRating rating = likesRatingStorage.findById(idMapper.toFilmId(filmId));

        if (rating.deleteLike(idMapper.toUserId(userId))) {
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
}