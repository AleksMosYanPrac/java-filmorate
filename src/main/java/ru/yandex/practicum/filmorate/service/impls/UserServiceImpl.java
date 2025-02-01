package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users;

    @Override
    public User create(User user) throws IllegalArgumentException {
        if (users.containsKey(user.getId())) {
            log.debug("Can't create user because already exist with id:{}", user.getId());
            throw new IllegalArgumentException("User already exist");
        }
        User newUser = user.toBuilder().id(generate()).build();
        users.put(newUser.getId(), newUser);
        log.debug("New User has been created with id={}", newUser.getId());
        return newUser;
    }

    @Override
    public User update(User user) throws NotFoundException {
        if (!users.containsKey(user.getId())) {
            log.debug("Can't update user because absent ID:{}", user.getId());
            throw new NotFoundException("Can't find User with ID: " + user.getId());
        }
        User userById = users.get(user.getId());
        User updatedUser = user.toBuilder().id(userById.getId()).build();
        users.replace(user.getId(), updatedUser);
        log.debug("User has been updated with id={}", updatedUser.getId());
        return users.get(user.getId());
    }

    @Override
    public List<User> list() {
        return users.values().stream().toList();
    }

    private Long generate() {
        return users.values()
                .stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0) + 1;
    }
}