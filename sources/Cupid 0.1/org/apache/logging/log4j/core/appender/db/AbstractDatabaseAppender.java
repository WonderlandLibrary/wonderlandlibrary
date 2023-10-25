package org.apache.logging.log4j.core.appender.db;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.Property;

public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager> extends AbstractAppender {
  public static final int DEFAULT_RECONNECT_INTERVAL_MILLIS = 5000;
  
  public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> {}
  
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  
  private final Lock readLock = this.lock.readLock();
  
  private final Lock writeLock = this.lock.writeLock();
  
  private T manager;
  
  @Deprecated
  protected AbstractDatabaseAppender(String name, Filter filter, boolean ignoreExceptions, T manager) {
    super(name, filter, null, ignoreExceptions, Property.EMPTY_ARRAY);
    this.manager = manager;
  }
  
  protected AbstractDatabaseAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, T manager) {
    super(name, filter, layout, ignoreExceptions, properties);
    this.manager = manager;
  }
  
  @Deprecated
  protected AbstractDatabaseAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, T manager) {
    super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
    this.manager = manager;
  }
  
  public final void append(LogEvent event) {
    this.readLock.lock();
    try {
      getManager().write(event, toSerializable(event));
    } catch (LoggingException e) {
      LOGGER.error("Unable to write to database [{}] for appender [{}].", getManager().getName(), 
          getName(), e);
      throw e;
    } catch (Exception e) {
      LOGGER.error("Unable to write to database [{}] for appender [{}].", getManager().getName(), 
          getName(), e);
      throw new AppenderLoggingException("Unable to write to database in appender: " + e.getMessage(), e);
    } finally {
      this.readLock.unlock();
    } 
  }
  
  public final Layout<LogEvent> getLayout() {
    return null;
  }
  
  public final T getManager() {
    return this.manager;
  }
  
  protected final void replaceManager(T manager) {
    this.writeLock.lock();
    try {
      T old = getManager();
      if (!manager.isRunning())
        manager.startup(); 
      this.manager = manager;
      old.close();
    } finally {
      this.writeLock.unlock();
    } 
  }
  
  public final void start() {
    if (getManager() == null)
      LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", getName()); 
    super.start();
    if (getManager() != null)
      getManager().startup(); 
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    boolean stopped = stop(timeout, timeUnit, false);
    if (getManager() != null)
      stopped &= getManager().stop(timeout, timeUnit); 
    setStopped();
    return stopped;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\db\AbstractDatabaseAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */