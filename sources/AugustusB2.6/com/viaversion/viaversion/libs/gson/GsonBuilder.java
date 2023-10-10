// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Collections;
import com.viaversion.viaversion.libs.gson.internal.bind.TypeAdapters;
import com.viaversion.viaversion.libs.gson.internal.bind.TreeTypeAdapter;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.internal.$Gson$Preconditions;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Map;
import com.viaversion.viaversion.libs.gson.internal.Excluder;

public final class GsonBuilder
{
    private Excluder excluder;
    private LongSerializationPolicy longSerializationPolicy;
    private FieldNamingStrategy fieldNamingPolicy;
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    private final List<TypeAdapterFactory> factories;
    private final List<TypeAdapterFactory> hierarchyFactories;
    private boolean serializeNulls;
    private String datePattern;
    private int dateStyle;
    private int timeStyle;
    private boolean complexMapKeySerialization;
    private boolean serializeSpecialFloatingPointValues;
    private boolean escapeHtmlChars;
    private boolean prettyPrinting;
    private boolean generateNonExecutableJson;
    private boolean lenient;
    
    public GsonBuilder() {
        this.excluder = Excluder.DEFAULT;
        this.longSerializationPolicy = LongSerializationPolicy.DEFAULT;
        this.fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
        this.instanceCreators = new HashMap<Type, InstanceCreator<?>>();
        this.factories = new ArrayList<TypeAdapterFactory>();
        this.hierarchyFactories = new ArrayList<TypeAdapterFactory>();
        this.serializeNulls = false;
        this.dateStyle = 2;
        this.timeStyle = 2;
        this.complexMapKeySerialization = false;
        this.serializeSpecialFloatingPointValues = false;
        this.escapeHtmlChars = true;
        this.prettyPrinting = false;
        this.generateNonExecutableJson = false;
        this.lenient = false;
    }
    
    GsonBuilder(final Gson gson) {
        this.excluder = Excluder.DEFAULT;
        this.longSerializationPolicy = LongSerializationPolicy.DEFAULT;
        this.fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
        this.instanceCreators = new HashMap<Type, InstanceCreator<?>>();
        this.factories = new ArrayList<TypeAdapterFactory>();
        this.hierarchyFactories = new ArrayList<TypeAdapterFactory>();
        this.serializeNulls = false;
        this.dateStyle = 2;
        this.timeStyle = 2;
        this.complexMapKeySerialization = false;
        this.serializeSpecialFloatingPointValues = false;
        this.escapeHtmlChars = true;
        this.prettyPrinting = false;
        this.generateNonExecutableJson = false;
        this.lenient = false;
        this.excluder = gson.excluder;
        this.fieldNamingPolicy = gson.fieldNamingStrategy;
        this.instanceCreators.putAll(gson.instanceCreators);
        this.serializeNulls = gson.serializeNulls;
        this.complexMapKeySerialization = gson.complexMapKeySerialization;
        this.generateNonExecutableJson = gson.generateNonExecutableJson;
        this.escapeHtmlChars = gson.htmlSafe;
        this.prettyPrinting = gson.prettyPrinting;
        this.lenient = gson.lenient;
        this.serializeSpecialFloatingPointValues = gson.serializeSpecialFloatingPointValues;
        this.longSerializationPolicy = gson.longSerializationPolicy;
        this.datePattern = gson.datePattern;
        this.dateStyle = gson.dateStyle;
        this.timeStyle = gson.timeStyle;
        this.factories.addAll(gson.builderFactories);
        this.hierarchyFactories.addAll(gson.builderHierarchyFactories);
    }
    
    public GsonBuilder setVersion(final double ignoreVersionsAfter) {
        this.excluder = this.excluder.withVersion(ignoreVersionsAfter);
        return this;
    }
    
    public GsonBuilder excludeFieldsWithModifiers(final int... modifiers) {
        this.excluder = this.excluder.withModifiers(modifiers);
        return this;
    }
    
    public GsonBuilder generateNonExecutableJson() {
        this.generateNonExecutableJson = true;
        return this;
    }
    
    public GsonBuilder excludeFieldsWithoutExposeAnnotation() {
        this.excluder = this.excluder.excludeFieldsWithoutExposeAnnotation();
        return this;
    }
    
