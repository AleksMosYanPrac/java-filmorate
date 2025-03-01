package ru.yandex.practicum.filmorate.mapping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserInfo {

    long id;
    String name;

    @JsonProperty("user_friends")
    Set<Friend> friends;

    public void setFriends(Set<Long> friendsId) {
        this.friends = friendsId.stream().map(Friend::new).collect(Collectors.toSet());
    }

    private record Friend(long id) {
    }
}