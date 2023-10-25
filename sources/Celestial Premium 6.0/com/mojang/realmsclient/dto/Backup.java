/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.dto.ValueObject;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Backup
extends ValueObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public String backupId;
    public Date lastModifiedDate;
    public long size;
    private boolean uploadedVersion;
    public Map<String, String> metadata = new HashMap<String, String>();
    public Map<String, String> changeList = new HashMap<String, String>();

    public static Backup parse(JsonElement node) {
        JsonObject object = node.getAsJsonObject();
        Backup backup = new Backup();
        try {
            backup.backupId = JsonUtils.getStringOr("backupId", object, "");
            backup.lastModifiedDate = JsonUtils.getDateOr("lastModifiedDate", object);
            backup.size = JsonUtils.getLongOr("size", object, 0L);
            if (object.has("metadata")) {
                JsonObject metadataObject = object.getAsJsonObject("metadata");
                Set<Map.Entry<String, JsonElement>> jsonElementSet = metadataObject.entrySet();
                for (Map.Entry<String, JsonElement> elem : jsonElementSet) {
                    if (elem.getValue().isJsonNull()) continue;
                    backup.metadata.put(Backup.format(elem.getKey()), elem.getValue().getAsString());
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("Could not parse Backup: " + e.getMessage());
        }
        return backup;
    }

    private static String format(String key) {
        String[] worlds = key.split("_");
        StringBuilder sb = new StringBuilder();
        for (String world : worlds) {
            if (world == null || world.length() < 1) continue;
            if ("of".equals(world)) {
                sb.append(world).append(" ");
                continue;
            }
            char firstCharacter = Character.toUpperCase(world.charAt(0));
            sb.append(firstCharacter).append(world.substring(1, world.length())).append(" ");
        }
        return sb.toString();
    }

    public boolean isUploadedVersion() {
        return this.uploadedVersion;
    }

    public void setUploadedVersion(boolean uploadedVersion) {
        this.uploadedVersion = uploadedVersion;
    }
}

