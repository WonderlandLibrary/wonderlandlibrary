/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.dto.RealmsServerPlayerList;
import com.mojang.realmsclient.dto.ValueObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServerPlayerLists
extends ValueObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public List<RealmsServerPlayerList> servers;

    public static RealmsServerPlayerLists parse(String json) {
        RealmsServerPlayerLists list = new RealmsServerPlayerLists();
        list.servers = new ArrayList<RealmsServerPlayerList>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(json).getAsJsonObject();
            if (object.get("lists").isJsonArray()) {
                JsonArray jsonArray = object.get("lists").getAsJsonArray();
                Iterator<JsonElement> it = jsonArray.iterator();
                while (it.hasNext()) {
                    list.servers.add(RealmsServerPlayerList.parse(it.next().getAsJsonObject()));
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("Could not parse RealmsServerPlayerLists: " + e.getMessage());
        }
        return list;
    }
}

