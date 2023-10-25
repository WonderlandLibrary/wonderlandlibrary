/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.dto.ServerActivity;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;

public class ServerActivityList {
    public long periodInMillis;
    public List<ServerActivity> serverActivities = new ArrayList<ServerActivity>();

    public static ServerActivityList parse(String json) {
        ServerActivityList activityList = new ServerActivityList();
        JsonParser parser = new JsonParser();
        try {
            JsonElement jsonElement = parser.parse(json);
            JsonObject object = jsonElement.getAsJsonObject();
            activityList.periodInMillis = JsonUtils.getLongOr("periodInMillis", object, -1L);
            JsonElement activityArray = object.get("playerActivityDto");
            if (activityArray != null && activityArray.isJsonArray()) {
                JsonArray jsonArray = activityArray.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    ServerActivity sa = ServerActivity.parse(element.getAsJsonObject());
                    activityList.serverActivities.add(sa);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return activityList;
    }
}

