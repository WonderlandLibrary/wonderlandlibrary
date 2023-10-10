// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.IndexedStringMap;
import org.apache.logging.log4j.core.util.Loader;
import java.lang.reflect.Constructor;
import org.apache.logging.log4j.util.StringMap;

public class ContextDataFactory
{
    private static final String CLASS_NAME;
    private static final Class<? extends StringMap> CACHED_CLASS;
    private static final Constructor<?> DEFAULT_CONSTRUCTOR;
    private static final Constructor<?> INITIAL_CAPACITY_CONSTRUCTOR;
    private static final StringMap EMPTY_STRING_MAP;
    
    private static Class<? extends StringMap> createCachedClass(final String className) {
        if (className == null) {
            return null;
        }
        try {
            return Loader.loadClass(className).asSubclass(IndexedStringMap.class);
        }
        catch (Exception any) {
            return null;
        }
    }
    
    private static Constructor<?> createDefaultConstructor(final Class<? extends StringMap> cachedClass) {
        if (cachedClass == null) {
            return null;
        }
        try {
            return cachedClass.getConstructor((Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException | IllegalAccessError ex) {
            final Throwable t;
            final Throwable ignored = t;
            return null;
        }
    }
    
    private static Constructor<?> createInitialCapacityConstructor(final Class<? extends StringMap> cachedClass) {
        if (cachedClass == null) {
            return null;
        }
        try {
            return cachedClass.getConstructor(Integer.TYPE);
        }
        catch (NoSuchMethodException | IllegalAccessError ex) {
            final Throwable t;
            final Throwable ignored = t;
            return null;
        }
    }
    
    public static StringMap createContextData() {
        if (ContextDataFactory.DEFAULT_CONSTRUCTOR == null) {
            return new SortedArrayStringMap();
        }
        try {
            return (IndexedStringMap)ContextDataFactory.DEFAULT_CONSTRUCTOR.newInstance(new Object[0]);
        }
        catch (Throwable ignored) {
            return new SortedArrayStringMap();
        }
    }
    
    public static StringMap createContextData(final int initialCapacity) {
        if (ContextDataFactory.INITIAL_CAPACITY_CONSTRUCTOR == null) {
            return new SortedArrayStringMap(initialCapacity);
        }
        try {
            return (IndexedStringMap)ContextDataFactory.INITIAL_CAPACITY_CONSTRUCTOR.newInstance(initialCapacity);
        }
        catch (Throwable ignored) {
            return new SortedArrayStringMap(initialCapacity);
        }
    }
    
    public static StringMap createContextData(final Map<String, String> context) {
        final StringMap contextData = createContextData(context.size());
        for (final Map.Entry<String, String> entry : context.entrySet()) {
            contextData.putValue(entry.getKey(), entry.getValue());
        }
        return contextData;
    }
    
    public static StringMap createContextData(final ReadOnlyStringMap readOnlyStringMap) {
        final StringMap contextData = createContextData(readOnlyStringMap.size());
        contextData.putAll(readOnlyStringMap);
        return contextData;
    }
    
    public static StringMap emptyFrozenContextData() {
        return ContextDataFactory.EMPTY_STRING_MAP;
    }
    
    static {
        CLASS_NAME = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextData");
        CACHED_CLASS = createCachedClass(ContextDataFactory.CLASS_NAME);
        DEFAULT_CONSTRUCTOR = createDefaultConstructor(ContextDataFactory.CACHED_CLASS);
        INITIAL_CAPACITY_CONSTRUCTOR = createInitialCapacityConstructor(ContextDataFactory.CACHED_CLASS);
        (EMPTY_STRING_MAP = createContextData(0)).freeze();
    }
}
