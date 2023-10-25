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
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.util.JsonUtils;

public static class ServerStatusResponse.PlayerCountData.Serializer
implements JsonDeserializer<ServerStatusResponse.PlayerCountData>,
JsonSerializer<ServerStatusResponse.PlayerCountData> {
    public ServerStatusResponse.PlayerCountData deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonArray jsonarray;
        JsonObject jsonobject = JsonUtils.getJsonObject((JsonElement)p_deserialize_1_, (String)"players");
        ServerStatusResponse.PlayerCountData serverstatusresponse$playercountdata = new ServerStatusResponse.PlayerCountData(JsonUtils.getInt((JsonObject)jsonobject, (String)"max"), JsonUtils.getInt((JsonObject)jsonobject, (String)"online"));
        if (JsonUtils.isJsonArray((JsonObject)jsonobject, (String)"sample") && (jsonarray = JsonUtils.getJsonArray((JsonObject)jsonobject, (String)"sample")).size() > 0) {
            GameProfile[] agameprofile = new GameProfile[jsonarray.size()];
            for (int i = 0; i < agameprofile.length; ++i) {
                JsonObject jsonobject1 = JsonUtils.getJsonObject((JsonElement)jsonarray.get(i), (String)("player[" + i + "]"));
                String s = JsonUtils.getString((JsonObject)jsonobject1, (String)"id");
                agameprofile[i] = new GameProfile(UUID.fromString((String)s), JsonUtils.getString((JsonObject)jsonobject1, (String)"name"));
            }
            serverstatusresponse$playercountdata.setPlayers(agameprofile);
        }
        return serverstatusresponse$playercountdata;
    }

    public JsonElement serialize(ServerStatusResponse.PlayerCountData p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("max", (Number)Integer.valueOf((int)p_serialize_1_.getMaxPlayers()));
        jsonobject.addProperty("online", (Number)Integer.valueOf((int)p_serialize_1_.getOnlinePlayerCount()));
        if (p_serialize_1_.getPlayers() != null && p_serialize_1_.getPlayers().length > 0) {
            JsonArray jsonarray = new JsonArray();
            for (int i = 0; i < p_serialize_1_.getPlayers().length; ++i) {
                JsonObject jsonobject1 = new JsonObject();
                UUID uuid = p_serialize_1_.getPlayers()[i].getId();
                jsonobject1.addProperty("id", uuid == null ? "" : uuid.toString());
                jsonobject1.addProperty("name", p_serialize_1_.getPlayers()[i].getName());
                jsonarray.add((JsonElement)jsonobject1);
            }
            jsonobject.add("sample", (JsonElement)jsonarray);
        }
        return jsonobject;
    }
}
