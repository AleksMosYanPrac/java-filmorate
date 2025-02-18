package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.UserMapper;
import ru.yandex.practicum.filmorate.mapping.dto.UserData;
import ru.yandex.practicum.filmorate.mapping.dto.UserInfo;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static ru.yandex.practicum.filmorate.validation.ValidationGroup.*;

@Slf4j
@Validated
@RestController
@RequestMapping("${filmorate.endpoints.users}")
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    @Validated(OnCreate.class)
    UserData createUser(@RequestBody @Valid @NotNull UserData user) throws ExistException {
        return userMapper.toUserData(
                userService.create(userMapper.toUser(user))
        );
    }

    @PutMapping
    @ResponseStatus(OK)
    @Validated(OnUpdate.class)
    UserData updateUser(@RequestBody @Valid @NotNull UserData user) throws ExistException {
        return userMapper.toUserData(
                userService.update(userMapper.toUser(user))
        );
    }

    @GetMapping
    @ResponseStatus(OK)
    List<UserData> getAllUsers() {
        return userService.list()
                .stream()
                .map(userMapper::toUserData)
                .toList();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(OK)
    UserInfo getUserById(@PathVariable long userId) throws ExistException {
        return userMapper.toUserInfo(
                userService.getById(userId)
        );
    }
}