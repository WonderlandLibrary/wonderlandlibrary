// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.UUID;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.text.ITextComponent;

public class ServerStatusResponse
{
    private ITextComponent description;
    private Players players;
    private Version version;
    private String favicon;
    
    public ITextComponent getServerDescription() {
        return this.description;
    }
    
    public void setServerDescription(final ITextComponent descriptionIn) {
        this.description = descriptionIn;
    }
    
    public Players getPlayers() {
        return this.players;
    }
    
    public void setPlayers(final Players playersIn) {
        this.players = playersIn;
    }
    
    public Version getVersion() {
        return this.version;
    }
    
    public void setVersion(final Version versionIn) {
        this.version = versionIn;
    }
    
    public void setFavicon(final String faviconBlob) {
        this.favicon = faviconBlob;
    }
    
    public String getFavicon() {
        return this.favicon;
    }
    
    public static class Players
    {
        private final int maxPlayers;
        private final int onlinePlayerCount;
        private GameProfile[] players;
        
        public Players(final int maxOnlinePlayers, final int onlinePlayers) {
            this.maxPlayers = maxOnlinePlayers;
            this.onlinePlayerCount = onlinePlayers;
        }
        
        public int getMaxPlayers() {
            return this.maxPlayers;
        }
        
        public int getOnlinePlayerCount() {
            return this.onlinePlayerCount;
        }
        
        public GameProfile[] getPlayers() {
            return this.players;
        }
        
        public void setPlayers(final GameProfile[] playersIn) {
            this.players = playersIn;
        }
        
        public static class Serializer implements JsonDeserializer<Players>, JsonSerializer<Players>
        {
            public Players deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "players");
                final Players serverstatusresponse$players = new Players(JsonUtils.getInt(jsonobject, "max"), JsonUtils.getInt(jsonobject, "online"));
                if (JsonUtils.isJsonArray(jsonobject, "sample")) {
                    final JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "sample");
                    if (jsonarray.size() > 0) {
                        final GameProfile[] agameprofile = new GameProfile[jsonarray.size()];
                        for (int i = 0; i < agameprofile.length; ++i) {
                            final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonarray.get(i), "player[" + i + "]");
                            final String s = JsonUtils.getString(jsonobject2, "id");
                            agameprofile[i] = new GameProfile(UUID.fromString(s), JsonUtils.getString(jsonobject2, "name"));
                        }
                        serverstatusresponse$players.setPlayers(agameprofile);
                    }
                }
                return serverstatusresponse$players;
            }
            
            public JsonElement serialize(final Players p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
                final JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("max", (Number)p_serialize_1_.getMaxPlayers());
                jsonobject.addProperty("online", (Number)p_serialize_1_.getOnlinePlayerCount());
                if (p_serialize_1_.getPlayers() != null && p_serialize_1_.getPlayers().length > 0) {
                    final JsonArray jsonarray = new JsonArray();
                    for (int i = 0; i < p_serialize_1_.getPlayers().length; ++i) {
                        final JsonObject jsonobject2 = new JsonObject();
                        final UUID uuid = p_serialize_1_.getPlayers()[i].getId();
                        jsonobject2.addProperty("id", (uuid == null) ? "" : uuid.toString());
                        jsonobject2.addProperty("name", p_serialize_1_.getPlayers()[i].getName());
                        jsonarray.add((JsonElement)jsonobject2);
                    }
                    jsonobject.add("sample", (JsonElement)jsonarray);
                }
                return (JsonElement)jsonobject;
            }
        }
    }
    
    public static class Serializer implements JsonDeserializer<ServerStatusResponse>, JsonSerializer<ServerStatusResponse>
    {
        public ServerStatusResponse deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "status");
            final ServerStatusResponse serverstatusresponse = new ServerStatusResponse();
            if (jsonobject.has("description")) {
                serverstatusresponse.setServerDescription((ITextComponent)p_deserialize_3_.deserialize(jsonobject.get("description"), (Type)ITextComponent.class));
            }
            if (jsonobject.has("players")) {
                serverstatusresponse.setPlayers((Players)p_deserialize_3_.deserialize(jsonobject.get("players"), (Type)Players.class));
            }
            if (jsonobject.has("version")) {
                serverstatusresponse.setVersion((Version)p_deserialize_3_.deserialize(jsonobject.get("version"), (Type)Version.class));
            }
            if (jsonobject.has("favicon")) {
                serverstatusresponse.setFavicon(JsonUtils.getString(jsonobject, "favicon"));
            }
            return serverstatusresponse;
        }
        
        public JsonElement serialize(final ServerStatusResponse p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            if (p_serialize_1_.getServerDescription() != null) {
                jsonobject.add("description", p_serialize_3_.serialize((Object)p_serialize_1_.getServerDescription()));
            }
            if (p_serialize_1_.getPlayers() != null) {
                jsonobject.add("players", p_serialize_3_.serialize((Object)p_serialize_1_.getPlayers()));
            }
            if (p_serialize_1_.getVersion() != null) {
                jsonobject.add("version", p_serialize_3_.serialize((Object)p_serialize_1_.getVersion()));
            }
            if (p_serialize_1_.getFavicon() != null) {
                jsonobject.addProperty("favicon", p_serialize_1_.getFavicon());
            }
            return (JsonElement)jsonobject;
        }
    }
    
    public static class Version
    {
        private final String name;
        private final int protocol;
        
        public Version(final String nameIn, final int protocolIn) {
            this.name = nameIn;
            this.protocol = protocolIn;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getProtocol() {
            return this.protocol;
        }
        
        public static class Serializer implements JsonDeserializer<Version>, JsonSerializer<Version>
        {
            public Version deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "version");
                return new Version(JsonUtils.getString(jsonobject, "name"), JsonUtils.getInt(jsonobject, "protocol"));
            }
            
            public JsonElement serialize(final Version p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
                final JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("name", p_serialize_1_.getName());
                jsonobject.addProperty("protocol", (Number)p_serialize_1_.getProtocol());
                return (JsonElement)jsonobject;
            }
        }
    }
}
