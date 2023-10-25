/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServerPlayerList {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final JsonParser jsonParser = new JsonParser();
    public long serverId;
    public List<String> players;

    public static RealmsServerPlayerList parse(JsonObject node) {
        RealmsServerPlayerList playerList = new RealmsServerPlayerList();
        try {
            JsonElement element;
            playerList.serverId = JsonUtils.getLongOr("serverId", node, -1L);
            String playerListString = JsonUtils.getStringOr("playerList", node, null);
            playerList.players = playerListString != null ? ((element = jsonParser.parse(playerListString)).isJsonArray() ? RealmsServerPlayerList.parsePlayers(element.getAsJsonArray()) : new ArrayList<String>()) : new ArrayList<String>();
        }
        catch (Exception e) {
            LOGGER.error("Could not parse RealmsServerPlayerList: " + e.getMessage());
        }
        return playerList;
    }

    private static List<String> parsePlayers(JsonArray jsonArray) {
        ArrayList<String> players = new ArrayList<String>();
        for (JsonElement aJsonArray : jsonArray) {
            try {
                players.add(aJsonArray.getAsString());
            }
            catch (Exception exception) {}
        }
        return players;
    }
}

