package ru.yandex.practicum.filmorate.mapping;

import ru.yandex.practicum.filmorate.mapping.dto.UserData;
import ru.yandex.practicum.filmorate.mapping.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.User;

public interface UserMapper {

    UserData toUserData(User user);

    UserInfo toUserInfo(User user);

    User toUser(UserData userData);
}