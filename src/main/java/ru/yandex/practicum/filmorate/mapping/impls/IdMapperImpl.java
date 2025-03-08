package ru.yandex.practicum.filmorate.mapping.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.IdMapper;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.UserStorage;

@Component
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class IdMapperImpl implements IdMapper {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Override
    public Long toUserId(long id) throws ExistException {
        return userStorage.findById(id).orElseThrow(() -> new ExistException("User not exist with ID" + id)).getId();
    }

    @Override
    public Long toFilmId(long id) throws ExistException {
        return filmStorage.findById(id).orElseThrow(() -> new ExistException("Film not exist with ID" + id)).getId();
    }
}
