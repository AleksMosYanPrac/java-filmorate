package ru.yandex.practicum.filmorate.service.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserService userService;

    @Override
    public void addToFriends(long userId, long friendId) throws ExistException {
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);

        if (user.addFriend(friend) && friend.addFriend(user)) {
            log.info("User with id:{} add friend with id:{}", userId, friendId);
            userService.update(user);
            userService.update(friend);
        } else {
            log.info("Users with id:{} and id:{} already friends", userId, friendId);
        }
    }

    @Override
    public void removeFromFriends(long userId, long friendId) throws ExistException {
        User user = userService.getById(userId);
        User friend = userService.getById(friendId);

        if (user.removeFriend(friend) && friend.removeFriend(user)) {
            log.info("User with id:{} remove friend with id:{}", userId, friendId);
            userService.update(user);
            userService.update(friend);
        } else {
            log.info("Users with id:{} and id:{} has been not friends", userId, friendId);
        }
    }

    @Override
    public List<User> listFriends(long userId) throws ExistException {
        return userService.getById(userId).getFriendsId().stream().map(id -> {
            try {
                return userService.getById(id);
            } catch (ExistException e) {
                log.warn("User with id:{} has unavailable friend with id:{}", userId, id);
                throw new IllegalStateException("User with id:" + id + " not present");
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<User> listCommonFriends(long userId, long otherUserId) throws ExistException {
        User user = userService.getById(userId);
        User otherUser = userService.getById(otherUserId);

        List<Long> commonFriendsId = user.getFriendsId().stream()
                .filter(id -> otherUser.getFriendsId().contains(id)).toList();

        if (!commonFriendsId.isEmpty()) {
            return commonFriendsId.stream().map(id -> {
                try {
                    return userService.getById(id);
                } catch (ExistException e) {
                    log.warn("Users id:{} and {} has unavailable friend with id:{}", userId, otherUser, id);
                    throw new IllegalStateException("User with id:" + id + " not present");
                }
            }).collect(Collectors.toList());
        }

        return List.of();
    }
}