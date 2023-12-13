/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class EnumTypeAdapterFactory
implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson p_create_1_, TypeToken<T> p_create_2_) {
        Class<T> oclass = p_create_2_.getRawType();
        if (!oclass.isEnum()) {
            return null;
        }
        final HashMap<String, T> map = Maps.newHashMap();
        for (T t2 : oclass.getEnumConstants()) {
            map.put(this.func_151232_a(t2), t2);
        }
        return new TypeAdapter<T>(){

            @Override
            public void write(JsonWriter p_write_1_, T p_write_2_) throws IOException {
                if (p_write_2_ == null) {
                    p_write_1_.nullValue();
                } else {
                    p_write_1_.value(EnumTypeAdapterFactory.this.func_151232_a(p_write_2_));
                }
            }

            @Override
            public T read(JsonReader p_read_1_) throws IOException {
                if (p_read_1_.peek() == JsonToken.NULL) {
                    p_read_1_.nextNull();
                    return null;
                }
                return map.get(p_read_1_.nextString());
            }
        };
    }

    private String func_151232_a(Object p_151232_1_) {
        return p_151232_1_ instanceof Enum ? ((Enum)p_151232_1_).name().toLowerCase(Locale.US) : p_151232_1_.toString().toLowerCase(Locale.US);
    }
}

