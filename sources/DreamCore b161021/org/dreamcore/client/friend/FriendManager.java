package org.dreamcore.client.friend;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {

    private final List<Friend> friends = new ArrayList<>();

    public void addFriend(Friend friend) {
        this.friends.add(friend);
    }

    public void addFriend(String name) {
        friends.add(new Friend(name));
    }

    public boolean isFriend(String friend) {
        return friends.stream().anyMatch(isFriend -> isFriend.getName().equals(friend));
    }

    public void removeFriend(String name) {
        friends.removeIf(friend -> friend.getName().equalsIgnoreCase(name));
    }

    public List<Friend> getFriends() {
        return this.friends;
    }

    public Friend getFriend(String friend) {
        return friends.stream().filter(isFriend -> isFriend.getName().equals(friend)).findFirst().get();
    }
}
