package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.UserMapper;
import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserData create(UserData user) throws ExistException {
        return userMapper.toUserData(userStorage.create(userMapper.toUser(user)));
    }

    @Override
    public UserData update(UserData user) throws ExistException {
        User updatingUser = userStorage.findById(user.getId())
                .orElseThrow(() -> new ExistException("User not exist with ID:" + user.getId()))
                .toBuilder()
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .build();

        return userMapper.toUserData(userStorage.update(updatingUser));
    }

    @Override
    public List<UserData> list() {
        return userStorage.findAll().stream().map(userMapper::toUserData).toList();
    }

    @Override
    public UserInfo getById(long id) throws ExistException {
        return userMapper.toUserInfo(
                userStorage.findById(id).orElseThrow(() -> new ExistException("User not exist with ID:" + id))
        );
    }
}