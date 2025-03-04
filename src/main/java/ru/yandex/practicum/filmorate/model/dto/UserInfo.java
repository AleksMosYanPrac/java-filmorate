package ru.yandex.practicum.filmorate.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserInfo {

    private long id;
    private String name;

    @JsonProperty("user_friends")
    private Set<Friend> friends;

    public void setFriends(Set<Long> friendsId) {
        this.friends = friendsId.stream().map(Friend::new).collect(Collectors.toSet());
    }

    private record Friend(long id) {
    }
}