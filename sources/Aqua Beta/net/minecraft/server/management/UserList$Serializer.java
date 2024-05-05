package net.minecraft.server.management;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.server.management.UserListEntry;

class UserList.Serializer
implements JsonDeserializer<UserListEntry<K>>,
JsonSerializer<UserListEntry<K>> {
    private UserList.Serializer() {
    }

    public JsonElement serialize(UserListEntry<K> p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        JsonObject jsonobject = new JsonObject();
        p_serialize_1_.onSerialization(jsonobject);
        return jsonobject;
    }

    public UserListEntry<K> deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        if (p_deserialize_1_.isJsonObject()) {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            UserListEntry userlistentry = UserList.this.createEntry(jsonobject);
            return userlistentry;
        }
        return null;
    }
}
