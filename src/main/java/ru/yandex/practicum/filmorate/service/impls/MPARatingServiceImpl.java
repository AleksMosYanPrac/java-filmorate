package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.repository.MPARatingStorage;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class MPARatingServiceImpl implements MPARatingService {

    private final MPARatingStorage mpaRatingStorage;

    @Override
    public MPARating getMPARatingById(long id) throws ExistException {
        return mpaRatingStorage.findById(id)
                .orElseThrow(() -> new ExistException("MPA rating not exist with ID: " + id));
    }

    @Override
    public List<MPARating> getAllMPARatings() {
        return mpaRatingStorage.findAll();
    }
}