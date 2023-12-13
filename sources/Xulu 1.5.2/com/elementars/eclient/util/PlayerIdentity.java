// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.net.URLConnection;
import java.util.Calendar;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.io.Serializable;

public class PlayerIdentity implements Serializable
{
    private String displayName;
    private String stringUuid;
    private Long lastUpdated;
    
    public PlayerIdentity(final String stringUuid) {
        final String formattedUuid = stringUuid.replace("-", "");
        this.stringUuid = stringUuid;
        this.displayName = "Loading...";
        new Thread(() -> {
            this.displayName = getName(formattedUuid);
            DataManager.identityCacheMap.put(this.getStringUuid(), this);
            try {
                DataManager.savePlayerIdentity(this, false);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }).start();
        this.lastUpdated = System.currentTimeMillis();
    }
    
    private static String getName(final String UUID) {
        final LinkedHashMap<String, Long> nameHistories = new LinkedHashMap<String, Long>();
        try {
            final URL e = new URL("https://api.mojang.com/user/profiles/" + UUID.replace("-", "") + "/names");
            final URLConnection connection = e.openConnection();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder jsonb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonb.append(line + "\n");
            }
            final String formattedjson = jsonb.toString();
            reader.close();
            final JsonArray array = new JsonParser().parse(formattedjson).getAsJsonArray();
            final JsonObject obj = array.get(array.size() - 1).getAsJsonObject();
            final String nameform = obj.get("name").getAsString();
            try {
                obj.get("changedToAt");
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(obj.get("changedToAt").getAsLong());
                final long changedAt = obj.get("changedToAt").getAsLong();
                final int mYear = calendar.get(1);
                final int mMonth = calendar.get(2);
                final int mDay = calendar.get(5);
                nameHistories.put(nameform, changedAt);
                return nameform;
            }
            catch (Exception ee) {
                return nameform;
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            System.out.print("fuck");
            return UUID;
        }
    }
    
    private static String getDateFormat(final int mm, final int dd, final int yyyy) {
        return mm + "/" + dd + "/" + yyyy;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public String getStringUuid() {
        return this.stringUuid;
    }
    
    public void updateDisplayName() {
        final PlayerIdentity identity;
        final PlayerIdentity identity2;
        new Thread(() -> {
            identity = new PlayerIdentity(this.stringUuid);
            this.displayName = identity.getDisplayName();
            this.lastUpdated = System.currentTimeMillis();
            identity2 = null;
        }).start();
    }
    
    public boolean shouldUpdate() {
        return System.currentTimeMillis() - this.lastUpdated >= 6.048E8;
    }
}
