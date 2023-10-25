package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class ContextDataInjectorFactory {
  public static ContextDataInjector createInjector() {
    String className = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextDataInjector");
    if (className == null)
      return createDefaultInjector(); 
    try {
      Class<? extends ContextDataInjector> cls = Loader.loadClass(className).asSubclass(ContextDataInjector.class);
      return cls.newInstance();
    } catch (Exception dynamicFailed) {
      ContextDataInjector result = createDefaultInjector();
      StatusLogger.getLogger().warn("Could not create ContextDataInjector for '{}', using default {}: {}", className, result
          
          .getClass().getName(), dynamicFailed);
      return result;
    } 
  }
  
  private static ContextDataInjector createDefaultInjector() {
    ReadOnlyThreadContextMap threadContextMap = ThreadContext.getThreadContextMap();
    if (threadContextMap instanceof org.apache.logging.log4j.spi.DefaultThreadContextMap || threadContextMap == null)
      return new ThreadContextDataInjector.ForDefaultThreadContextMap(); 
    if (threadContextMap instanceof org.apache.logging.log4j.spi.CopyOnWrite)
      return new ThreadContextDataInjector.ForCopyOnWriteThreadContextMap(); 
    return new ThreadContextDataInjector.ForGarbageFreeThreadContextMap();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ContextDataInjectorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */