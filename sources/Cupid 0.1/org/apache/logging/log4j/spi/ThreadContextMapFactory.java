package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ProviderUtil;

public final class ThreadContextMapFactory {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
  
  private static final String GC_FREE_THREAD_CONTEXT_KEY = "log4j2.garbagefree.threadContextMap";
  
  private static boolean GcFreeThreadContextKey;
  
  private static String ThreadContextMapName;
  
  static {
    initPrivate();
  }
  
  public static void init() {
    CopyOnWriteSortedArrayThreadContextMap.init();
    GarbageFreeSortedArrayThreadContextMap.init();
    DefaultThreadContextMap.init();
    initPrivate();
  }
  
  private static void initPrivate() {
    PropertiesUtil properties = PropertiesUtil.getProperties();
    ThreadContextMapName = properties.getStringProperty("log4j2.threadContextMap");
    GcFreeThreadContextKey = properties.getBooleanProperty("log4j2.garbagefree.threadContextMap");
  }
  
  public static ThreadContextMap createThreadContextMap() {
    ClassLoader cl = ProviderUtil.findClassLoader();
    ThreadContextMap result = null;
    if (ThreadContextMapName != null)
      try {
        Class<?> clazz = cl.loadClass(ThreadContextMapName);
        if (ThreadContextMap.class.isAssignableFrom(clazz))
          result = (ThreadContextMap)clazz.newInstance(); 
      } catch (ClassNotFoundException cnfe) {
        LOGGER.error("Unable to locate configured ThreadContextMap {}", ThreadContextMapName);
      } catch (Exception ex) {
        LOGGER.error("Unable to create configured ThreadContextMap {}", ThreadContextMapName, ex);
      }  
    if (result == null && ProviderUtil.hasProviders() && LogManager.getFactory() != null) {
      String factoryClassName = LogManager.getFactory().getClass().getName();
      for (Provider provider : ProviderUtil.getProviders()) {
        if (factoryClassName.equals(provider.getClassName())) {
          Class<? extends ThreadContextMap> clazz = provider.loadThreadContextMap();
          if (clazz != null)
            try {
              result = clazz.newInstance();
              break;
            } catch (Exception e) {
              LOGGER.error("Unable to locate or load configured ThreadContextMap {}", provider
                  .getThreadContextMap(), e);
              result = createDefaultThreadContextMap();
            }  
        } 
      } 
    } 
    if (result == null)
      result = createDefaultThreadContextMap(); 
    return result;
  }
  
  private static ThreadContextMap createDefaultThreadContextMap() {
    if (Constants.ENABLE_THREADLOCALS) {
      if (GcFreeThreadContextKey)
        return new GarbageFreeSortedArrayThreadContextMap(); 
      return new CopyOnWriteSortedArrayThreadContextMap();
    } 
    return new DefaultThreadContextMap(true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\ThreadContextMapFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */