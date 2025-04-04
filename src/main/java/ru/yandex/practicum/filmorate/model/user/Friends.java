package ru.yandex.practicum.filmorate.model.user;

import lombok.Value;

import java.util.Map;
import java.util.Set;

@Value
public class Friends {

    Long userId;
    Map<Long, FriendshipStatus> friendIdAndStatus;

    public Set<Long> getFriendsId() {
        return friendIdAndStatus.keySet();
    }

    public boolean add(Long id) {
        if (friendIdAndStatus.containsKey(id)) {
            return false;
        } else {
            friendIdAndStatus.put(id, FriendshipStatus.REQUESTED);
            return true;
        }
    }

    public boolean remove(Long id) {
        if (friendIdAndStatus.containsKey(id)) {
            friendIdAndStatus.remove(id);
            return true;
        } else {
            return false;
        }
    }
}