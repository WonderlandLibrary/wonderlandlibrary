// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.logging.log4j.message.MessageFactory;
import java.util.Objects;
import java.util.Map;

public class LoggerRegistry<T extends ExtendedLogger>
{
    private static final String DEFAULT_FACTORY_KEY;
    private final MapFactory<T> factory;
    private final Map<String, Map<String, T>> map;
    
    public LoggerRegistry() {
        this((MapFactory)new ConcurrentMapFactory());
    }
    
    public LoggerRegistry(final MapFactory<T> factory) {
        this.factory = Objects.requireNonNull(factory, "factory");
        this.map = factory.createOuterMap();
    }
    
    private static String factoryClassKey(final Class<? extends MessageFactory> messageFactoryClass) {
        return (messageFactoryClass == null) ? LoggerRegistry.DEFAULT_FACTORY_KEY : messageFactoryClass.getName();
    }
    
    private static String factoryKey(final MessageFactory messageFactory) {
        return (messageFactory == null) ? LoggerRegistry.DEFAULT_FACTORY_KEY : messageFactory.getClass().getName();
    }
    
    public T getLogger(final String name) {
        return this.getOrCreateInnerMap(LoggerRegistry.DEFAULT_FACTORY_KEY).get(name);
    }
    
    public T getLogger(final String name, final MessageFactory messageFactory) {
        return this.getOrCreateInnerMap(factoryKey(messageFactory)).get(name);
    }
    
    public Collection<T> getLoggers() {
        return this.getLoggers(new ArrayList<T>());
    }
    
    public Collection<T> getLoggers(final Collection<T> destination) {
        for (final Map<String, T> inner : this.map.values()) {
            destination.addAll((Collection<? extends T>)inner.values());
        }
        return destination;
    }
    
    private Map<String, T> getOrCreateInnerMap(final String factoryName) {
        Map<String, T> inner = this.map.get(factoryName);
        if (inner == null) {
            inner = this.factory.createInnerMap();
            this.map.put(factoryName, inner);
        }
        return inner;
    }
    
    public boolean hasLogger(final String name) {
        return this.getOrCreateInnerMap(LoggerRegistry.DEFAULT_FACTORY_KEY).containsKey(name);
    }
    
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return this.getOrCreateInnerMap(factoryKey(messageFactory)).containsKey(name);
    }
    
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return this.getOrCreateInnerMap(factoryClassKey(messageFactoryClass)).containsKey(name);
    }
    
    public void putIfAbsent(final String name, final MessageFactory messageFactory, final T logger) {
        this.factory.putIfAbsent(this.getOrCreateInnerMap(factoryKey(messageFactory)), name, logger);
    }
    
    static {
        DEFAULT_FACTORY_KEY = AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName();
    }
    
    public static class ConcurrentMapFactory<T extends ExtendedLogger> implements MapFactory<T>
    {
        @Override
        public Map<String, T> createInnerMap() {
            return new ConcurrentHashMap<String, T>();
        }
        
        @Override
        public Map<String, Map<String, T>> createOuterMap() {
            return new ConcurrentHashMap<String, Map<String, T>>();
        }
        
        @Override
        public void putIfAbsent(final Map<String, T> innerMap, final String name, final T logger) {
            ((ConcurrentMap)innerMap).putIfAbsent(name, logger);
        }
    }
    
    public static class WeakMapFactory<T extends ExtendedLogger> implements MapFactory<T>
    {
        @Override
        public Map<String, T> createInnerMap() {
            return new WeakHashMap<String, T>();
        }
        
        @Override
        public Map<String, Map<String, T>> createOuterMap() {
            return new WeakHashMap<String, Map<String, T>>();
        }
        
        @Override
        public void putIfAbsent(final Map<String, T> innerMap, final String name, final T logger) {
            innerMap.put(name, logger);
        }
    }
    
    public interface MapFactory<T extends ExtendedLogger>
    {
        Map<String, T> createInnerMap();
        
        Map<String, Map<String, T>> createOuterMap();
        
        void putIfAbsent(final Map<String, T> innerMap, final String name, final T logger);
    }
}
