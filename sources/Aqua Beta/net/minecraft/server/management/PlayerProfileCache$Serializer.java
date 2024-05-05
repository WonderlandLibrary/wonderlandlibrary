package net.minecraft.server.management;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import net.minecraft.server.management.PlayerProfileCache;

class PlayerProfileCache.Serializer
implements JsonDeserializer<PlayerProfileCache.ProfileEntry>,
JsonSerializer<PlayerProfileCache.ProfileEntry> {
    private PlayerProfileCache.Serializer() {
    }

    public JsonElement serialize(PlayerProfileCache.ProfileEntry p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("name", p_serialize_1_.getGameProfile().getName());
        UUID uuid = p_serialize_1_.getGameProfile().getId();
        jsonobject.addProperty("uuid", uuid == null ? "" : uuid.toString());
        jsonobject.addProperty("expiresOn", PlayerProfileCache.dateFormat.format(p_serialize_1_.getExpirationDate()));
        return jsonobject;
    }

    public PlayerProfileCache.ProfileEntry deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        if (p_deserialize_1_.isJsonObject()) {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            JsonElement jsonelement = jsonobject.get("name");
            JsonElement jsonelement1 = jsonobject.get("uuid");
            JsonElement jsonelement2 = jsonobject.get("expiresOn");
            if (jsonelement != null && jsonelement1 != null) {
                String s = jsonelement1.getAsString();
                String s1 = jsonelement.getAsString();
                Date date = null;
                if (jsonelement2 != null) {
                    try {
                        date = PlayerProfileCache.dateFormat.parse(jsonelement2.getAsString());
                    }
                    catch (ParseException var14) {
                        date = null;
                    }
                }
                if (s1 != null && s != null) {
                    UUID uuid;
                    try {
                        uuid = UUID.fromString((String)s);
                    }
                    catch (Throwable var13) {
                        return null;
                    }
                    PlayerProfileCache playerProfileCache = PlayerProfileCache.this;
                    playerProfileCache.getClass();
                    PlayerProfileCache.ProfileEntry playerprofilecache$profileentry = new PlayerProfileCache.ProfileEntry(playerProfileCache, new GameProfile(uuid, s1), date, null);
                    return playerprofilecache$profileentry;
                }
                return null;
            }
            return null;
        }
        return null;
    }
}
