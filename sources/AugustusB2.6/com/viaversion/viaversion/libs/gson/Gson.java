// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

import com.viaversion.viaversion.libs.gson.internal.bind.JsonTreeReader;
import java.io.EOFException;
import com.viaversion.viaversion.libs.gson.stream.MalformedJsonException;
import java.io.StringReader;
import com.viaversion.viaversion.libs.gson.internal.Primitives;
import java.io.Reader;
import java.io.Writer;
import com.viaversion.viaversion.libs.gson.internal.Streams;
import java.io.StringWriter;
import com.viaversion.viaversion.libs.gson.internal.bind.JsonTreeWriter;
import java.util.Iterator;
import java.util.HashMap;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.internal.bind.MapTypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.internal.bind.CollectionTypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.internal.bind.ArrayTypeAdapter;
import com.viaversion.viaversion.libs.gson.internal.bind.SqlDateTypeAdapter;
import com.viaversion.viaversion.libs.gson.internal.bind.TimeTypeAdapter;
import com.viaversion.viaversion.libs.gson.internal.bind.DateTypeAdapter;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Collection;
import com.viaversion.viaversion.libs.gson.internal.bind.ObjectTypeAdapter;
import com.viaversion.viaversion.libs.gson.internal.bind.TypeAdapters;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.internal.Excluder;
import java.util.List;
import com.viaversion.viaversion.libs.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.internal.ConstructorConstructor;
import java.util.Map;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;

public final class Gson
{
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    static final boolean DEFAULT_LENIENT = false;
    static final boolean DEFAULT_PRETTY_PRINT = false;
    static final boolean DEFAULT_ESCAPE_HTML = true;
    static final boolean DEFAULT_SERIALIZE_NULLS = false;
    static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
    static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
    private static final TypeToken<?> NULL_KEY_SURROGATE;
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls;
    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache;
    private final ConstructorConstructor constructorConstructor;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    final List<TypeAdapterFactory> factories;
    final Excluder excluder;
    final FieldNamingStrategy fieldNamingStrategy;
    final Map<Type, InstanceCreator<?>> instanceCreators;
    final boolean serializeNulls;
    final boolean complexMapKeySerialization;
    final boolean generateNonExecutableJson;
    final boolean htmlSafe;
    final boolean prettyPrinting;
    final boolean lenient;
    final boolean serializeSpecialFloatingPointValues;
    final String datePattern;
    final int dateStyle;
    final int timeStyle;
    final LongSerializationPolicy longSerializationPolicy;
    final List<TypeAdapterFactory> builderFactories;
    final List<TypeAdapterFactory> builderHierarchyFactories;
    
    public Gson() {
        this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, false, LongSerializationPolicy.DEFAULT, null, 2, 2, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }
    
