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
import net.minecraft.util.JsonUtils;

public static class ServerStatusResponse.MinecraftProtocolVersionIdentifier.Serializer
implements JsonDeserializer<ServerStatusResponse.MinecraftProtocolVersionIdentifier>,
JsonSerializer<ServerStatusResponse.MinecraftProtocolVersionIdentifier> {
    public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = JsonUtils.getJsonObject((JsonElement)p_deserialize_1_, (String)"version");
        return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(JsonUtils.getString((JsonObject)jsonobject, (String)"name"), JsonUtils.getInt((JsonObject)jsonobject, (String)"protocol"));
    }

    public JsonElement serialize(ServerStatusResponse.MinecraftProtocolVersionIdentifier p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("name", p_serialize_1_.getName());
        jsonobject.addProperty("protocol", (Number)Integer.valueOf((int)p_serialize_1_.getProtocol()));
        return jsonobject;
    }
}
