package wtf.evolution.helpers;

import java.util.ArrayList;

public class FriendManager {

    public ArrayList<String> friends = new ArrayList<>();

    public void add(String name) {
        friends.add(name);
    }

    public void remove(String name) {
        friends.remove(name);
    }

    public boolean isFriend(String name) {
        return friends.contains(name);
    }




}
