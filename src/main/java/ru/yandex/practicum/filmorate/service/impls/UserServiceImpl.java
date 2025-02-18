package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User create(User user) throws ExistException {
        if (exist(user)) {
            log.debug("Created user already exist with login:{}", user.getLogin());
            throw new ExistException("User already exist");
        }
        User newUser = userStorage.add(user);
        log.info("New User has been created with id={}", newUser.getId());
        return newUser;
    }

    @Override
    public User getById(long id) throws ExistException {
        return userStorage.findById(id).orElseThrow(() -> {
            log.debug("User not exist with ID:{}", id);
            return new ExistException("User not exist with ID:" + id);
        });
    }

    @Override
    public User update(User user) throws ExistException {
        if (!exist(user)) {
            log.debug("User not exist with ID:{}", user.getId());
            throw new ExistException("User not exist with ID:" + user.getId());
        }

        User updatingUser = userStorage.findById(user.getId())
                .get()
                .toBuilder()
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .build();

        User updatedUser = userStorage.update(updatingUser);
        log.info("User has been updated with id={}", updatedUser.getId());
        return updatedUser;
    }

    @Override
    public List<User> list() {
        return userStorage.getAll();
    }

    private boolean exist(User user) {
        Predicate<User> predicate =
                !Objects.isNull(user.getId()) ?
                        (u) -> u.getId().equals(user.getId()) :
                        (u) -> u.getLogin().equals(user.getLogin()) || u.getEmail().equals(user.getEmail());
        return userStorage.contains(predicate);
    }
}