    Gson(final Excluder excluder, final FieldNamingStrategy fieldNamingStrategy, final Map<Type, InstanceCreator<?>> instanceCreators, final boolean serializeNulls, final boolean complexMapKeySerialization, final boolean generateNonExecutableGson, final boolean htmlSafe, final boolean prettyPrinting, final boolean lenient, final boolean serializeSpecialFloatingPointValues, final LongSerializationPolicy longSerializationPolicy, final String datePattern, final int dateStyle, final int timeStyle, final List<TypeAdapterFactory> builderFactories, final List<TypeAdapterFactory> builderHierarchyFactories, final List<TypeAdapterFactory> factoriesToBeAdded) {
        this.calls = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>();
        this.typeTokenCache = new ConcurrentHashMap<TypeToken<?>, TypeAdapter<?>>();
        this.excluder = excluder;
        this.fieldNamingStrategy = fieldNamingStrategy;
        this.instanceCreators = instanceCreators;
        this.constructorConstructor = new ConstructorConstructor(instanceCreators);
        this.serializeNulls = serializeNulls;
        this.complexMapKeySerialization = complexMapKeySerialization;
        this.generateNonExecutableJson = generateNonExecutableGson;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;
        this.lenient = lenient;
        this.serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValues;
        this.longSerializationPolicy = longSerializationPolicy;
        this.datePattern = datePattern;
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.builderFactories = builderFactories;
        this.builderHierarchyFactories = builderHierarchyFactories;
        final List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();
        factories.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        factories.add(ObjectTypeAdapter.FACTORY);
        factories.add(excluder);
        factories.addAll(factoriesToBeAdded);
        factories.add(TypeAdapters.STRING_FACTORY);
        factories.add(TypeAdapters.INTEGER_FACTORY);
        factories.add(TypeAdapters.BOOLEAN_FACTORY);
        factories.add(TypeAdapters.BYTE_FACTORY);
        factories.add(TypeAdapters.SHORT_FACTORY);
        final TypeAdapter<Number> longAdapter = longAdapter(longSerializationPolicy);
        factories.add(TypeAdapters.newFactory(Long.TYPE, Long.class, longAdapter));
        factories.add(TypeAdapters.newFactory(Double.TYPE, Double.class, this.doubleAdapter(serializeSpecialFloatingPointValues)));
        factories.add(TypeAdapters.newFactory(Float.TYPE, Float.class, this.floatAdapter(serializeSpecialFloatingPointValues)));
        factories.add(TypeAdapters.NUMBER_FACTORY);
        factories.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        factories.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        factories.add(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
        factories.add(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
        factories.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        factories.add(TypeAdapters.CHARACTER_FACTORY);
        factories.add(TypeAdapters.STRING_BUILDER_FACTORY);
        factories.add(TypeAdapters.STRING_BUFFER_FACTORY);
        factories.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        factories.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        factories.add(TypeAdapters.URL_FACTORY);
        factories.add(TypeAdapters.URI_FACTORY);
        factories.add(TypeAdapters.UUID_FACTORY);
        factories.add(TypeAdapters.CURRENCY_FACTORY);
        factories.add(TypeAdapters.LOCALE_FACTORY);
        factories.add(TypeAdapters.INET_ADDRESS_FACTORY);
        factories.add(TypeAdapters.BIT_SET_FACTORY);
        factories.add(DateTypeAdapter.FACTORY);
        factories.add(TypeAdapters.CALENDAR_FACTORY);
        factories.add(TimeTypeAdapter.FACTORY);
        factories.add(SqlDateTypeAdapter.FACTORY);
        factories.add(TypeAdapters.TIMESTAMP_FACTORY);
        factories.add(ArrayTypeAdapter.FACTORY);
        factories.add(TypeAdapters.CLASS_FACTORY);
        factories.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
        factories.add(new MapTypeAdapterFactory(this.constructorConstructor, complexMapKeySerialization));
        factories.add(this.jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor));
        factories.add(TypeAdapters.ENUM_FACTORY);
        factories.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy, excluder, this.jsonAdapterFactory));
        this.factories = Collections.unmodifiableList((List<? extends TypeAdapterFactory>)factories);
    }
    
    public GsonBuilder newBuilder() {
        return new GsonBuilder(this);
    }
    
    public Excluder excluder() {
        return this.excluder;
    }
    
    public FieldNamingStrategy fieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }
    
    public boolean serializeNulls() {
        return this.serializeNulls;
    }
    
    public boolean htmlSafe() {
        return this.htmlSafe;
    }
    
    private TypeAdapter<Number> doubleAdapter(final boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Double read(final JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter out, final Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                final double doubleValue = value.doubleValue();
                Gson.checkValidFloatingPoint(doubleValue);
                out.value(value);
            }
        };
    }
    
    private TypeAdapter<Number> floatAdapter(final boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Float read(final JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return (float)in.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter out, final Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                final float floatValue = value.floatValue();
                Gson.checkValidFloatingPoint(floatValue);
                out.value(value);
            }
        };
    }
    
    static void checkValidFloatingPoint(final double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(value + " is not a valid double value as per JSON specification. To override this behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }
    
    private static TypeAdapter<Number> longAdapter(final LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextLong();
            }
            
            @Override
            public void write(final JsonWriter out, final Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(value.toString());
            }
        };
    }
    
    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> longAdapter) {
        return new TypeAdapter<AtomicLong>() {
            @Override
            public void write(final JsonWriter out, final AtomicLong value) throws IOException {
                longAdapter.write(out, value.get());
            }
            
            @Override
            public AtomicLong read(final JsonReader in) throws IOException {
                final Number value = longAdapter.read(in);
                return new AtomicLong(value.longValue());
            }
        }.nullSafe();
    }
    
    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> longAdapter) {
        return new TypeAdapter<AtomicLongArray>() {
            @Override
            public void write(final JsonWriter out, final AtomicLongArray value) throws IOException {
                out.beginArray();
                for (int i = 0, length = value.length(); i < length; ++i) {
                    longAdapter.write(out, value.get(i));
                }
                out.endArray();
            }
            
            @Override
            public AtomicLongArray read(final JsonReader in) throws IOException {
                final List<Long> list = new ArrayList<Long>();
                in.beginArray();
                while (in.hasNext()) {
                    final long value = longAdapter.read(in).longValue();
                    list.add(value);
                }
                in.endArray();
                final int length = list.size();
                final AtomicLongArray array = new AtomicLongArray(length);
                for (int i = 0; i < length; ++i) {
                    array.set(i, list.get(i));
                }
                return array;
            }
        }.nullSafe();
    }
    
    public <T> TypeAdapter<T> getAdapter(final TypeToken<T> type) {
        final TypeAdapter<?> cached = this.typeTokenCache.get((type == null) ? Gson.NULL_KEY_SURROGATE : type);
        if (cached != null) {
            return (TypeAdapter<T>)cached;
        }
        Map<TypeToken<?>, FutureTypeAdapter<?>> threadCalls = this.calls.get();
        boolean requiresThreadLocalCleanup = false;
        if (threadCalls == null) {
            threadCalls = new HashMap<TypeToken<?>, FutureTypeAdapter<?>>();
            this.calls.set(threadCalls);
            requiresThreadLocalCleanup = true;
        }
        final FutureTypeAdapter<T> ongoingCall = (FutureTypeAdapter<T>)threadCalls.get(type);
        if (ongoingCall != null) {
            return ongoingCall;
        }
        try {
            final FutureTypeAdapter<T> call = new FutureTypeAdapter<T>();
            threadCalls.put(type, call);
            for (final TypeAdapterFactory factory : this.factories) {
                final TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    call.setDelegate(candidate);
                    this.typeTokenCache.put(type, candidate);
                    return candidate;
                }
            }
            throw new IllegalArgumentException("GSON (2.8.8) cannot handle " + type);
        }
        finally {
            threadCalls.remove(type);
            if (requiresThreadLocalCleanup) {
                this.calls.remove();
            }
        }
    }
    
    public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, final TypeToken<T> type) {
        if (!this.factories.contains(skipPast)) {
            skipPast = this.jsonAdapterFactory;
        }
        boolean skipPastFound = false;
        for (final TypeAdapterFactory factory : this.factories) {
            if (!skipPastFound) {
                if (factory != skipPast) {
                    continue;
                }
                skipPastFound = true;
            }
            else {
                final TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    return candidate;
                }
                continue;
            }
        }
        throw new IllegalArgumentException("GSON cannot serialize " + type);
    }
    
    public <T> TypeAdapter<T> getAdapter(final Class<T> type) {
        return this.getAdapter((TypeToken<T>)TypeToken.get((Class<T>)type));
    }
    
    public JsonElement toJsonTree(final Object src) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        return this.toJsonTree(src, src.getClass());
    }
    
    public JsonElement toJsonTree(final Object src, final Type typeOfSrc) {
        final JsonTreeWriter writer = new JsonTreeWriter();
        this.toJson(src, typeOfSrc, writer);
        return writer.get();
    }
    
    public String toJson(final Object src) {
        if (src == null) {
            return this.toJson(JsonNull.INSTANCE);
        }
        return this.toJson(src, src.getClass());
    }
    
    public String toJson(final Object src, final Type typeOfSrc) {
        final StringWriter writer = new StringWriter();
        this.toJson(src, typeOfSrc, writer);
        return writer.toString();
    }
    
    public void toJson(final Object src, final Appendable writer) throws JsonIOException {
        if (src != null) {
            this.toJson(src, src.getClass(), writer);
        }
        else {
            this.toJson(JsonNull.INSTANCE, writer);
        }
    }
    
    public void toJson(final Object src, final Type typeOfSrc, final Appendable writer) throws JsonIOException {
        try {
            final JsonWriter jsonWriter = this.newJsonWriter(Streams.writerForAppendable(writer));
            this.toJson(src, typeOfSrc, jsonWriter);
        }
        catch (IOException e) {
            throw new JsonIOException(e);
        }
    }
    
    public void toJson(final Object src, final Type typeOfSrc, final JsonWriter writer) throws JsonIOException {
        final TypeAdapter<?> adapter = this.getAdapter(TypeToken.get(typeOfSrc));
        final boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        final boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(this.htmlSafe);
        final boolean oldSerializeNulls = writer.getSerializeNulls();
        writer.setSerializeNulls(this.serializeNulls);
        try {
            adapter.write(writer, src);
        }
        catch (IOException e) {
            throw new JsonIOException(e);
        }
        catch (AssertionError e2) {
            final AssertionError error = new AssertionError((Object)("AssertionError (GSON 2.8.8): " + e2.getMessage()));
            error.initCause(e2);
            throw error;
        }
        finally {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        }
    }
    
    public String toJson(final JsonElement jsonElement) {
        final StringWriter writer = new StringWriter();
        this.toJson(jsonElement, writer);
        return writer.toString();
    }
    
    public void toJson(final JsonElement jsonElement, final Appendable writer) throws JsonIOException {
        try {
            final JsonWriter jsonWriter = this.newJsonWriter(Streams.writerForAppendable(writer));
            this.toJson(jsonElement, jsonWriter);
        }
        catch (IOException e) {
            throw new JsonIOException(e);
        }
    }
    
    public JsonWriter newJsonWriter(final Writer writer) throws IOException {
        if (this.generateNonExecutableJson) {
            writer.write(")]}'\n");
        }
        final JsonWriter jsonWriter = new JsonWriter(writer);
        if (this.prettyPrinting) {
            jsonWriter.setIndent("  ");
        }
        jsonWriter.setSerializeNulls(this.serializeNulls);
        return jsonWriter;
    }
    
    public JsonReader newJsonReader(final Reader reader) {
        final JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(this.lenient);
        return jsonReader;
    }
    
    public void toJson(final JsonElement jsonElement, final JsonWriter writer) throws JsonIOException {
        final boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        final boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(this.htmlSafe);
        final boolean oldSerializeNulls = writer.getSerializeNulls();
        writer.setSerializeNulls(this.serializeNulls);
        try {
            Streams.write(jsonElement, writer);
        }
        catch (IOException e) {
            throw new JsonIOException(e);
        }
        catch (AssertionError e2) {
            final AssertionError error = new AssertionError((Object)("AssertionError (GSON 2.8.8): " + e2.getMessage()));
            error.initCause(e2);
            throw error;
        }
        finally {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        }
    }
    
    public <T> T fromJson(final String json, final Class<T> classOfT) throws JsonSyntaxException {
        final Object object = this.fromJson(json, (Type)classOfT);
        return Primitives.wrap(classOfT).cast(object);
    }
    
    public <T> T fromJson(final String json, final Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        final StringReader reader = new StringReader(json);
        final T target = this.fromJson(reader, typeOfT);
        return target;
    }
    
    public <T> T fromJson(final Reader json, final Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        final JsonReader jsonReader = this.newJsonReader(json);
        final Object object = this.fromJson(jsonReader, classOfT);
        assertFullConsumption(object, jsonReader);
        return Primitives.wrap(classOfT).cast(object);
    }
    
    public <T> T fromJson(final Reader json, final Type typeOfT) throws JsonIOException, JsonSyntaxException {
        final JsonReader jsonReader = this.newJsonReader(json);
        final T object = this.fromJson(jsonReader, typeOfT);
        assertFullConsumption(object, jsonReader);
        return object;
    }
    
    private static void assertFullConsumption(final Object obj, final JsonReader reader) {
        try {
            if (obj != null && reader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
        }
        catch (MalformedJsonException e) {
            throw new JsonSyntaxException(e);
        }
        catch (IOException e2) {
            throw new JsonIOException(e2);
        }
    }
    
    public <T> T fromJson(final JsonReader reader, final Type typeOfT) throws JsonIOException, JsonSyntaxException {
        boolean isEmpty = true;
        final boolean oldLenient = reader.isLenient();
        reader.setLenient(true);
        try {
            reader.peek();
            isEmpty = false;
            final TypeToken<T> typeToken = (TypeToken<T>)TypeToken.get(typeOfT);
            final TypeAdapter<T> typeAdapter = this.getAdapter(typeToken);
            final T object = typeAdapter.read(reader);
            return object;
        }
        catch (EOFException e) {
            if (isEmpty) {
                return null;
            }
            throw new JsonSyntaxException(e);
        }
        catch (IllegalStateException e2) {
            throw new JsonSyntaxException(e2);
        }
        catch (IOException e3) {
            throw new JsonSyntaxException(e3);
        }
        catch (AssertionError e4) {
            final AssertionError error = new AssertionError((Object)("AssertionError (GSON 2.8.8): " + e4.getMessage()));
            error.initCause(e4);
            throw error;
        }
        finally {
            reader.setLenient(oldLenient);
        }
    }
    
    public <T> T fromJson(final JsonElement json, final Class<T> classOfT) throws JsonSyntaxException {
        final Object object = this.fromJson(json, (Type)classOfT);
        return Primitives.wrap(classOfT).cast(object);
    }
    
    public <T> T fromJson(final JsonElement json, final Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return this.fromJson(new JsonTreeReader(json), typeOfT);
    }
    
    @Override
    public String toString() {
        return "{serializeNulls:" + this.serializeNulls + ",factories:" + this.factories + ",instanceCreators:" + this.constructorConstructor + "}";
    }
    
    static {
        NULL_KEY_SURROGATE = TypeToken.get((Class<?>)Object.class);
    }
    
    static class FutureTypeAdapter<T> extends TypeAdapter<T>
    {
        private TypeAdapter<T> delegate;
        
        public void setDelegate(final TypeAdapter<T> typeAdapter) {
            if (this.delegate != null) {
                throw new AssertionError();
            }
            this.delegate = typeAdapter;
        }
        
        @Override
        public T read(final JsonReader in) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            return this.delegate.read(in);
        }
        
        @Override
        public void write(final JsonWriter out, final T value) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            this.delegate.write(out, value);
        }
    }
}
