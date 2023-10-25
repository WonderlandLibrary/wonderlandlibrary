package org.apache.logging.log4j.core.impl;

import java.lang.reflect.Constructor;
import java.util.Map;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.util.IndexedStringMap;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.StringMap;

public class ContextDataFactory {
  private static final String CLASS_NAME = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextData");
  
  private static final Class<? extends StringMap> CACHED_CLASS = createCachedClass(CLASS_NAME);
  
  private static final Constructor<?> DEFAULT_CONSTRUCTOR = createDefaultConstructor(CACHED_CLASS);
  
  private static final Constructor<?> INITIAL_CAPACITY_CONSTRUCTOR = createInitialCapacityConstructor(CACHED_CLASS);
  
  private static final StringMap EMPTY_STRING_MAP = createContextData(0);
  
  static {
    EMPTY_STRING_MAP.freeze();
  }
  
  private static Class<? extends StringMap> createCachedClass(String className) {
    if (className == null)
      return null; 
    try {
      return Loader.loadClass(className).asSubclass(IndexedStringMap.class);
    } catch (Exception any) {
      return null;
    } 
  }
  
  private static Constructor<?> createDefaultConstructor(Class<? extends StringMap> cachedClass) {
    if (cachedClass == null)
      return null; 
    try {
      return cachedClass.getConstructor(new Class[0]);
    } catch (NoSuchMethodException|IllegalAccessError ignored) {
      return null;
    } 
  }
  
  private static Constructor<?> createInitialCapacityConstructor(Class<? extends StringMap> cachedClass) {
    if (cachedClass == null)
      return null; 
    try {
      return cachedClass.getConstructor(new Class[] { int.class });
    } catch (NoSuchMethodException|IllegalAccessError ignored) {
      return null;
    } 
  }
  
  public static StringMap createContextData() {
    if (DEFAULT_CONSTRUCTOR == null)
      return (StringMap)new SortedArrayStringMap(); 
    try {
      return (StringMap)DEFAULT_CONSTRUCTOR.newInstance(new Object[0]);
    } catch (Throwable ignored) {
      return (StringMap)new SortedArrayStringMap();
    } 
  }
  
  public static StringMap createContextData(int initialCapacity) {
    if (INITIAL_CAPACITY_CONSTRUCTOR == null)
      return (StringMap)new SortedArrayStringMap(initialCapacity); 
    try {
      return (StringMap)INITIAL_CAPACITY_CONSTRUCTOR.newInstance(new Object[] { Integer.valueOf(initialCapacity) });
    } catch (Throwable ignored) {
      return (StringMap)new SortedArrayStringMap(initialCapacity);
    } 
  }
  
  public static StringMap createContextData(Map<String, String> context) {
    StringMap contextData = createContextData(context.size());
    for (Map.Entry<String, String> entry : context.entrySet())
      contextData.putValue(entry.getKey(), entry.getValue()); 
    return contextData;
  }
  
  public static StringMap createContextData(ReadOnlyStringMap readOnlyStringMap) {
    StringMap contextData = createContextData(readOnlyStringMap.size());
    contextData.putAll(readOnlyStringMap);
    return contextData;
  }
  
  public static StringMap emptyFrozenContextData() {
    return EMPTY_STRING_MAP;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ContextDataFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */