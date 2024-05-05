package intent.AquaDev.aqua.utils;

import java.util.ArrayList;
import net.minecraft.entity.Entity;

public class FriendSystem {
    public static ArrayList<String> friends = new ArrayList();

    public static void addFriend(String name) {
        System.out.println("Added " + name);
        friends.add((Object)name);
    }

    public static void removeFriend(String name) {
        System.out.println("Removed " + name);
        friends.remove((Object)name);
    }

    public static ArrayList<String> getFriends() {
        return friends;
    }

    public static boolean isFriendEntity(Entity e) {
        return friends.contains((Object)e.getName());
    }

    public static boolean isFriendString(String name) {
        return friends.contains((Object)name);
    }

    public static void clearFriends() {
        System.out.println("Cleared friend list");
        friends.clear();
    }
}
