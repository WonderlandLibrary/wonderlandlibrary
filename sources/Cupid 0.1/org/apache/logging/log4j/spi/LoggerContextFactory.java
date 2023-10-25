package org.apache.logging.log4j.spi;

import java.net.URI;

public interface LoggerContextFactory {
  default void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
    if (hasContext(fqcn, loader, currentContext)) {
      LoggerContext ctx = getContext(fqcn, loader, null, currentContext);
      if (ctx instanceof Terminable)
        ((Terminable)ctx).terminate(); 
    } 
  }
  
  default boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
    return false;
  }
  
  LoggerContext getContext(String paramString, ClassLoader paramClassLoader, Object paramObject, boolean paramBoolean);
  
  LoggerContext getContext(String paramString1, ClassLoader paramClassLoader, Object paramObject, boolean paramBoolean, URI paramURI, String paramString2);
  
  void removeContext(LoggerContext paramLoggerContext);
  
  default boolean isClassLoaderDependent() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\LoggerContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */