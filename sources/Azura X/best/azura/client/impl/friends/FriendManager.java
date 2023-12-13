package best.azura.client.impl.friends;

import best.azura.client.api.friend.Friend;

import java.util.ArrayList;

public class FriendManager {

    private final ArrayList<Friend> friendList = new ArrayList<>();

    public void addFriend(String name) {
        friendList.add(new Friend(name));
    }

    public void removeFriend(String name) {
        friendList.removeIf(f -> f.getName().equalsIgnoreCase(name));
    }

    public ArrayList<Friend> getFriendList() {
        return friendList;
    }

    public boolean isFriend(String name) {
        return friendList.stream().anyMatch(f -> f.getName().equals(name));
    }
}
