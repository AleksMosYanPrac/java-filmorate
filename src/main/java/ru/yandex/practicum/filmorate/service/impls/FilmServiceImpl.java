package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final Map<Long, Film> filmCollection;

    @Override
    public Film add(Film film) throws ExistException {
        if (exist(film)) {
            log.debug("Film with name {} and release date {} already exist",
                    film.getName(), film.getFormattedReleaseDate());
            throw new ExistException("Film already exist");
        }
        Film newFilm = film.toBuilder().id(generate()).build();
        filmCollection.put(newFilm.getId(), newFilm);
        log.info("Film added with ID:{} NAME:{} ", newFilm.getId(), newFilm.getName());
        return filmCollection.get(newFilm.getId());
    }

    @Override
    public Film update(Film film) throws ExistException {
        if (!exist(film)) {
            log.debug("Film with ID {} not exist", film.getId());
            throw new ExistException("Film with ID " + film.getId() + "not exist");
        }
        filmCollection.replace(film.getId(), film);
        log.info("Film updated with ID:{}", film.getId());
        return filmCollection.get(film.getId());
    }

    @Override
    public List<Film> list() {
        return filmCollection.values().stream().toList();
    }

    private boolean exist(Film film) {
        if (Objects.isNull(film.getId())) {
            Predicate<Film> predicate =
                    (Film f) -> f.getName().equals(film.getName()) && f.getReleaseDate().isEqual(film.getReleaseDate());
            return filmCollection.values().stream().anyMatch(predicate);
        } else {
            return filmCollection.containsKey(film.getId());
        }
    }

    private Long generate() {
        return filmCollection.keySet()
                       .stream()
                       .mapToLong(id -> id)
                       .max()
                       .orElse(0) + 1;
    }
}