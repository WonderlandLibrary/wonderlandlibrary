/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonSyntaxException
 */
package net.ccbluex.liquidbounce.file.configs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public class FriendsConfig
extends FileConfig {
    private final List<Friend> friends = new ArrayList<Friend>();

    public FriendsConfig(File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {
        this.clearFriends();
        try {
            JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            for (JsonElement friendElement : jsonElement.getAsJsonArray()) {
                JsonObject friendObject = friendElement.getAsJsonObject();
                this.addFriend(friendObject.get("playerName").getAsString(), friendObject.get("alias").getAsString());
            }
        }
        catch (JsonSyntaxException | IllegalStateException ex) {
            String line;
            ClientUtils.getLogger().info("[FileManager] Try to load old Friends config...");
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
            ClientUtils.getLogger().info("[FileManager] Loaded old Friends config...");
            this.saveConfig();
            ClientUtils.getLogger().info("[FileManager] Saved Friends to new config...");
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonArray jsonArray = new JsonArray();
        for (Friend friend : this.getFriends()) {
            JsonObject friendObject = new JsonObject();
            friendObject.addProperty("playerName", friend.getPlayerName());
            friendObject.addProperty("alias", friend.getAlias());
            jsonArray.add((JsonElement)friendObject);
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonArray));
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

