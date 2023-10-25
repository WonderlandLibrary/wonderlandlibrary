/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.google.gson.Gson
 *  com.google.gson.TypeAdapter
 *  com.google.gson.TypeAdapterFactory
 *  com.google.gson.reflect.TypeToken
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
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
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class clazz = typeToken.getRawType();
        if (!clazz.isEnum()) {
            return null;
        }
        final HashMap hashMap = Maps.newHashMap();
        T[] TArray = clazz.getEnumConstants();
        int n = TArray.length;
        int n2 = 0;
        while (n2 < n) {
            Object t = TArray[n2];
            hashMap.put(this.func_151232_a(t), t);
            ++n2;
        }
        return new TypeAdapter<T>(){

            public void write(JsonWriter jsonWriter, T t) throws IOException {
                if (t == null) {
                    jsonWriter.nullValue();
                } else {
                    jsonWriter.value(EnumTypeAdapterFactory.this.func_151232_a(t));
                }
            }

            public T read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return hashMap.get(jsonReader.nextString());
            }
        };
    }

    private String func_151232_a(Object object) {
        return object instanceof Enum ? ((Enum)object).name().toLowerCase(Locale.US) : object.toString().toLowerCase(Locale.US);
    }
}

