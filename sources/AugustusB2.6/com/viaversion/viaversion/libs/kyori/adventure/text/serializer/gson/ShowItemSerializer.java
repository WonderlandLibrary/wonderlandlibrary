// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.kyori.adventure.nbt.api.BinaryTagHolder;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class ShowItemSerializer extends TypeAdapter<HoverEvent.ShowItem>
{
    static final String ID = "id";
    static final String COUNT = "count";
    static final String TAG = "tag";
    private final Gson gson;
    
    static TypeAdapter<HoverEvent.ShowItem> create(final Gson gson) {
        return new ShowItemSerializer(gson).nullSafe();
    }
    
    private ShowItemSerializer(final Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public HoverEvent.ShowItem read(final JsonReader in) throws IOException {
        in.beginObject();
        Key key = null;
        int count = 1;
        BinaryTagHolder nbt = null;
        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals("id")) {
                key = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
            }
            else if (fieldName.equals("count")) {
                count = in.nextInt();
            }
            else if (fieldName.equals("tag")) {
                final JsonToken token = in.peek();
                if (token == JsonToken.STRING || token == JsonToken.NUMBER) {
                    nbt = BinaryTagHolder.of(in.nextString());
                }
                else if (token == JsonToken.BOOLEAN) {
                    nbt = BinaryTagHolder.of(String.valueOf(in.nextBoolean()));
                }
                else {
                    if (token != JsonToken.NULL) {
                        throw new JsonParseException("Expected tag to be a string");
                    }
                    in.nextNull();
                }
            }
            else {
                in.skipValue();
            }
        }
        if (key == null) {
            throw new JsonParseException("Not sure how to deserialize show_item hover event");
        }
        in.endObject();
        return HoverEvent.ShowItem.of(key, count, nbt);
    }
    
    @Override
    public void write(final JsonWriter out, final HoverEvent.ShowItem value) throws IOException {
        out.beginObject();
        out.name("id");
        this.gson.toJson(value.item(), SerializerFactory.KEY_TYPE, out);
        final int count = value.count();
        if (count != 1) {
            out.name("count");
            out.value(count);
        }
        final BinaryTagHolder nbt = value.nbt();
        if (nbt != null) {
            out.name("tag");
            out.value(nbt.string());
        }
        out.endObject();
    }
}
