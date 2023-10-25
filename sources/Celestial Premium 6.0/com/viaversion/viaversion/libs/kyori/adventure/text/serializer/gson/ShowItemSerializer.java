/*
 * Decompiled with CFR 0.150.
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
import com.viaversion.viaversion.libs.kyori.adventure.nbt.api.BinaryTagHolder;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import java.lang.reflect.Type;

final class ShowItemSerializer
implements JsonDeserializer<HoverEvent.ShowItem>,
JsonSerializer<HoverEvent.ShowItem> {
    static final String ID = "id";
    static final String COUNT = "count";
    static final String TAG = "tag";

    ShowItemSerializer() {
    }

    @Override
    public HoverEvent.ShowItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        if (!object.has(ID)) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
        }
        Key id = (Key)context.deserialize(object.getAsJsonPrimitive(ID), (Type)((Object)Key.class));
        int count = 1;
        if (object.has(COUNT)) {
            count = object.get(COUNT).getAsInt();
        }
        BinaryTagHolder nbt = null;
        if (object.has(TAG)) {
            JsonElement tag = object.get(TAG);
            if (tag.isJsonPrimitive()) {
                nbt = BinaryTagHolder.of(tag.getAsString());
            } else if (!tag.isJsonNull()) {
                throw new JsonParseException("Expected tag to be a string");
            }
        }
        return HoverEvent.ShowItem.of(id, count, nbt);
    }

    @Override
    public JsonElement serialize(HoverEvent.ShowItem src, Type typeOfSrc, JsonSerializationContext context) {
        BinaryTagHolder nbt;
        JsonObject json = new JsonObject();
        json.add(ID, context.serialize(src.item()));
        int count = src.count();
        if (count != 1) {
            json.addProperty(COUNT, count);
        }
        if ((nbt = src.nbt()) != null) {
            json.addProperty(TAG, nbt.string());
        }
        return json;
    }
}

