package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.UserMapper;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public void addToFriends(long userId, long friendId) throws ExistException {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        if (user.addFriend(friend) && friend.addFriend(user)) {
            userStorage.update(user);
            userStorage.update(friend);
            log.info("User with id:{} add friend with id:{}", userId, friendId);
        } else {
            log.info("Users with id:{} and id:{} already friends", userId, friendId);
        }
    }

    @Override
    public void removeFromFriends(long userId, long friendId) throws ExistException {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        if (user.removeFriend(friend) && friend.removeFriend(user)) {
            userStorage.update(user);
            userStorage.update(friend);
            log.info("User with id:{} remove friend with id:{}", userId, friendId);
        } else {
            log.info("Users with id:{} and id:{} has been not friends", userId, friendId);
        }
    }

    @Override
    public List<UserInfo> listFriends(long userId) throws ExistException {
        return userStorage.findFriendsForUserById(userId)
                .stream()
                .map(userMapper::toUserInfo)
                .toList();
    }

    @Override
    public List<UserInfo> listCommonFriends(long userId, long otherUserId) throws ExistException {
        return userStorage.findCommonFriendsForUsersById(userId, otherUserId)
                .stream()
                .map(userMapper::toUserInfo)
                .toList();
    }
}