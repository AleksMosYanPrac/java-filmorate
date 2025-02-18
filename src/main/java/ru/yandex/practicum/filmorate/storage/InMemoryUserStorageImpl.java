package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorageImpl implements UserStorage {

    private final Map<Long, User> users;

    @Override
    public User add(User user) throws ExistException {
        if (contains(user)) {
            throw new ExistException("User already exist with login: " + user.getLogin());
        }
        User newUser = user.toBuilder().id(generate()).build();
        users.put(newUser.getId(), newUser);
        log.info("New user with id:{} has been added", newUser.getId());
        return newUser;
    }

    @Override
    public User findById(long id) throws ExistException {
        return users.values().stream()
                .filter(u -> u.getId() == id).findFirst()
                .orElseThrow(() -> new ExistException("User not exist with ID: " + id));
    }

    @Override
    public User update(User user) throws ExistException {
        if (!contains(user)) {
            throw new ExistException("User not exist with ID: " + user.getId());
        }
        users.replace(user.getId(), user);
        log.info("User with id:{} has been updated", user.getId());
        return users.get(user.getId());
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public List<User> findFriendsForUserById(long userId) throws ExistException {
        return findById(userId)
                .getFriendsId().stream().map(id -> {
                    try {
                        return findById(id);
                    } catch (ExistException e) {
                        log.warn("User with id:{} has unavailable friend with id:{}", userId, id);
                        throw new IllegalStateException("User with id: " + id + " not present");
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriendsForUsersById(long userId, long otherUserId) throws ExistException {
        User user = findById(userId);
        User otherUser = findById(otherUserId);

        List<Long> commonFriendsId = user.getFriendsId().stream()
                .filter(id -> otherUser.getFriendsId().contains(id)).toList();

        if (!commonFriendsId.isEmpty()) {
            return commonFriendsId.stream().map(id -> {
                try {
                    return findById(id);
                } catch (ExistException e) {
                    log.warn("Users id:{} and {} has unavailable friend with id:{}", userId, otherUser, id);
                    throw new IllegalStateException("User with id:" + id + " not present");
                }
            }).collect(Collectors.toList());
        }
        return List.of();
    }

    private boolean contains(User user) {
        Predicate<User> predicate =
                !Objects.isNull(user.getId()) ?
                        (u) -> u.getId().equals(user.getId()) :
                        (u) -> u.getLogin().equals(user.getLogin()) || u.getEmail().equals(user.getEmail());

        return users.values().stream().anyMatch(predicate);
    }

    private Long generate() {
        return users.keySet().stream().mapToLong(l -> l).max().orElse(0L) + 1;
    }
}