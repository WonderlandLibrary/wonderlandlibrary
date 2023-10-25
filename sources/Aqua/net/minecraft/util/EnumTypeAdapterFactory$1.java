package net.minecraft.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Map;
import net.minecraft.util.EnumTypeAdapterFactory;

/*
 * Exception performing whole class analysis ignored.
 */
class EnumTypeAdapterFactory.1
extends TypeAdapter<T> {
    final /* synthetic */ Map val$map;

    EnumTypeAdapterFactory.1(Map map) {
        this.val$map = map;
    }

    public void write(JsonWriter p_write_1_, T p_write_2_) throws IOException {
        if (p_write_2_ == null) {
            p_write_1_.nullValue();
        } else {
            p_write_1_.value(EnumTypeAdapterFactory.access$000((EnumTypeAdapterFactory)EnumTypeAdapterFactory.this, p_write_2_));
        }
    }

    public T read(JsonReader p_read_1_) throws IOException {
        if (p_read_1_.peek() == JsonToken.NULL) {
            p_read_1_.nextNull();
            return null;
        }
        return this.val$map.get((Object)p_read_1_.nextString());
    }
}
