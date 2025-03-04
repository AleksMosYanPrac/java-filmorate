package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.dto.UserInfo;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("${filmorate.endpoints.users}")
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class FriendController {

    private final FriendService friendService;

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(OK)
    void addToFriends(@PathVariable long userId, @PathVariable long friendId) throws ExistException {
        friendService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(OK)
    void removeFromFriends(@PathVariable long userId, @PathVariable long friendId) throws ExistException {
        friendService.removeFromFriends(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(OK)
    List<UserInfo> listOfFriends(@PathVariable long userId) throws ExistException {
        return friendService.listFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    @ResponseStatus(OK)
    List<UserInfo> listOfCommonFriends(@PathVariable long userId,
                                       @PathVariable long otherUserId) throws ExistException {
        return friendService.listCommonFriends(userId, otherUserId);
    }
}