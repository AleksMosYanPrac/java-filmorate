package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    @Override
    public Film add(Film film) throws ExistException {
        if (exist(film)) {
            log.debug("Film with name {} and release date {} already exist",
                    film.getName(), film.getFormattedReleaseDate());
            throw new ExistException("Film already exist");
        }
        Film newFilm = filmStorage.add(film);
        log.info("Film added with ID:{} NAME:{} ", newFilm.getId(), newFilm.getName());
        return newFilm;
    }

    @Override
    public Film getById(long id) throws ExistException {
        return filmStorage.findById(id).orElseThrow(() -> {
            log.debug("Film not exist with ID:{}", id);
            return new ExistException("User not exist with ID:" + id);
        });
    }

    @Override
    public Film update(Film film) throws ExistException {
        if (!exist(film)) {
            log.debug("Film with ID {} not exist", film.getId());
            throw new ExistException("Film with ID " + film.getId() + "not exist");
        }

        Film updatingFilm = filmStorage.findById(film.getId())
                .get()
                .toBuilder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        Film updatedFilm = filmStorage.update(updatingFilm);
        log.info("Film updated with ID:{}", film.getId());
        return updatedFilm;
    }

    @Override
    public List<Film> list() {
        return filmStorage.getAll();
    }

    private boolean exist(Film film) {
        Predicate<Film> predicate =
                Objects.isNull(film.getId()) ?
                        (Film f) -> f.getName().equals(film.getName()) &&
                                    f.getReleaseDate().isEqual(film.getReleaseDate()) :
                        (Film f) -> f.getId().equals(film.getId());
        return filmStorage.contains(predicate);
    }
}