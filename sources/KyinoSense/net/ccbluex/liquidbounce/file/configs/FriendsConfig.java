/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.file.configs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.file.FileConfig;

public class FriendsConfig
extends FileConfig {
    private final List<Friend> friends = new ArrayList<Friend>();

    public FriendsConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        String line;
        this.clearFriends();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("{") || line.contains("}")) continue;
            if ((line = line.replace(" ", "").replace("\"", "").replace(",", "")).contains(":")) {
                String[] data = line.split(":");
                this.addFriend(data[0], data[1]);
                continue;
            }
            this.addFriend(line);
        }
        bufferedReader.close();
    }

    @Override
    protected void saveConfig() throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        for (Friend friend : this.getFriends()) {
            printWriter.append(friend.getPlayerName()).append(":").append(friend.getAlias()).append("\n");
        }
        printWriter.close();
    }

    public boolean addFriend(String playerName) {
        return this.addFriend(playerName, playerName);
    }

    public boolean addFriend(String playerName, String alias) {
        if (this.isFriend(playerName)) {
            return false;
        }
        this.friends.add(new Friend(playerName, alias));
        return true;
    }

    public boolean removeFriend(String playerName) {
        if (!this.isFriend(playerName)) {
            return false;
        }
        this.friends.removeIf(friend -> friend.getPlayerName().equals(playerName));
        return true;
    }

    public boolean isFriend(String playerName) {
        for (Friend friend : this.friends) {
            if (!friend.getPlayerName().equals(playerName)) continue;
            return true;
        }
        return false;
    }

    public void clearFriends() {
        this.friends.clear();
    }

    public List<Friend> getFriends() {
        return this.friends;
    }

    public class Friend {
        private final String playerName;
        private final String alias;

        Friend(String playerName, String alias) {
            this.playerName = playerName;
            this.alias = alias;
        }

        public String getPlayerName() {
            return this.playerName;
        }

        public String getAlias() {
            return this.alias;
        }
    }
}

