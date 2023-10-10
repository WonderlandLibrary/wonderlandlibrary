// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import com.viaversion.viaversion.libs.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

public final class ArrayTypeAdapter<E> extends TypeAdapter<Object>
{
    public static final TypeAdapterFactory FACTORY;
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;
    
    public ArrayTypeAdapter(final Gson context, final TypeAdapter<E> componentTypeAdapter, final Class<E> componentType) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(context, componentTypeAdapter, componentType);
        this.componentType = componentType;
    }
    
    @Override
    public Object read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        final List<E> list = new ArrayList<E>();
        in.beginArray();
        while (in.hasNext()) {
            final E instance = this.componentTypeAdapter.read(in);
            list.add(instance);
        }
        in.endArray();
        final int size = list.size();
        final Object array = Array.newInstance(this.componentType, size);
        for (int i = 0; i < size; ++i) {
            Array.set(array, i, list.get(i));
        }
        return array;
    }
    
    @Override
    public void write(final JsonWriter out, final Object array) throws IOException {
        if (array == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        for (int i = 0, length = Array.getLength(array); i < length; ++i) {
            final E value = (E)Array.get(array, i);
            this.componentTypeAdapter.write(out, value);
        }
        out.endArray();
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                final Type type = typeToken.getType();
                if (!(type instanceof GenericArrayType) && (!(type instanceof Class) || !((Class)type).isArray())) {
                    return null;
                }
                final Type componentType = $Gson$Types.getArrayComponentType(type);
                final TypeAdapter<?> componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType));
                return (TypeAdapter<T>)new ArrayTypeAdapter(gson, (TypeAdapter<Object>)componentTypeAdapter, (Class<Object>)$Gson$Types.getRawType(componentType));
            }
        };
    }
}
