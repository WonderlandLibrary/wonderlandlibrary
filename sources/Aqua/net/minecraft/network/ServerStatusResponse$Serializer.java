package net.minecraft.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;

public static class ServerStatusResponse.Serializer
implements JsonDeserializer<ServerStatusResponse>,
JsonSerializer<ServerStatusResponse> {
    public ServerStatusResponse deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = JsonUtils.getJsonObject((JsonElement)p_deserialize_1_, (String)"status");
        ServerStatusResponse serverstatusresponse = new ServerStatusResponse();
        if (jsonobject.has("description")) {
            serverstatusresponse.setServerDescription((IChatComponent)p_deserialize_3_.deserialize(jsonobject.get("description"), IChatComponent.class));
        }
        if (jsonobject.has("players")) {
            serverstatusresponse.setPlayerCountData((ServerStatusResponse.PlayerCountData)p_deserialize_3_.deserialize(jsonobject.get("players"), ServerStatusResponse.PlayerCountData.class));
        }
        if (jsonobject.has("version")) {
            serverstatusresponse.setProtocolVersionInfo((ServerStatusResponse.MinecraftProtocolVersionIdentifier)p_deserialize_3_.deserialize(jsonobject.get("version"), ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));
        }
        if (jsonobject.has("favicon")) {
            serverstatusresponse.setFavicon(JsonUtils.getString((JsonObject)jsonobject, (String)"favicon"));
        }
        return serverstatusresponse;
    }

    public JsonElement serialize(ServerStatusResponse p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        JsonObject jsonobject = new JsonObject();
        if (p_serialize_1_.getServerDescription() != null) {
            jsonobject.add("description", p_serialize_3_.serialize((Object)p_serialize_1_.getServerDescription()));
        }
        if (p_serialize_1_.getPlayerCountData() != null) {
            jsonobject.add("players", p_serialize_3_.serialize((Object)p_serialize_1_.getPlayerCountData()));
        }
        if (p_serialize_1_.getProtocolVersionInfo() != null) {
            jsonobject.add("version", p_serialize_3_.serialize((Object)p_serialize_1_.getProtocolVersionInfo()));
        }
        if (p_serialize_1_.getFavicon() != null) {
            jsonobject.addProperty("favicon", p_serialize_1_.getFavicon());
        }
        return jsonobject;
    }
}
