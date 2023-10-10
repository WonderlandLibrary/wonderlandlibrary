// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.util.UUID;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class ShowEntitySerializer extends TypeAdapter<HoverEvent.ShowEntity>
{
    static final String TYPE = "type";
    static final String ID = "id";
    static final String NAME = "name";
    private final Gson gson;
    
    static TypeAdapter<HoverEvent.ShowEntity> create(final Gson gson) {
        return new ShowEntitySerializer(gson).nullSafe();
    }
    
    private ShowEntitySerializer(final Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public HoverEvent.ShowEntity read(final JsonReader in) throws IOException {
        in.beginObject();
        Key type = null;
        UUID id = null;
        Component name = null;
        while (in.hasNext()) {
            final String fieldName = in.nextName();
            if (fieldName.equals("type")) {
                type = this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
            }
            else if (fieldName.equals("id")) {
                id = UUID.fromString(in.nextString());
            }
            else if (fieldName.equals("name")) {
                name = this.gson.fromJson(in, SerializerFactory.COMPONENT_TYPE);
            }
            else {
                in.skipValue();
            }
        }
        if (type == null || id == null) {
            throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
        }
        in.endObject();
        return HoverEvent.ShowEntity.of(type, id, name);
    }
    
    @Override
    public void write(final JsonWriter out, final HoverEvent.ShowEntity value) throws IOException {
        out.beginObject();
        out.name("type");
        this.gson.toJson(value.type(), SerializerFactory.KEY_TYPE, out);
        out.name("id");
        out.value(value.id().toString());
        final Component name = value.name();
        if (name != null) {
            out.name("name");
            this.gson.toJson(name, SerializerFactory.COMPONENT_TYPE, out);
        }
        out.endObject();
    }
}