    public GsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }
    
    public GsonBuilder enableComplexMapKeySerialization() {
        this.complexMapKeySerialization = true;
        return this;
    }
    
    public GsonBuilder disableInnerClassSerialization() {
        this.excluder = this.excluder.disableInnerClassSerialization();
        return this;
    }
    
    public GsonBuilder setLongSerializationPolicy(final LongSerializationPolicy serializationPolicy) {
        this.longSerializationPolicy = serializationPolicy;
        return this;
    }
    
    public GsonBuilder setFieldNamingPolicy(final FieldNamingPolicy namingConvention) {
        this.fieldNamingPolicy = namingConvention;
        return this;
    }
    
    public GsonBuilder setFieldNamingStrategy(final FieldNamingStrategy fieldNamingStrategy) {
        this.fieldNamingPolicy = fieldNamingStrategy;
        return this;
    }
    
    public GsonBuilder setExclusionStrategies(final ExclusionStrategy... strategies) {
        for (final ExclusionStrategy strategy : strategies) {
            this.excluder = this.excluder.withExclusionStrategy(strategy, true, true);
        }
        return this;
    }
    
    public GsonBuilder addSerializationExclusionStrategy(final ExclusionStrategy strategy) {
        this.excluder = this.excluder.withExclusionStrategy(strategy, true, false);
        return this;
    }
    
    public GsonBuilder addDeserializationExclusionStrategy(final ExclusionStrategy strategy) {
        this.excluder = this.excluder.withExclusionStrategy(strategy, false, true);
        return this;
    }
    
    public GsonBuilder setPrettyPrinting() {
        this.prettyPrinting = true;
        return this;
    }
    
    public GsonBuilder setLenient() {
        this.lenient = true;
        return this;
    }
    
    public GsonBuilder disableHtmlEscaping() {
        this.escapeHtmlChars = false;
        return this;
    }
    
    public GsonBuilder setDateFormat(final String pattern) {
        this.datePattern = pattern;
        return this;
    }
    
    public GsonBuilder setDateFormat(final int style) {
        this.dateStyle = style;
        this.datePattern = null;
        return this;
    }
    
    public GsonBuilder setDateFormat(final int dateStyle, final int timeStyle) {
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.datePattern = null;
        return this;
    }
    
    public GsonBuilder registerTypeAdapter(final Type type, final Object typeAdapter) {
        $Gson$Preconditions.checkArgument(typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer || typeAdapter instanceof InstanceCreator || typeAdapter instanceof TypeAdapter);
        if (typeAdapter instanceof InstanceCreator) {
            this.instanceCreators.put(type, (InstanceCreator<?>)typeAdapter);
        }
        if (typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer) {
            final TypeToken<?> typeToken = TypeToken.get(type);
            this.factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter) {
            this.factories.add(TypeAdapters.newFactory(TypeToken.get(type), (TypeAdapter<?>)typeAdapter));
        }
        return this;
    }
    
    public GsonBuilder registerTypeAdapterFactory(final TypeAdapterFactory factory) {
        this.factories.add(factory);
        return this;
    }
    
    public GsonBuilder registerTypeHierarchyAdapter(final Class<?> baseType, final Object typeAdapter) {
        $Gson$Preconditions.checkArgument(typeAdapter instanceof JsonSerializer || typeAdapter instanceof JsonDeserializer || typeAdapter instanceof TypeAdapter);
        if (typeAdapter instanceof JsonDeserializer || typeAdapter instanceof JsonSerializer) {
            this.hierarchyFactories.add(TreeTypeAdapter.newTypeHierarchyFactory(baseType, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter) {
            this.factories.add(TypeAdapters.newTypeHierarchyFactory(baseType, (TypeAdapter<?>)typeAdapter));
        }
        return this;
    }
    
    public GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }
    
    public Gson create() {
        final List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>(this.factories.size() + this.hierarchyFactories.size() + 3);
        factories.addAll(this.factories);
        Collections.reverse(factories);
        final List<TypeAdapterFactory> hierarchyFactories = new ArrayList<TypeAdapterFactory>(this.hierarchyFactories);
        Collections.reverse(hierarchyFactories);
        factories.addAll(hierarchyFactories);
        this.addTypeAdaptersForDate(this.datePattern, this.dateStyle, this.timeStyle, factories);
        return new Gson(this.excluder, this.fieldNamingPolicy, this.instanceCreators, this.serializeNulls, this.complexMapKeySerialization, this.generateNonExecutableJson, this.escapeHtmlChars, this.prettyPrinting, this.lenient, this.serializeSpecialFloatingPointValues, this.longSerializationPolicy, this.datePattern, this.dateStyle, this.timeStyle, this.factories, this.hierarchyFactories, factories);
    }
    
    private void addTypeAdaptersForDate(final String datePattern, final int dateStyle, final int timeStyle, final List<TypeAdapterFactory> factories) {
        DefaultDateTypeAdapter dateTypeAdapter;
        TypeAdapter<Timestamp> timestampTypeAdapter;
        TypeAdapter<java.sql.Date> javaSqlDateTypeAdapter;
        if (datePattern != null && !"".equals(datePattern.trim())) {
            dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);
            timestampTypeAdapter = (TypeAdapter<Timestamp>)new DefaultDateTypeAdapter(Timestamp.class, datePattern);
            javaSqlDateTypeAdapter = (TypeAdapter<java.sql.Date>)new DefaultDateTypeAdapter(java.sql.Date.class, datePattern);
        }
        else {
            if (dateStyle == 2 || timeStyle == 2) {
                return;
            }
            dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, dateStyle, timeStyle);
            timestampTypeAdapter = (TypeAdapter<Timestamp>)new DefaultDateTypeAdapter(Timestamp.class, dateStyle, timeStyle);
            javaSqlDateTypeAdapter = (TypeAdapter<java.sql.Date>)new DefaultDateTypeAdapter(java.sql.Date.class, dateStyle, timeStyle);
        }
        factories.add(TypeAdapters.newFactory(Date.class, dateTypeAdapter));
        factories.add(TypeAdapters.newFactory(Timestamp.class, timestampTypeAdapter));
        factories.add(TypeAdapters.newFactory(java.sql.Date.class, javaSqlDateTypeAdapter));
    }
}
