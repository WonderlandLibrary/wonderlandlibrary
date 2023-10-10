/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.properties.Property
 *  com.mojang.util.UUIDTypeAdapter
 */
package com.wallhacks.losebypass.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import com.wallhacks.losebypass.utils.MC;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.io.IOUtils;

public class SessionUtils
implements MC {
    private static final HashMap<UUID, Property> skinMap = new HashMap();
    private static final HashMap<String, UUID> knownPlayerName = new HashMap();
    private static final HashMap<UUID, String> knownPlayerUUID = new HashMap();

    public static boolean setSkin(NetworkPlayerInfo info, UUID uuid) {
        Property p;
        if (uuid == null) {
            info.loadPlayerTextures();
            return true;
        }
        if (skinMap.containsKey(uuid)) {
            p = skinMap.get(uuid);
        } else {
            p = SessionUtils.getTexture(uuid);
            skinMap.put(uuid, p);
        }
        if (p == null) return false;
        info.getGameProfile().getProperties().put((Object)"textures", (Object)p);
        info.playerTexturesLoaded = false;
        info.loadPlayerTextures();
        return true;
    }

    public static UUID fromString(String uuid) {
        try {
            return UUIDTypeAdapter.fromString((String)uuid);
        }
        catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static Property getTexture(UUID uuid) {
        try {
            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();
            return new Property("textures", texture, signature);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static UUID getid(String name) {
        if (name == null) {
            return null;
        }
        if (knownPlayerName.containsKey(name)) {
            return knownPlayerName.get(name);
        }
        try {
            JsonElement root;
            URLConnection request = new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            try {
                root = jp.parse(new InputStreamReader((InputStream)request.getContent()));
            }
            catch (IOException fucked) {
                knownPlayerName.put(name, null);
                return null;
            }
            JsonObject rootobj = root.getAsJsonObject();
            String id = rootobj.get("id").getAsString();
            id = UUID.fromString(id.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5")).toString();
            UUID uuid = UUID.fromString(id);
            knownPlayerUUID.put(uuid, name);
            knownPlayerName.put(name, uuid);
            return uuid;
        }
        catch (Exception e) {
            knownPlayerName.put(name, null);
            e.printStackTrace();
            return null;
        }
    }

    public static List<EntityPlayer> getTabPlayerList() {
        ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
        List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(SessionUtils.mc.thePlayer.sendQueue.getPlayerInfoMap());
        Iterator<NetworkPlayerInfo> iterator = players.iterator();
        while (iterator.hasNext()) {
            NetworkPlayerInfo info = iterator.next();
            if (info == null) continue;
            list.add(SessionUtils.mc.theWorld.getPlayerEntityByName(info.gameProfile.getName()));
        }
        return list;
    }

    public static String getname(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        if (knownPlayerUUID.containsKey(uuid)) {
            return knownPlayerUUID.get(uuid);
        }
        try {
            URL url = new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names");
            URLConnection request = url.openConnection();
            request.connect();
            String nameJson = IOUtils.toString(url);
            JsonParser jp = new JsonParser();
            JsonArray nameValue = (JsonArray)jp.parse(nameJson);
            String playerSlot = nameValue.get(nameValue.size() - 1).toString();
            JsonObject nameObject = (JsonObject)jp.parse(playerSlot);
            String n = nameObject.get("name").toString().replaceAll("\"", "");
            knownPlayerUUID.put(uuid, n);
            knownPlayerName.put(n, uuid);
            return n;
        }
        catch (Exception e) {
            knownPlayerUUID.put(uuid, null);
            return null;
        }
    }
}

