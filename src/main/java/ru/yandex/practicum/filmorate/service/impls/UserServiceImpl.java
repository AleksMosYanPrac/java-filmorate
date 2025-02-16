package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users;

    @Override
    public User create(User user) throws ExistException {
        if (exist(user)) {
            log.debug("Created user already exist with login:{}", user.getLogin());
            throw new ExistException("User already exist");
        }
        User newUser = user.toBuilder().id(generate()).build();
        users.put(newUser.getId(), newUser);
        log.info("New User has been created with id={}", newUser.getId());
        return newUser;
    }

    @Override
    public User update(User user) throws ExistException {
        if (!exist(user)) {
            log.debug("User not exist with ID:{}", user.getId());
            throw new ExistException("User not exist with ID:" + user.getId());
        }
        User userById = users.get(user.getId());
        User updatedUser = user.toBuilder().id(userById.getId()).build();
        users.replace(user.getId(), updatedUser);
        log.info("User has been updated with id={}", updatedUser.getId());
        return users.get(user.getId());
    }

    @Override
    public List<User> list() {
        return users.values().stream().toList();
    }

    private boolean exist(User user) {
        if (Objects.isNull(user.getId())) {
            Predicate<User> predicate =
                    (User u) -> u.getLogin().equals(user.getLogin()) || u.getEmail().equals(user.getEmail());
            return users.values().stream().anyMatch(predicate);
        } else {
            return users.containsKey(user.getId());
        }
    }

    private Long generate() {
        return users.values()
                       .stream()
                       .mapToLong(User::getId)
                       .max()
                       .orElse(0) + 1;
    }
}