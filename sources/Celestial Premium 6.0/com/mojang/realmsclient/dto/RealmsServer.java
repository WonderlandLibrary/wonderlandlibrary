/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.common.collect.ComparisonChain;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.dto.PlayerInfo;
import com.mojang.realmsclient.dto.RealmsServerPing;
import com.mojang.realmsclient.dto.RealmsServerPlayerList;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import com.mojang.realmsclient.dto.ValueObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.realms.Realms;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsServer
extends ValueObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public long id;
    public String remoteSubscriptionId;
    public String name;
    public String motd;
    public State state;
    public String owner;
    public String ownerUUID;
    public List<PlayerInfo> players;
    public Map<Integer, RealmsWorldOptions> slots;
    public boolean expired;
    public boolean expiredTrial;
    public int daysLeft;
    public WorldType worldType;
    public int activeSlot;
    public String minigameName;
    public int minigameId;
    public String minigameImage;
    public RealmsServerPing serverPing = new RealmsServerPing();

    public String getDescription() {
        return this.motd;
    }

    public String getName() {
        return this.name;
    }

    public String getMinigameName() {
        return this.minigameName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String motd) {
        this.motd = motd;
    }

    public void updateServerPing(RealmsServerPlayerList serverPlayerList) {
        StringBuilder builder = new StringBuilder();
        int players = 0;
        for (String uuid : serverPlayerList.players) {
            if (uuid.equals(Realms.getUUID())) continue;
            String name = "";
            try {
                name = RealmsUtil.uuidToName(uuid);
            }
            catch (Exception exception) {
                LOGGER.error("Could not get name for " + uuid, (Throwable)exception);
                continue;
            }
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(name);
            ++players;
        }
        this.serverPing.nrOfPlayers = String.valueOf(players);
        this.serverPing.playerList = builder.toString();
    }

    public static RealmsServer parse(JsonObject node) {
        RealmsServer server = new RealmsServer();
        try {
            server.id = JsonUtils.getLongOr("id", node, -1L);
            server.remoteSubscriptionId = JsonUtils.getStringOr("remoteSubscriptionId", node, null);
            server.name = JsonUtils.getStringOr("name", node, null);
            server.motd = JsonUtils.getStringOr("motd", node, null);
            server.state = RealmsServer.getState(JsonUtils.getStringOr("state", node, State.CLOSED.name()));
            server.owner = JsonUtils.getStringOr("owner", node, null);
            if (node.get("players") != null && node.get("players").isJsonArray()) {
                server.players = RealmsServer.parseInvited(node.get("players").getAsJsonArray());
                RealmsServer.sortInvited(server);
            } else {
                server.players = new ArrayList<PlayerInfo>();
            }
            server.daysLeft = JsonUtils.getIntOr("daysLeft", node, 0);
            server.expired = JsonUtils.getBooleanOr("expired", node, false);
            server.expiredTrial = JsonUtils.getBooleanOr("expiredTrial", node, false);
            server.worldType = RealmsServer.getWorldType(JsonUtils.getStringOr("worldType", node, WorldType.NORMAL.name()));
            server.ownerUUID = JsonUtils.getStringOr("ownerUUID", node, "");
            server.slots = node.get("slots") != null && node.get("slots").isJsonArray() ? RealmsServer.parseSlots(node.get("slots").getAsJsonArray()) : RealmsServer.getEmptySlots();
            server.minigameName = JsonUtils.getStringOr("minigameName", node, null);
            server.activeSlot = JsonUtils.getIntOr("activeSlot", node, -1);
            server.minigameId = JsonUtils.getIntOr("minigameId", node, -1);
            server.minigameImage = JsonUtils.getStringOr("minigameImage", node, null);
        }
        catch (Exception e) {
            LOGGER.error("Could not parse McoServer: " + e.getMessage());
        }
        return server;
    }

    private static void sortInvited(RealmsServer server) {
        Collections.sort(server.players, new Comparator<PlayerInfo>(){

            @Override
            public int compare(PlayerInfo o1, PlayerInfo o2) {
                return ComparisonChain.start().compare(o2.getAccepted(), o1.getAccepted()).compare((Comparable<?>)((Object)o1.getName().toLowerCase(Locale.ROOT)), (Comparable<?>)((Object)o2.getName().toLowerCase(Locale.ROOT))).result();
            }
        });
    }

    private static List<PlayerInfo> parseInvited(JsonArray jsonArray) {
        ArrayList<PlayerInfo> invited = new ArrayList<PlayerInfo>();
        for (JsonElement aJsonArray : jsonArray) {
            try {
                JsonObject node = aJsonArray.getAsJsonObject();
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setName(JsonUtils.getStringOr("name", node, null));
                playerInfo.setUuid(JsonUtils.getStringOr("uuid", node, null));
                playerInfo.setOperator(JsonUtils.getBooleanOr("operator", node, false));
                playerInfo.setAccepted(JsonUtils.getBooleanOr("accepted", node, false));
                playerInfo.setOnline(JsonUtils.getBooleanOr("online", node, false));
                invited.add(playerInfo);
            }
            catch (Exception exception) {}
        }
        return invited;
    }

    private static Map<Integer, RealmsWorldOptions> parseSlots(JsonArray jsonArray) {
        HashMap<Integer, RealmsWorldOptions> slots = new HashMap<Integer, RealmsWorldOptions>();
        for (JsonElement aJsonArray : jsonArray) {
            try {
                JsonObject node = aJsonArray.getAsJsonObject();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(node.get("options").getAsString());
                RealmsWorldOptions options = element == null ? RealmsWorldOptions.getDefaults() : RealmsWorldOptions.parse(element.getAsJsonObject());
                int slot = JsonUtils.getIntOr("slotId", node, -1);
                slots.put(slot, options);
            }
            catch (Exception exception) {}
        }
        for (int i = 1; i <= 3; ++i) {
            if (slots.containsKey(i)) continue;
            slots.put(i, RealmsWorldOptions.getEmptyDefaults());
        }
        return slots;
    }

    private static Map<Integer, RealmsWorldOptions> getEmptySlots() {
        HashMap<Integer, RealmsWorldOptions> slots = new HashMap<Integer, RealmsWorldOptions>();
        slots.put(1, RealmsWorldOptions.getEmptyDefaults());
        slots.put(2, RealmsWorldOptions.getEmptyDefaults());
        slots.put(3, RealmsWorldOptions.getEmptyDefaults());
        return slots;
    }

    public static RealmsServer parse(String json) {
        RealmsServer server = new RealmsServer();
        try {
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(json).getAsJsonObject();
            server = RealmsServer.parse(object);
        }
        catch (Exception e) {
            LOGGER.error("Could not parse McoServer: " + e.getMessage());
        }
        return server;
    }

    private static State getState(String state) {
        try {
            return State.valueOf(state);
        }
        catch (Exception ignored) {
            return State.CLOSED;
        }
    }

    private static WorldType getWorldType(String state) {
        try {
            return WorldType.valueOf(state);
        }
        catch (Exception ignored) {
            return WorldType.NORMAL;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.motd).append((Object)this.state).append(this.owner).append(this.expired).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        RealmsServer rhs = (RealmsServer)obj;
        return new EqualsBuilder().append(this.id, rhs.id).append(this.name, rhs.name).append(this.motd, rhs.motd).append((Object)this.state, (Object)rhs.state).append(this.owner, rhs.owner).append(this.expired, rhs.expired).append((Object)this.worldType, (Object)this.worldType).isEquals();
    }

    public RealmsServer clone() {
        RealmsServer server = new RealmsServer();
        server.id = this.id;
        server.remoteSubscriptionId = this.remoteSubscriptionId;
        server.name = this.name;
        server.motd = this.motd;
        server.state = this.state;
        server.owner = this.owner;
        server.players = this.players;
        server.slots = this.cloneSlots(this.slots);
        server.expired = this.expired;
        server.expiredTrial = this.expiredTrial;
        server.daysLeft = this.daysLeft;
        server.serverPing = new RealmsServerPing();
        server.serverPing.nrOfPlayers = this.serverPing.nrOfPlayers;
        server.serverPing.playerList = this.serverPing.playerList;
        server.worldType = this.worldType;
        server.ownerUUID = this.ownerUUID;
        server.minigameName = this.minigameName;
        server.activeSlot = this.activeSlot;
        server.minigameId = this.minigameId;
        server.minigameImage = this.minigameImage;
        return server;
    }

    public Map<Integer, RealmsWorldOptions> cloneSlots(Map<Integer, RealmsWorldOptions> slots) {
        HashMap<Integer, RealmsWorldOptions> newSlots = new HashMap<Integer, RealmsWorldOptions>();
        for (Map.Entry<Integer, RealmsWorldOptions> entry : slots.entrySet()) {
            newSlots.put(entry.getKey(), entry.getValue().clone());
        }
        return newSlots;
    }

    public static enum WorldType {
        NORMAL,
        MINIGAME,
        ADVENTUREMAP,
        EXPERIENCE,
        INSPIRATION;

    }

    public static enum State {
        CLOSED,
        OPEN,
        UNINITIALIZED;

    }

    public static class McoServerComparator
    implements Comparator<RealmsServer> {
        private final String refOwner;

        public McoServerComparator(String owner) {
            this.refOwner = owner;
        }

        @Override
        public int compare(RealmsServer server1, RealmsServer server2) {
            return ComparisonChain.start().compareTrueFirst(server1.state.equals((Object)State.UNINITIALIZED), server2.state.equals((Object)State.UNINITIALIZED)).compareTrueFirst(server1.expiredTrial, server2.expiredTrial).compareTrueFirst(server1.owner.equals(this.refOwner), server2.owner.equals(this.refOwner)).compareFalseFirst(server1.expired, server2.expired).compareTrueFirst(server1.state.equals((Object)State.OPEN), server2.state.equals((Object)State.OPEN)).compare(server1.id, server2.id).result();
        }
    }
}

