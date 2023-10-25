package org.apache.logging.log4j.simple;

import java.net.URI;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class SimpleLoggerContextFactory implements LoggerContextFactory {
  private static LoggerContext context = new SimpleLoggerContext();
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext) {
    return context;
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, URI configLocation, String name) {
    return context;
  }
  
  public void removeContext(LoggerContext removeContext) {}
  
  public boolean isClassLoaderDependent() {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\simple\SimpleLoggerContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */