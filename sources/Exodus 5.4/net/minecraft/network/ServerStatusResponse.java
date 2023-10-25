/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;

public class ServerStatusResponse {
    private String favicon;
    private PlayerCountData playerCount;
    private IChatComponent serverMotd;
    private MinecraftProtocolVersionIdentifier protocolVersion;

    public MinecraftProtocolVersionIdentifier getProtocolVersionInfo() {
        return this.protocolVersion;
    }

    public IChatComponent getServerDescription() {
        return this.serverMotd;
    }

    public void setPlayerCountData(PlayerCountData playerCountData) {
        this.playerCount = playerCountData;
    }

    public void setServerDescription(IChatComponent iChatComponent) {
        this.serverMotd = iChatComponent;
    }

    public PlayerCountData getPlayerCountData() {
        return this.playerCount;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public void setProtocolVersionInfo(MinecraftProtocolVersionIdentifier minecraftProtocolVersionIdentifier) {
        this.protocolVersion = minecraftProtocolVersionIdentifier;
    }

    public void setFavicon(String string) {
        this.favicon = string;
    }

    public static class MinecraftProtocolVersionIdentifier {
        private final String name;
        private final int protocol;

        public MinecraftProtocolVersionIdentifier(String string, int n) {
            this.name = string;
            this.protocol = n;
        }

        public int getProtocol() {
            return this.protocol;
        }

        public String getName() {
            return this.name;
        }

        public static class Serializer
        implements JsonDeserializer<MinecraftProtocolVersionIdentifier>,
        JsonSerializer<MinecraftProtocolVersionIdentifier> {
            public MinecraftProtocolVersionIdentifier deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "version");
                return new MinecraftProtocolVersionIdentifier(JsonUtils.getString(jsonObject, "name"), JsonUtils.getInt(jsonObject, "protocol"));
            }

            public JsonElement serialize(MinecraftProtocolVersionIdentifier minecraftProtocolVersionIdentifier, Type type, JsonSerializationContext jsonSerializationContext) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", minecraftProtocolVersionIdentifier.getName());
                jsonObject.addProperty("protocol", (Number)minecraftProtocolVersionIdentifier.getProtocol());
                return jsonObject;
            }
        }
    }

    public static class PlayerCountData {
        private final int onlinePlayerCount;
        private final int maxPlayers;
        private GameProfile[] players;

        public int getOnlinePlayerCount() {
            return this.onlinePlayerCount;
        }

        public GameProfile[] getPlayers() {
            return this.players;
        }

        public PlayerCountData(int n, int n2) {
            this.maxPlayers = n;
            this.onlinePlayerCount = n2;
        }

        public int getMaxPlayers() {
            return this.maxPlayers;
        }

        public void setPlayers(GameProfile[] gameProfileArray) {
            this.players = gameProfileArray;
        }

        public static class Serializer
        implements JsonDeserializer<PlayerCountData>,
        JsonSerializer<PlayerCountData> {
            public PlayerCountData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                JsonArray jsonArray;
                JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "players");
                PlayerCountData playerCountData = new PlayerCountData(JsonUtils.getInt(jsonObject, "max"), JsonUtils.getInt(jsonObject, "online"));
                if (JsonUtils.isJsonArray(jsonObject, "sample") && (jsonArray = JsonUtils.getJsonArray(jsonObject, "sample")).size() > 0) {
                    GameProfile[] gameProfileArray = new GameProfile[jsonArray.size()];
                    int n = 0;
                    while (n < gameProfileArray.length) {
                        JsonObject jsonObject2 = JsonUtils.getJsonObject(jsonArray.get(n), "player[" + n + "]");
                        String string = JsonUtils.getString(jsonObject2, "id");
                        gameProfileArray[n] = new GameProfile(UUID.fromString(string), JsonUtils.getString(jsonObject2, "name"));
                        ++n;
                    }
                    playerCountData.setPlayers(gameProfileArray);
                }
                return playerCountData;
            }

            public JsonElement serialize(PlayerCountData playerCountData, Type type, JsonSerializationContext jsonSerializationContext) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("max", (Number)playerCountData.getMaxPlayers());
                jsonObject.addProperty("online", (Number)playerCountData.getOnlinePlayerCount());
                if (playerCountData.getPlayers() != null && playerCountData.getPlayers().length > 0) {
                    JsonArray jsonArray = new JsonArray();
                    int n = 0;
                    while (n < playerCountData.getPlayers().length) {
                        JsonObject jsonObject2 = new JsonObject();
                        UUID uUID = playerCountData.getPlayers()[n].getId();
                        jsonObject2.addProperty("id", uUID == null ? "" : uUID.toString());
                        jsonObject2.addProperty("name", playerCountData.getPlayers()[n].getName());
                        jsonArray.add((JsonElement)jsonObject2);
                        ++n;
                    }
                    jsonObject.add("sample", (JsonElement)jsonArray);
                }
                return jsonObject;
            }
        }
    }

    public static class Serializer
    implements JsonDeserializer<ServerStatusResponse>,
    JsonSerializer<ServerStatusResponse> {
        public ServerStatusResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "status");
            ServerStatusResponse serverStatusResponse = new ServerStatusResponse();
            if (jsonObject.has("description")) {
                serverStatusResponse.setServerDescription((IChatComponent)jsonDeserializationContext.deserialize(jsonObject.get("description"), IChatComponent.class));
            }
            if (jsonObject.has("players")) {
                serverStatusResponse.setPlayerCountData((PlayerCountData)jsonDeserializationContext.deserialize(jsonObject.get("players"), PlayerCountData.class));
            }
            if (jsonObject.has("version")) {
                serverStatusResponse.setProtocolVersionInfo((MinecraftProtocolVersionIdentifier)jsonDeserializationContext.deserialize(jsonObject.get("version"), MinecraftProtocolVersionIdentifier.class));
            }
            if (jsonObject.has("favicon")) {
                serverStatusResponse.setFavicon(JsonUtils.getString(jsonObject, "favicon"));
            }
            return serverStatusResponse;
        }

        public JsonElement serialize(ServerStatusResponse serverStatusResponse, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            if (serverStatusResponse.getServerDescription() != null) {
                jsonObject.add("description", jsonSerializationContext.serialize((Object)serverStatusResponse.getServerDescription()));
            }
            if (serverStatusResponse.getPlayerCountData() != null) {
                jsonObject.add("players", jsonSerializationContext.serialize((Object)serverStatusResponse.getPlayerCountData()));
            }
            if (serverStatusResponse.getProtocolVersionInfo() != null) {
                jsonObject.add("version", jsonSerializationContext.serialize((Object)serverStatusResponse.getProtocolVersionInfo()));
            }
            if (serverStatusResponse.getFavicon() != null) {
                jsonObject.addProperty("favicon", serverStatusResponse.getFavicon());
            }
            return jsonObject;
        }
    }
}

