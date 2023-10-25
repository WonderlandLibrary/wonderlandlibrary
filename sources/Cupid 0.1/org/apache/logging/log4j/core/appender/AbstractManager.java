package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractManager implements AutoCloseable {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final Map<String, AbstractManager> MAP = new HashMap<>();
  
  private static final Lock LOCK = new ReentrantLock();
  
  protected int count;
  
  private final String name;
  
  private final LoggerContext loggerContext;
  
  protected AbstractManager(LoggerContext loggerContext, String name) {
    this.loggerContext = loggerContext;
    this.name = name;
    LOGGER.debug("Starting {} {}", getClass().getSimpleName(), name);
  }
  
  public void close() {
    stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    boolean stopped = true;
    LOCK.lock();
    try {
      this.count--;
      if (this.count <= 0) {
        MAP.remove(this.name);
        LOGGER.debug("Shutting down {} {}", getClass().getSimpleName(), getName());
        stopped = releaseSub(timeout, timeUnit);
        LOGGER.debug("Shut down {} {}, all resources released: {}", getClass().getSimpleName(), getName(), Boolean.valueOf(stopped));
      } 
    } finally {
      LOCK.unlock();
    } 
    return stopped;
  }
  
  public static <M extends AbstractManager, T> M getManager(String name, ManagerFactory<M, T> factory, T data) {
    LOCK.lock();
    try {
      AbstractManager abstractManager = MAP.get(name);
      if (abstractManager == null) {
        abstractManager = ((ManagerFactory<AbstractManager, T>)Objects.requireNonNull(factory, "factory")).createManager(name, data);
        if (abstractManager == null)
          throw new IllegalStateException("ManagerFactory [" + factory + "] unable to create manager for [" + name + "] with data [" + data + "]"); 
        MAP.put(name, abstractManager);
      } else {
        abstractManager.updateData(data);
      } 
      abstractManager.count++;
      return (M)abstractManager;
    } finally {
      LOCK.unlock();
    } 
  }
  
  public void updateData(Object data) {}
  
  public static boolean hasManager(String name) {
    LOCK.lock();
    try {
      return MAP.containsKey(name);
    } finally {
      LOCK.unlock();
    } 
  }
  
  protected static <M extends AbstractManager> M narrow(Class<M> narrowClass, AbstractManager manager) {
    if (narrowClass.isAssignableFrom(manager.getClass()))
      return (M)manager; 
    throw new ConfigurationException("Configuration has multiple incompatible Appenders pointing to the same resource '" + manager
        
        .getName() + "'");
  }
  
  protected static StatusLogger logger() {
    return StatusLogger.getLogger();
  }
  
  protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
    return true;
  }
  
  protected int getCount() {
    return this.count;
  }
  
  public LoggerContext getLoggerContext() {
    return this.loggerContext;
  }
  
  @Deprecated
  public void release() {
    close();
  }
  
  public String getName() {
    return this.name;
  }
  
  public Map<String, String> getContentFormat() {
    return new HashMap<>();
  }
  
  protected void log(Level level, String message, Throwable throwable) {
    Message m = LOGGER.getMessageFactory().newMessage("{} {} {}: {}", new Object[] { getClass().getSimpleName(), getName(), message, throwable });
    LOGGER.log(level, m, throwable);
  }
  
  protected void logDebug(String message, Throwable throwable) {
    log(Level.DEBUG, message, throwable);
  }
  
  protected void logError(String message, Throwable throwable) {
    log(Level.ERROR, message, throwable);
  }
  
  protected void logWarn(String message, Throwable throwable) {
    log(Level.WARN, message, throwable);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\AbstractManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */