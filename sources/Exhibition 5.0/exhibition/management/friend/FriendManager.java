// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.friend;

import java.util.List;
import exhibition.util.FileUtils;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.minecraft.util.StringUtils;
import java.util.ArrayList;
import java.io.File;

public class FriendManager
{
    private static final File FRIEND_DIR;
    public static ArrayList<Friend> friendsList;
    
    public static void start() {
        load();
        save();
    }
    
    public static void addFriend(final String name, final String alias) {
        FriendManager.friendsList.add(new Friend(name, alias));
        save();
    }
    
    public static String getAlias(final String name) {
        String alias = null;
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                alias = friend.alias;
                break;
            }
        }
        return alias;
    }
    
    public static void removeFriend(final String name) {
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(name)) {
                FriendManager.friendsList.remove(friend);
                break;
            }
        }
        save();
    }
    
    public static boolean isFriend(final String name) {
        boolean isFriend = false;
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                isFriend = true;
                break;
            }
        }
        if (Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name) {
            isFriend = true;
        }
        return isFriend;
    }
    
    public static void load() {
        FriendManager.friendsList.clear();
        final List<String> fileContent = FileUtils.read(FriendManager.FRIEND_DIR);
        for (final String line : fileContent) {
            try {
                final String[] split = line.split(":");
                final String name = split[0];
                final String alias = split[1];
                addFriend(name, alias);
            }
            catch (Exception ex) {}
        }
    }
    
    public static void save() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Friend friend : FriendManager.friendsList) {
            fileContent.add(String.format("%s:%s", friend.name, friend.alias));
        }
        FileUtils.write(FriendManager.FRIEND_DIR, fileContent, true);
    }
    
    static {
        FRIEND_DIR = FileUtils.getConfigFile("Friends");
        FriendManager.friendsList = new ArrayList<Friend>();
    }
}
