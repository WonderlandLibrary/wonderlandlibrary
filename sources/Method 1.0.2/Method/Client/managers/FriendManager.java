package Method.Client.managers;


import Method.Client.utils.visual.ChatUtils;

import java.util.ArrayList;

public class FriendManager {

    public static ArrayList<String> friendsList = new ArrayList<>();

    public static void addFriend(String friendname) {
        if (!friendsList.contains(friendname)) {
            friendsList.add(friendname);
            FileManager.saveData(FileManager.files[3]);
            ChatUtils.message(friendname + " Added to \u00a7bfriends \u00a77list.");
        }
    }

    public static void removeFriend(String friendname) {
        if (friendsList.contains(friendname)) {
            friendsList.remove(friendname);
            FileManager.saveData(FileManager.files[3]);
            ChatUtils.message(friendname + " Removed from \u00a7bfriends \u00a77list.");
        }
    }

    public static void clear() {
        if (!friendsList.isEmpty()) {
            friendsList.clear();
            FileManager.saveData(FileManager.files[3]);
            ChatUtils.message("\u00a7bFriends \u00a77list clear.");
        }
    }

    public static boolean isFriend(String name) {
        return friendsList.contains(name);
    }
}
