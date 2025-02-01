package ru.yandex.practicum.filmorate.service.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FilmServiceImpl implements FilmService {

    private static final Logger logger = LoggerFactory.getLogger(FilmServiceImpl.class);

    private final Map<Long, Film> filmCollection;

    public FilmServiceImpl() {
        this.filmCollection = new HashMap<>();
    }

    public FilmServiceImpl(Map<Long, Film> filmCollection) {
        this.filmCollection = filmCollection;
    }

    @Override
    public Film add(Film film) throws ValidationException {
        if (!validate(film).isBlank()) {
            String validationMessage = validate(film);
            logger.error("Film validation fail:{}", validationMessage);
            throw new ValidationException(validationMessage);
        }
        film.setId(generate());
        filmCollection.put(film.getId(), film);
        logger.debug("Film added with ID:{} NAME:{} ", film.getId(), film.getName());
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException, NotFoundException {
        if (!validate(film).isBlank()) {
            String validationMessage = validate(film);
            logger.error("Film validation fail:{}", validationMessage);
            throw new ValidationException(validationMessage);
        }
        Film old = filmCollection.computeIfAbsent(film.getId(), k -> {
            logger.debug("Can't update film because absent ID:{}", film.getId());
            throw new NotFoundException("Film with" + k + "not found");
        });
        filmCollection.replace(old.getId(), old, film);
        logger.debug("Film updated with ID:{}", film.getId());
        return film;
    }

    @Override
    public List<Film> list() {
        return filmCollection.values().stream().toList();
    }

    private String validate(Film film) {
        StringBuilder message = new StringBuilder();
        if (Objects.isNull(film.getName()) || film.getName().isBlank()) {
            message.append("'name can't be empty'");
        } else if (film.getDescription().length() >= 200) {
            message.append("'description length should be less then 200 symbols'");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            message.append("'date of release must be after 28.12.1895'");
        } else if (film.getDuration() <= 0) {
            message.append("'duration should more 0'");
        }
        return message.toString();
    }

    private Long generate() {
        return filmCollection.keySet()
                       .stream()
                       .mapToLong(id -> id)
                       .max()
                       .orElse(0) + 1;
    }
}