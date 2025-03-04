package ru.yandex.practicum.filmorate.mapping.impls;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapping.UserMapper;
import ru.yandex.practicum.filmorate.model.dto.UserData;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserData toUserData(User user) {
        return new UserData(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday());
    }

    @Override
    public UserInfo toUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setFriends(user.getFriendsId());
        return userInfo;
    }

    @Override
    public User toUser(UserData userData) {
        return User.builder()
                .id(userData.getId())
                .login(userData.getLogin())
                .email(userData.getEmail())
                .name(userData.getName())
                .birthday(userData.getBirthday())
                .build();
    }
}