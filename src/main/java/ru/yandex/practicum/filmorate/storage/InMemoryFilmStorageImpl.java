package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorageImpl implements FilmStorage {

    private final Map<Long, Film> filmCollection;

    @Override
    public Film add(Film film) {
        Film newFilm = film.toBuilder().id(generate()).build();
        filmCollection.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Optional<Film> findById(long id) {
        return filmCollection.values().stream().filter(f -> f.getId() == id).findFirst();
    }

    @Override
    public Film update(Film film) {
        filmCollection.replace(film.getId(), film);
        return filmCollection.get(film.getId());
    }

    @Override
    public List<Film> getAll() {
        return filmCollection.values().stream().toList();
    }

    @Override
    public boolean contains(Predicate<Film> predicate) {
        return filmCollection.values().stream().anyMatch(predicate);
    }

    private Long generate() {
        return filmCollection.keySet().stream().mapToLong(l -> l).max().orElse(0L) + 1;
    }
}