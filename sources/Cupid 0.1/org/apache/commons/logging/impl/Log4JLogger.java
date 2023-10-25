package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4JLogger implements Log, Serializable {
  private static final long serialVersionUID = 5160705895411730424L;
  
  private static final String FQCN = Log4JLogger.class.getName();
  
  static {
    Level level;
  }
  
  private volatile transient Logger logger = null;
  
  private final String name;
  
  private static final Priority traceLevel;
  
  static {
    if (!Priority.class.isAssignableFrom(Level.class))
      throw new InstantiationError("Log4J 1.2 not available"); 
    try {
      Priority _traceLevel = (Priority)Level.class.getDeclaredField("TRACE").get(null);
    } catch (Exception ex) {
      level = Level.DEBUG;
    } 
    traceLevel = (Priority)level;
  }
  
  public Log4JLogger() {
    this.name = null;
  }
  
  public Log4JLogger(String name) {
    this.name = name;
    this.logger = getLogger();
  }
  
  public Log4JLogger(Logger logger) {
    if (logger == null)
      throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration."); 
    this.name = logger.getName();
    this.logger = logger;
  }
  
  public void trace(Object message) {
    getLogger().log(FQCN, traceLevel, message, null);
  }
  
  public void trace(Object message, Throwable t) {
    getLogger().log(FQCN, traceLevel, message, t);
  }
  
  public void debug(Object message) {
    getLogger().log(FQCN, (Priority)Level.DEBUG, message, null);
  }
  
  public void debug(Object message, Throwable t) {
    getLogger().log(FQCN, (Priority)Level.DEBUG, message, t);
  }
  
  public void info(Object message) {
    getLogger().log(FQCN, (Priority)Level.INFO, message, null);
  }
  
  public void info(Object message, Throwable t) {
    getLogger().log(FQCN, (Priority)Level.INFO, message, t);
  }
  
  public void warn(Object message) {
    getLogger().log(FQCN, (Priority)Level.WARN, message, null);
  }
  
  public void warn(Object message, Throwable t) {
    getLogger().log(FQCN, (Priority)Level.WARN, message, t);
  }
  
  public void error(Object message) {
    getLogger().log(FQCN, (Priority)Level.ERROR, message, null);
  }
  
  public void error(Object message, Throwable t) {
    getLogger().log(FQCN, (Priority)Level.ERROR, message, t);
  }
  
  public void fatal(Object message) {
    getLogger().log(FQCN, (Priority)Level.FATAL, message, null);
  }
  
  public void fatal(Object message, Throwable t) {
    getLogger().log(FQCN, (Priority)Level.FATAL, message, t);
  }
  
  public Logger getLogger() {
    Logger result = this.logger;
    if (result == null)
      synchronized (this) {
        result = this.logger;
        if (result == null)
          this.logger = result = Logger.getLogger(this.name); 
      }  
    return result;
  }
  
  public boolean isDebugEnabled() {
    return getLogger().isDebugEnabled();
  }
  
  public boolean isErrorEnabled() {
    return getLogger().isEnabledFor((Priority)Level.ERROR);
  }
  
  public boolean isFatalEnabled() {
    return getLogger().isEnabledFor((Priority)Level.FATAL);
  }
  
  public boolean isInfoEnabled() {
    return getLogger().isInfoEnabled();
  }
  
  public boolean isTraceEnabled() {
    return getLogger().isEnabledFor(traceLevel);
  }
  
  public boolean isWarnEnabled() {
    return getLogger().isEnabledFor((Priority)Level.WARN);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\logging\impl\Log4JLogger.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */