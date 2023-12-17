// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.managers;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.io.File;
import java.util.List;

public class FriendManager
{
    private final List<Friend> friends;
    public static final File friendFile;
    
    public FriendManager() {
        this.friends = new ArrayList<Friend>();
    }
    
    public void init() throws Exception {
        if (!FriendManager.friendFile.exists()) {
            FriendManager.friendFile.createNewFile();
        }
        else {
            this.readFriends();
        }
    }
    
    public void addFriend(final String name) {
        this.friends.add(new Friend(name));
        this.updateFile();
    }
    
    public boolean isFriend(final String friend) {
        return this.friends.stream().anyMatch(isFriend -> isFriend.getName().equals(friend));
    }
    
    public void removeFriend(final String name) {
        this.friends.removeIf(friend -> friend.getName().equalsIgnoreCase(name));
    }
    
    public void clearFriend() {
        this.friends.clear();
        this.updateFile();
    }
    
    public List<Friend> getFriends() {
        return this.friends;
    }
    
    public void updateFile() {
        try {
            final StringBuilder builder = new StringBuilder();
            this.friends.forEach(friend -> builder.append(friend.getName()).append("\n"));
            Files.write(FriendManager.friendFile.toPath(), builder.toString().getBytes(), new OpenOption[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void readFriends() {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(FriendManager.friendFile.getAbsolutePath()))));
            String line;
            while ((line = reader.readLine()) != null) {
                this.friends.add(new Friend(line));
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        friendFile = new File("C:\\Minced\\game\\minced\\", "friends.pon");
    }
}
