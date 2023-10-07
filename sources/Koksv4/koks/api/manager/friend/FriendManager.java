package koks.api.manager.friend;

import lombok.Getter;

import java.util.HashMap;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

public class FriendManager {

    @Getter
    private final HashMap<String, String> friends = new HashMap<>();

    private static FriendManager instance;

    public static FriendManager getInstance() {
        if (instance == null) {
            instance = new FriendManager();
        }
        return instance;
    }

    public boolean isFriend(String name) {
        return friends.containsKey(name);
    }

    public void addFriend(String name, String alias) {
        friends.put(name, alias);
    }

    public void removeFriend(String name) {
        friends.remove(name);
    }

    public String getAlias(String name) {
        return friends.getOrDefault(name, "none");
    }

}
