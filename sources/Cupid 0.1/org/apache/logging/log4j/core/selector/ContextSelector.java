package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LoggerContext;

public interface ContextSelector {
  public static final long DEFAULT_STOP_TIMEOUT = 50L;
  
  default void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
    if (hasContext(fqcn, loader, currentContext))
      getContext(fqcn, loader, currentContext).stop(50L, TimeUnit.MILLISECONDS); 
  }
  
  default boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
    return false;
  }
  
  LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean);
  
  default LoggerContext getContext(String fqcn, ClassLoader loader, Map.Entry<String, Object> entry, boolean currentContext) {
    LoggerContext lc = getContext(fqcn, loader, currentContext);
    if (lc != null)
      lc.putObject(entry.getKey(), entry.getValue()); 
    return lc;
  }
  
  LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean, URI paramURI);
  
  default LoggerContext getContext(String fqcn, ClassLoader loader, Map.Entry<String, Object> entry, boolean currentContext, URI configLocation) {
    LoggerContext lc = getContext(fqcn, loader, currentContext, configLocation);
    if (lc != null)
      lc.putObject(entry.getKey(), entry.getValue()); 
    return lc;
  }
  
  List<LoggerContext> getLoggerContexts();
  
  void removeContext(LoggerContext paramLoggerContext);
  
  default boolean isClassLoaderDependent() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\selector\ContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */