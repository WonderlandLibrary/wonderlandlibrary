package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.message.MessageFactory;

public class LoggerRegistry<T extends ExtendedLogger> {
  private static final String DEFAULT_FACTORY_KEY = AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName();
  
  private final MapFactory<T> factory;
  
  private final Map<String, Map<String, T>> map;
  
  public static interface MapFactory<T extends ExtendedLogger> {
    Map<String, T> createInnerMap();
    
    Map<String, Map<String, T>> createOuterMap();
    
    void putIfAbsent(Map<String, T> param1Map, String param1String, T param1T);
  }
  
  public static class ConcurrentMapFactory<T extends ExtendedLogger> implements MapFactory<T> {
    public Map<String, T> createInnerMap() {
      return new ConcurrentHashMap<>();
    }
    
    public Map<String, Map<String, T>> createOuterMap() {
      return new ConcurrentHashMap<>();
    }
    
    public void putIfAbsent(Map<String, T> innerMap, String name, T logger) {
      ((ConcurrentMap<String, T>)innerMap).putIfAbsent(name, logger);
    }
  }
  
  public static class WeakMapFactory<T extends ExtendedLogger> implements MapFactory<T> {
    public Map<String, T> createInnerMap() {
      return new WeakHashMap<>();
    }
    
    public Map<String, Map<String, T>> createOuterMap() {
      return new WeakHashMap<>();
    }
    
    public void putIfAbsent(Map<String, T> innerMap, String name, T logger) {
      innerMap.put(name, logger);
    }
  }
  
  public LoggerRegistry() {
    this(new ConcurrentMapFactory<>());
  }
  
  public LoggerRegistry(MapFactory<T> factory) {
    this.factory = Objects.<MapFactory<T>>requireNonNull(factory, "factory");
    this.map = factory.createOuterMap();
  }
  
  private static String factoryClassKey(Class<? extends MessageFactory> messageFactoryClass) {
    return (messageFactoryClass == null) ? DEFAULT_FACTORY_KEY : messageFactoryClass.getName();
  }
  
  private static String factoryKey(MessageFactory messageFactory) {
    return (messageFactory == null) ? DEFAULT_FACTORY_KEY : messageFactory.getClass().getName();
  }
  
  public T getLogger(String name) {
    return getOrCreateInnerMap(DEFAULT_FACTORY_KEY).get(name);
  }
  
  public T getLogger(String name, MessageFactory messageFactory) {
    return getOrCreateInnerMap(factoryKey(messageFactory)).get(name);
  }
  
  public Collection<T> getLoggers() {
    return getLoggers(new ArrayList<>());
  }
  
  public Collection<T> getLoggers(Collection<T> destination) {
    for (Map<String, T> inner : this.map.values())
      destination.addAll(inner.values()); 
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
    return getOrCreateInnerMap(DEFAULT_FACTORY_KEY).containsKey(name);
  }
  
  public boolean hasLogger(String name, MessageFactory messageFactory) {
    return getOrCreateInnerMap(factoryKey(messageFactory)).containsKey(name);
  }
  
  public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
    return getOrCreateInnerMap(factoryClassKey(messageFactoryClass)).containsKey(name);
  }
  
  public void putIfAbsent(String name, MessageFactory messageFactory, T logger) {
    this.factory.putIfAbsent(getOrCreateInnerMap(factoryKey(messageFactory)), name, logger);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\LoggerRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */