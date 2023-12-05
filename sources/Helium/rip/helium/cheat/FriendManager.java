package rip.helium.cheat;

import java.util.ArrayList;

public class FriendManager {

    public static ArrayList<String> friends = new ArrayList<>();

    public static boolean isFriend(String name) {
        return friends.contains(name);
    }

}
