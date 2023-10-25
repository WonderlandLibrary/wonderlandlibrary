package org.apache.commons.logging.impl;

import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;

public class AvalonLogger implements Log {
  private static volatile Logger defaultLogger = null;
  
  private final transient Logger logger;
  
  public AvalonLogger(Logger logger) {
    this.logger = logger;
  }
  
  public AvalonLogger(String name) {
    if (defaultLogger == null)
      throw new NullPointerException("default logger has to be specified if this constructor is used!"); 
    this.logger = defaultLogger.getChildLogger(name);
  }
  
  public Logger getLogger() {
    return this.logger;
  }
  
  public static void setDefaultLogger(Logger logger) {
    defaultLogger = logger;
  }
  
  public void debug(Object message, Throwable t) {
    if (getLogger().isDebugEnabled())
      getLogger().debug(String.valueOf(message), t); 
  }
  
  public void debug(Object message) {
    if (getLogger().isDebugEnabled())
      getLogger().debug(String.valueOf(message)); 
  }
  
  public void error(Object message, Throwable t) {
    if (getLogger().isErrorEnabled())
      getLogger().error(String.valueOf(message), t); 
  }
  
  public void error(Object message) {
    if (getLogger().isErrorEnabled())
      getLogger().error(String.valueOf(message)); 
  }
  
  public void fatal(Object message, Throwable t) {
    if (getLogger().isFatalErrorEnabled())
      getLogger().fatalError(String.valueOf(message), t); 
  }
  
  public void fatal(Object message) {
    if (getLogger().isFatalErrorEnabled())
      getLogger().fatalError(String.valueOf(message)); 
  }
  
  public void info(Object message, Throwable t) {
    if (getLogger().isInfoEnabled())
      getLogger().info(String.valueOf(message), t); 
  }
  
  public void info(Object message) {
    if (getLogger().isInfoEnabled())
      getLogger().info(String.valueOf(message)); 
  }
  
  public boolean isDebugEnabled() {
    return getLogger().isDebugEnabled();
  }
  
  public boolean isErrorEnabled() {
    return getLogger().isErrorEnabled();
  }
  
  public boolean isFatalEnabled() {
    return getLogger().isFatalErrorEnabled();
  }
  
  public boolean isInfoEnabled() {
    return getLogger().isInfoEnabled();
  }
  
  public boolean isTraceEnabled() {
    return getLogger().isDebugEnabled();
  }
  
  public boolean isWarnEnabled() {
    return getLogger().isWarnEnabled();
  }
  
  public void trace(Object message, Throwable t) {
    if (getLogger().isDebugEnabled())
      getLogger().debug(String.valueOf(message), t); 
  }
  
  public void trace(Object message) {
    if (getLogger().isDebugEnabled())
      getLogger().debug(String.valueOf(message)); 
  }
  
  public void warn(Object message, Throwable t) {
    if (getLogger().isWarnEnabled())
      getLogger().warn(String.valueOf(message), t); 
  }
  
  public void warn(Object message) {
    if (getLogger().isWarnEnabled())
      getLogger().warn(String.valueOf(message)); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\logging\impl\AvalonLogger.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */