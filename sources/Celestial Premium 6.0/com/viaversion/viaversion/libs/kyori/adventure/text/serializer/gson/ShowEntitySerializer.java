/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.JsonDeserializationContext;
import com.viaversion.viaversion.libs.gson.JsonDeserializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.JsonSerializationContext;
import com.viaversion.viaversion.libs.gson.JsonSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import java.lang.reflect.Type;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

final class ShowEntitySerializer
implements JsonDeserializer<HoverEvent.ShowEntity>,
JsonSerializer<HoverEvent.ShowEntity> {
    static final String TYPE = "type";
    static final String ID = "id";
    static final String NAME = "name";

    ShowEntitySerializer() {
    }

    @Override
    public HoverEvent.ShowEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        if (!object.has(TYPE) || !object.has(ID)) {
            throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
        }
        Key type = (Key)context.deserialize(object.getAsJsonPrimitive(TYPE), (Type)((Object)Key.class));
        UUID id = UUID.fromString(object.getAsJsonPrimitive(ID).getAsString());
        Component name = null;
        if (object.has(NAME)) {
            name = (Component)context.deserialize(object.get(NAME), (Type)((Object)Component.class));
        }
        return HoverEvent.ShowEntity.of(type, id, name);
    }

    @Override
    public JsonElement serialize(HoverEvent.ShowEntity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add(TYPE, context.serialize(src.type()));
        json.addProperty(ID, src.id().toString());
        @Nullable Component name = src.name();
        if (name != null) {
            json.add(NAME, context.serialize(name));
        }
        return json;
    }
}

