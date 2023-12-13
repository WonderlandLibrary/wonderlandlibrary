/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.friendsystem;

import java.util.ArrayList;
import java.util.List;
import me.wintware.client.friendsystem.Friend;

public class FriendManager {
    private final List<Friend> friends = new ArrayList<Friend>();

    public void addFriend(Friend paramFriend) {
        this.friends.add(paramFriend);
    }

    public boolean isFriend(String paramString) {
        return this.friends.stream().anyMatch(paramFriend -> paramFriend.getName().equals(paramString));
    }

    public void removeFriend(String name) {
        for (Friend friend : this.friends) {
            if (!friend.getName().equalsIgnoreCase(name)) continue;
            this.friends.remove(friend);
            break;
        }
    }

    public List<Friend> getFriends() {
        return this.friends;
    }

    public Friend getFriend(String friend) {
        return this.friends.stream().filter(paramFriend -> paramFriend.getName().equals(friend)).findFirst().get();
    }
}

