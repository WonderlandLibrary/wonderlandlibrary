// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson.internal.bind;

import me.gong.mcleaks.util.google.gson.JsonDeserializer;
import me.gong.mcleaks.util.google.gson.JsonSerializer;
import me.gong.mcleaks.util.google.gson.annotations.JsonAdapter;
import me.gong.mcleaks.util.google.gson.TypeAdapter;
import me.gong.mcleaks.util.google.gson.reflect.TypeToken;
import me.gong.mcleaks.util.google.gson.Gson;
import me.gong.mcleaks.util.google.gson.internal.ConstructorConstructor;
import me.gong.mcleaks.util.google.gson.TypeAdapterFactory;

public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    
    public JsonAdapterAnnotationTypeAdapterFactory(final ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> targetType) {
        final Class<? super T> rawType = targetType.getRawType();
        final JsonAdapter annotation = rawType.getAnnotation(JsonAdapter.class);
        if (annotation == null) {
            return null;
        }
        return (TypeAdapter<T>)this.getTypeAdapter(this.constructorConstructor, gson, targetType, annotation);
    }
    
    TypeAdapter<?> getTypeAdapter(final ConstructorConstructor constructorConstructor, final Gson gson, final TypeToken<?> type, final JsonAdapter annotation) {
        final Object instance = constructorConstructor.get((TypeToken<Object>)TypeToken.get(annotation.value())).construct();
        TypeAdapter<?> typeAdapter;
        if (instance instanceof TypeAdapter) {
            typeAdapter = (TypeAdapter<?>)instance;
        }
        else if (instance instanceof TypeAdapterFactory) {
            typeAdapter = ((TypeAdapterFactory)instance).create(gson, type);
        }
        else {
            if (!(instance instanceof JsonSerializer) && !(instance instanceof JsonDeserializer)) {
                throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer reference.");
            }
            final JsonSerializer<?> serializer = (JsonSerializer<?>)((instance instanceof JsonSerializer) ? ((JsonSerializer)instance) : null);
            final JsonDeserializer<?> deserializer = (JsonDeserializer<?>)((instance instanceof JsonDeserializer) ? ((JsonDeserializer)instance) : null);
            typeAdapter = new TreeTypeAdapter<Object>(serializer, deserializer, gson, type, null);
        }
        if (typeAdapter != null && annotation.nullSafe()) {
            typeAdapter = typeAdapter.nullSafe();
        }
        return typeAdapter;
    }
}
