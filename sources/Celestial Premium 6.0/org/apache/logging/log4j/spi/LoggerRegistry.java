/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLogger;

public class LoggerRegistry<T extends ExtendedLogger> {
    private static final String DEFAULT_FACTORY_KEY = AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName();
    private final MapFactory<T> factory;
    private final Map<String, Map<String, T>> map;

    public LoggerRegistry() {
        this(new ConcurrentMapFactory());
    }

    public LoggerRegistry(MapFactory<T> factory) {
        this.factory = Objects.requireNonNull(factory, "factory");
        this.map = factory.createOuterMap();
    }

    private static String factoryClassKey(Class<? extends MessageFactory> messageFactoryClass) {
        return messageFactoryClass == null ? DEFAULT_FACTORY_KEY : messageFactoryClass.getName();
    }

    private static String factoryKey(MessageFactory messageFactory) {
        return messageFactory == null ? DEFAULT_FACTORY_KEY : messageFactory.getClass().getName();
    }

    public T getLogger(String name) {
        return (T)((ExtendedLogger)this.getOrCreateInnerMap(DEFAULT_FACTORY_KEY).get(name));
    }

    public T getLogger(String name, MessageFactory messageFactory) {
        return (T)((ExtendedLogger)this.getOrCreateInnerMap(LoggerRegistry.factoryKey(messageFactory)).get(name));
    }

    public Collection<T> getLoggers() {
        return this.getLoggers(new ArrayList());
    }

    public Collection<T> getLoggers(Collection<T> destination) {
        for (Map<String, T> inner : this.map.values()) {
            destination.addAll(inner.values());
        }
        return destination;
    }

    private Map<String, T> getOrCreateInnerMap(String factoryName) {
        Map<String, T> inner = this.map.get(factoryName);
        if (inner == null) {
            inner = this.factory.createInnerMap();
            this.map.put(factoryName, inner);
        }
        return inner;
    }

    public boolean hasLogger(String name) {
        return this.getOrCreateInnerMap(DEFAULT_FACTORY_KEY).containsKey(name);
    }

    public boolean hasLogger(String name, MessageFactory messageFactory) {
        return this.getOrCreateInnerMap(LoggerRegistry.factoryKey(messageFactory)).containsKey(name);
    }

    public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
        return this.getOrCreateInnerMap(LoggerRegistry.factoryClassKey(messageFactoryClass)).containsKey(name);
    }

    public void putIfAbsent(String name, MessageFactory messageFactory, T logger) {
        this.factory.putIfAbsent(this.getOrCreateInnerMap(LoggerRegistry.factoryKey(messageFactory)), name, logger);
    }

    public static class WeakMapFactory<T extends ExtendedLogger>
    implements MapFactory<T> {
        @Override
        public Map<String, T> createInnerMap() {
            return new WeakHashMap();
        }

        @Override
        public Map<String, Map<String, T>> createOuterMap() {
            return new WeakHashMap<String, Map<String, T>>();
        }

        @Override
        public void putIfAbsent(Map<String, T> innerMap, String name, T logger) {
            innerMap.put(name, logger);
        }
    }

    public static class ConcurrentMapFactory<T extends ExtendedLogger>
    implements MapFactory<T> {
        @Override
        public Map<String, T> createInnerMap() {
            return new ConcurrentHashMap();
        }

        @Override
        public Map<String, Map<String, T>> createOuterMap() {
            return new ConcurrentHashMap<String, Map<String, T>>();
        }

        @Override
        public void putIfAbsent(Map<String, T> innerMap, String name, T logger) {
            ((ConcurrentMap)innerMap).putIfAbsent(name, logger);
        }
    }

    public static interface MapFactory<T extends ExtendedLogger> {
        public Map<String, T> createInnerMap();

        public Map<String, Map<String, T>> createOuterMap();

        public void putIfAbsent(Map<String, T> var1, String var2, T var3);
    }
}

