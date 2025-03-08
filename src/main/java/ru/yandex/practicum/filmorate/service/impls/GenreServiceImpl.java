package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.repository.GenreStorage;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class GenreServiceImpl implements GenreService {

    private final GenreStorage genreStorage;

    @Override
    public Genre getGenreById(long id) throws ExistException {
        return genreStorage.findById(id).orElseThrow(() -> new ExistException("Genre not exist with ID: " + id));
    }

    public Set<Genre> getGenresByIdList(Set<Long> idList) throws ExistException {
        Set<Genre> genres = genreStorage.findByIdList(idList);
        if (genres.size() != idList.size()) {
            List<Long> unavailableGenresId = idList.stream()
                    .filter(l -> !genres.stream().map(Genre::getId).toList().contains(l))
                    .toList();
            throw new ExistException("Genres not exist with List ID: " + unavailableGenresId);
        }
        return genres;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreStorage.findAll();
    }
}
