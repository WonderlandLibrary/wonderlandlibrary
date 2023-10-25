package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.config.Property;

public abstract class AbstractWriterAppender<M extends WriterManager> extends AbstractAppender {
  protected final boolean immediateFlush;
  
  private final M manager;
  
  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  
  private final Lock readLock = this.readWriteLock.readLock();
  
  protected AbstractWriterAppender(String name, StringLayout layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, Property[] properties, M manager) {
    super(name, filter, (Layout<? extends Serializable>)layout, ignoreExceptions, properties);
    this.manager = manager;
    this.immediateFlush = immediateFlush;
  }
  
  @Deprecated
  protected AbstractWriterAppender(String name, StringLayout layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, M manager) {
    super(name, filter, (Layout<? extends Serializable>)layout, ignoreExceptions, Property.EMPTY_ARRAY);
    this.manager = manager;
    this.immediateFlush = immediateFlush;
  }
  
  public void append(LogEvent event) {
    this.readLock.lock();
    try {
      String str = (String)getStringLayout().toSerializable(event);
      if (str.length() > 0) {
        this.manager.write(str);
        if (this.immediateFlush || event.isEndOfBatch())
          this.manager.flush(); 
      } 
    } catch (AppenderLoggingException ex) {
      error("Unable to write " + this.manager.getName() + " for appender " + getName(), event, (Throwable)ex);
      throw ex;
    } finally {
      this.readLock.unlock();
    } 
  }
  
  public M getManager() {
    return this.manager;
  }
  
  public StringLayout getStringLayout() {
    return (StringLayout)getLayout();
  }
  
  public void start() {
    if (getLayout() == null)
      LOGGER.error("No layout set for the appender named [{}].", getName()); 
    if (this.manager == null)
      LOGGER.error("No OutputStreamManager set for the appender named [{}].", getName()); 
    super.start();
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    boolean stopped = stop(timeout, timeUnit, false);
    stopped &= this.manager.stop(timeout, timeUnit);
    setStopped();
    return stopped;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\AbstractWriterAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */