package org.apache.logging.log4j.spi;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.LoaderUtil;

public abstract class AbstractLoggerAdapter<L> implements LoggerAdapter<L>, LoggerContextShutdownAware {
  protected final Map<LoggerContext, ConcurrentMap<String, L>> registry = new ConcurrentHashMap<>();
  
  private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
  
  public L getLogger(String name) {
    LoggerContext context = getContext();
    ConcurrentMap<String, L> loggers = getLoggersInContext(context);
    L logger = loggers.get(name);
    if (logger != null)
      return logger; 
    loggers.putIfAbsent(name, newLogger(name, context));
    return loggers.get(name);
  }
  
  public void contextShutdown(LoggerContext loggerContext) {
    this.registry.remove(loggerContext);
  }
  
  public ConcurrentMap<String, L> getLoggersInContext(LoggerContext context) {
    ConcurrentMap<String, L> loggers;
    this.lock.readLock().lock();
    try {
      loggers = this.registry.get(context);
    } finally {
      this.lock.readLock().unlock();
    } 
    if (loggers != null)
      return loggers; 
    this.lock.writeLock().lock();
    try {
      loggers = this.registry.get(context);
      if (loggers == null) {
        loggers = new ConcurrentHashMap<>();
        this.registry.put(context, loggers);
        if (context instanceof LoggerContextShutdownEnabled)
          ((LoggerContextShutdownEnabled)context).addShutdownListener(this); 
      } 
      return loggers;
    } finally {
      this.lock.writeLock().unlock();
    } 
  }
  
  public Set<LoggerContext> getLoggerContexts() {
    return new HashSet<>(this.registry.keySet());
  }
  
  protected abstract L newLogger(String paramString, LoggerContext paramLoggerContext);
  
  protected abstract LoggerContext getContext();
  
  protected LoggerContext getContext(Class<?> callerClass) {
    ClassLoader cl = null;
    if (callerClass != null)
      cl = callerClass.getClassLoader(); 
    if (cl == null)
      cl = LoaderUtil.getThreadContextClassLoader(); 
    return LogManager.getContext(cl, false);
  }
  
  public void close() {
    this.lock.writeLock().lock();
    try {
      this.registry.clear();
    } finally {
      this.lock.writeLock().unlock();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\AbstractLoggerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */