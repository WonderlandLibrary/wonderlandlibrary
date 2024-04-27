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
    private final List friends = new ArrayList();

    public boolean addFriend(String string) {
        return this.addFriend(string, string);
    }

    public FriendsConfig(File file) {
        super(file);
    }

    public List getFriends() {
        return this.friends;
    }

    public void clearFriends() {
        this.friends.clear();
    }

    @Override
    protected void loadConfig() throws IOException {
        this.clearFriends();
        try {
            JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(this.getFile())));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
                JsonObject jsonObject = jsonElement2.getAsJsonObject();
                this.addFriend(jsonObject.get("playerName").getAsString(), jsonObject.get("alias").getAsString());
            }
        }
        catch (JsonSyntaxException | IllegalStateException throwable) {
            String string;
            ClientUtils.getLogger().info("[FileManager] Try to load old Friends config...");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));
            while ((string = bufferedReader.readLine()) != null) {
                if (string.contains("{") || string.contains("}")) continue;
                if ((string = string.replace(" ", "").replace("\"", "").replace(",", "")).contains(":")) {
                    String[] stringArray = string.split(":");
                    this.addFriend(stringArray[0], stringArray[1]);
                    continue;
                }
                this.addFriend(string);
            }
            bufferedReader.close();
            ClientUtils.getLogger().info("[FileManager] Loaded old Friends config...");
            this.saveConfig();
            ClientUtils.getLogger().info("[FileManager] Saved Friends to new config...");
        }
    }

    private static boolean lambda$removeFriend$0(String string, Friend friend) {
        return friend.getPlayerName().equals(string);
    }

    public boolean removeFriend(String string) {
        FriendsConfig friendsConfig = this;
        if (string != false) {
            return false;
        }
        this.friends.removeIf(arg_0 -> FriendsConfig.lambda$removeFriend$0(string, arg_0));
        return true;
    }

    public boolean addFriend(String string, String string2) {
        FriendsConfig friendsConfig = this;
        if (string != false) {
            return false;
        }
        this.friends.add(new Friend(this, string, string2));
        return true;
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonArray jsonArray = new JsonArray();
        for (Friend friend : this.getFriends()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("playerName", friend.getPlayerName());
            jsonObject.addProperty("alias", friend.getAlias());
            jsonArray.add((JsonElement)jsonObject);
        }
        PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonArray));
        printWriter.close();
    }

    public class Friend {
        private final String playerName;
        private final String alias;
        final FriendsConfig this$0;

        Friend(FriendsConfig friendsConfig, String string, String string2) {
            this.this$0 = friendsConfig;
            this.playerName = string;
            this.alias = string2;
        }

        public String getAlias() {
            return this.alias;
        }

        public String getPlayerName() {
            return this.playerName;
        }
    }
}

