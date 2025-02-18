package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorageImpl implements FilmStorage {

    private final Map<Long, Film> filmCollection;

    @Override
    public Film add(Film film) throws ExistException {
        if (contains(film)) {
            throw new ExistException("Film already exist with name: " + film.getName());
        }
        Film newFilm = film.toBuilder().id(generate()).build();
        filmCollection.put(newFilm.getId(), newFilm);
        log.info("New film with id:{} has been added", newFilm.getId());
        return newFilm;
    }

    @Override
    public Film findById(long id) throws ExistException {
        return filmCollection.values().stream()
                .filter(f -> f.getId() == id).findFirst()
                .orElseThrow(() -> new ExistException("Film not exist with ID: " + id));
    }

    @Override
    public Film update(Film film) throws ExistException {
        if (!contains(film)) {
            throw new ExistException("Film not exist with ID:" + film.getId());
        }
        filmCollection.replace(film.getId(), film);
        log.info("Film with id:{} has been updated", film.getId());
        return filmCollection.get(film.getId());
    }

    @Override
    public List<Film> getAll() {
        return filmCollection.values().stream().toList();
    }

    @Override
    public List<Film> sortByRateAndLimitResult(long listSize) {
        return filmCollection.values().stream()
                .sorted(Comparator.comparingLong(Film::rate).reversed())
                .limit(listSize)
                .toList();
    }

    private boolean contains(Film film) {
        Predicate<Film> predicate =
                Objects.isNull(film.getId()) ?
                        (Film f) -> f.getName().equals(film.getName()) &&
                                    f.getReleaseDate().isEqual(film.getReleaseDate()) :
                        (Film f) -> f.getId().equals(film.getId());

        return filmCollection.values().stream().anyMatch(predicate);
    }

    private Long generate() {
        return filmCollection.keySet().stream().mapToLong(l -> l).max().orElse(0L) + 1;
    }
}