// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.friend;

import java.util.ArrayList;
import java.util.List;

public class FriendManager
{
    private final List<Friend> friends;
    
    public FriendManager() {
        this.friends = new ArrayList<Friend>();
    }
    
    public void addFriend(final Friend friend) {
        this.friends.add(friend);
    }
    
    public void addFriend(final String name) {
        this.friends.add(new Friend(name));
    }
    
    public boolean isFriend(final String friend) {
        return this.friends.stream().anyMatch(isFriend -> isFriend.getName().equals(friend));
    }
    
    public void removeFriend(final String name) {
        this.friends.removeIf(friend -> friend.getName().equalsIgnoreCase(name));
    }
    
    public List<Friend> getFriends() {
        return this.friends;
    }
    
    public Friend getFriend(final String friend) {
        return this.friends.stream().filter(isFriend -> isFriend.getName().equals(friend)).findFirst().get();
    }
}
