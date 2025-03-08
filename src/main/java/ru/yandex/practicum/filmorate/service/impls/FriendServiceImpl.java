package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.mapping.IdMapper;
import ru.yandex.practicum.filmorate.mapping.UserMapper;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.model.user.Friends;
import ru.yandex.practicum.filmorate.repository.FriendsStorage;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendsStorage friendsStorage;
    private final IdMapper idMapper;
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public void addToFriends(long userId, long friendId) throws ExistException {
        Friends friends = friendsStorage.findById(idMapper.toUserId(userId));

        if (friends.add(idMapper.toUserId(friendId))) {
            friendsStorage.save(friends);
            log.info("User with id:{} add friend with id:{}", userId, friendId);
        } else {
            log.info("Users with id:{} and id:{} already friends", userId, friendId);
        }
    }

    @Override
    public void removeFromFriends(long userId, long friendId) throws ExistException {
        Friends friends = friendsStorage.findById(idMapper.toUserId(userId));

        if (friends.remove(idMapper.toUserId(friendId))) {
            friendsStorage.save(friends);
            log.info("User with id:{} remove friend with id:{}", userId, friendId);
        } else {
            log.info("Users with id:{} and id:{} has been not friends", userId, friendId);
        }
    }

    @Override
    public List<UserInfo> listFriends(long userId) throws ExistException {
        return userStorage.findFriendsForUserById(idMapper.toUserId(userId))
                .stream()
                .map(userMapper::toUserInfo)
                .toList();
    }

    @Override
    public List<UserInfo> listCommonFriends(long userId, long otherUserId) throws ExistException {
        return userStorage.findCommonFriendsForUsersById(idMapper.toUserId(userId), idMapper.toUserId(otherUserId))
                .stream()
                .map(userMapper::toUserInfo)
                .toList();
    }
}