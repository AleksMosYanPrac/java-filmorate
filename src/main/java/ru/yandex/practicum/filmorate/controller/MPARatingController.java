package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${filmorate.endpoints.mpa}")
@RequiredArgsConstructor
public class MPARatingController {

    private final MPARatingService mpaRatingService;

    @GetMapping("/{mpaId}")
    @ResponseStatus(OK)
    MPARating getNameById(@PathVariable long mpaId) throws ExistException {
        return mpaRatingService.getMPARatingById(mpaId);
    }

    @GetMapping
    @ResponseStatus(OK)
    List<MPARating> getAllMPARating() {
        return mpaRatingService.getAllMPARatings();
    }
}