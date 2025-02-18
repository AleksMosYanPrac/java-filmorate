package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorageImpl implements UserStorage {

    private final Map<Long, User> users;

    @Override
    public User add(User user) {
        User newUser = user.toBuilder().id(generate()).build();
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public Optional<User> findById(long id) {
        return users.values().stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public User update(User user) {
        users.replace(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public boolean contains(Predicate<User> predicate) {
        return users.values().stream().anyMatch(predicate);
    }

    private Long generate() {
        return users.keySet().stream().mapToLong(l -> l).max().orElse(0L) + 1;
    }
}