// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import com.viaversion.viaversion.libs.gson.JsonDeserializer;
import com.viaversion.viaversion.libs.gson.JsonSerializer;
import com.viaversion.viaversion.libs.gson.annotations.JsonAdapter;
import com.viaversion.viaversion.libs.gson.TypeAdapter;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.internal.ConstructorConstructor;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;

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
                throw new IllegalArgumentException("Invalid attempt to bind an instance of " + instance.getClass().getName() + " as a @JsonAdapter for " + type.toString() + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer.");
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
