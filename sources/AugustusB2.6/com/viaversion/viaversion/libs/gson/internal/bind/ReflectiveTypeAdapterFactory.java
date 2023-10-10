// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.internal.$Gson$Types;
import java.lang.reflect.AccessibleObject;
import java.util.LinkedHashMap;
import java.util.Map;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.gson.annotations.JsonAdapter;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.internal.Primitives;
import com.viaversion.viaversion.libs.gson.internal.ObjectConstructor;
import com.viaversion.viaversion.libs.gson.TypeAdapter;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import com.viaversion.viaversion.libs.gson.annotations.SerializedName;
import java.util.List;
import java.lang.reflect.Field;
import com.viaversion.viaversion.libs.gson.internal.reflect.ReflectionAccessor;
import com.viaversion.viaversion.libs.gson.internal.Excluder;
import com.viaversion.viaversion.libs.gson.FieldNamingStrategy;
import com.viaversion.viaversion.libs.gson.internal.ConstructorConstructor;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    private final ReflectionAccessor accessor;
    
    public ReflectiveTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final FieldNamingStrategy fieldNamingPolicy, final Excluder excluder, final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory) {
        this.accessor = ReflectionAccessor.getInstance();
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterFactory;
    }
    
    public boolean excludeField(final Field f, final boolean serialize) {
        return excludeField(f, serialize, this.excluder);
    }
    
    static boolean excludeField(final Field f, final boolean serialize, final Excluder excluder) {
        return !excluder.excludeClass(f.getType(), serialize) && !excluder.excludeField(f, serialize);
    }
    
    private List<String> getFieldNames(final Field f) {
        final SerializedName annotation = f.getAnnotation(SerializedName.class);
        if (annotation == null) {
            final String name = this.fieldNamingPolicy.translateName(f);
            return Collections.singletonList(name);
        }
        final String serializedName = annotation.value();
        final String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return Collections.singletonList(serializedName);
        }
        final List<String> fieldNames = new ArrayList<String>(alternates.length + 1);
        fieldNames.add(serializedName);
        for (final String alternate : alternates) {
            fieldNames.add(alternate);
        }
        return fieldNames;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        final ObjectConstructor<T> constructor = this.constructorConstructor.get(type);
        return new Adapter<T>(constructor, this.getBoundFields(gson, type, raw));
    }
    
    private BoundField createBoundField(final Gson context, final Field field, final String name, final TypeToken<?> fieldType, final boolean serialize, final boolean deserialize) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        final JsonAdapter annotation = field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> mapped = null;
        if (annotation != null) {
            mapped = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, context, fieldType, annotation);
        }
        final boolean jsonAdapterPresent = mapped != null;
        if (mapped == null) {
            mapped = context.getAdapter(fieldType);
        }
        final TypeAdapter<?> typeAdapter = mapped;
        return new BoundField(name, serialize, deserialize) {
            @Override
            void write(final JsonWriter writer, final Object value) throws IOException, IllegalAccessException {
                final Object fieldValue = field.get(value);
                final TypeAdapter t = jsonAdapterPresent ? typeAdapter : new TypeAdapterRuntimeTypeWrapper(context, typeAdapter, fieldType.getType());
                t.write(writer, fieldValue);
            }
            
            @Override
            void read(final JsonReader reader, final Object value) throws IOException, IllegalAccessException {
                final Object fieldValue = typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    field.set(value, fieldValue);
                }
            }
            
            public boolean writeField(final Object value) throws IOException, IllegalAccessException {
                if (!this.serialized) {
                    return false;
                }
                final Object fieldValue = field.get(value);
                return fieldValue != value;
            }
        };
    }
    
    private Map<String, BoundField> getBoundFields(final Gson context, TypeToken<?> type, Class<?> raw) {
        final Map<String, BoundField> result = new LinkedHashMap<String, BoundField>();
        if (raw.isInterface()) {
            return result;
        }
        final Type declaredType = type.getType();
        while (raw != Object.class) {
            final Field[] declaredFields;
            final Field[] fields = declaredFields = raw.getDeclaredFields();
            for (final Field field : declaredFields) {
                boolean serialize = this.excludeField(field, true);
                final boolean deserialize = this.excludeField(field, false);
                if (serialize || deserialize) {
                    this.accessor.makeAccessible(field);
                    final Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
                    final List<String> fieldNames = this.getFieldNames(field);
                    BoundField previous = null;
                    for (int i = 0, size = fieldNames.size(); i < size; ++i) {
                        final String name = fieldNames.get(i);
                        if (i != 0) {
                            serialize = false;
                        }
                        final BoundField boundField = this.createBoundField(context, field, name, TypeToken.get(fieldType), serialize, deserialize);
                        final BoundField replaced = result.put(name, boundField);
                        if (previous == null) {
                            previous = replaced;
                        }
                    }
                    if (previous != null) {
                        throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + previous.name);
                    }
                }
            }
            type = TypeToken.get($Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
            raw = type.getRawType();
        }
        return result;
    }
    
    abstract static class BoundField
    {
        final String name;
        final boolean serialized;
        final boolean deserialized;
        
        protected BoundField(final String name, final boolean serialized, final boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
        
        abstract boolean writeField(final Object p0) throws IOException, IllegalAccessException;
        
        abstract void write(final JsonWriter p0, final Object p1) throws IOException, IllegalAccessException;
        
        abstract void read(final JsonReader p0, final Object p1) throws IOException, IllegalAccessException;
    }
    
    public static final class Adapter<T> extends TypeAdapter<T>
    {
        private final ObjectConstructor<T> constructor;
        private final Map<String, BoundField> boundFields;
        
        Adapter(final ObjectConstructor<T> constructor, final Map<String, BoundField> boundFields) {
            this.constructor = constructor;
            this.boundFields = boundFields;
        }
        
        @Override
        public T read(final JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            final T instance = this.constructor.construct();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    final String name = in.nextName();
                    final BoundField field = this.boundFields.get(name);
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    }
                    else {
                        field.read(in, instance);
                    }
                }
            }
            catch (IllegalStateException e) {
                throw new JsonSyntaxException(e);
            }
            catch (IllegalAccessException e2) {
                throw new AssertionError((Object)e2);
            }
            in.endObject();
            return instance;
        }
        
        @Override
        public void write(final JsonWriter out, final T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (final BoundField boundField : this.boundFields.values()) {
                    if (boundField.writeField(value)) {
                        out.name(boundField.name);
                        boundField.write(out, value);
                    }
                }
            }
            catch (IllegalAccessException e) {
                throw new AssertionError((Object)e);
            }
            out.endObject();
        }
    }
}
