package ru.yandex.practicum.filmorate.mapping;

import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.User;

public interface UserMapper {

    UserData toUserData(User user);

    UserInfo toUserInfo(User user);

    User toUser(UserData userData);
}