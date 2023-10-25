package org.apache.logging.log4j.core.appender.db;

import java.io.Flushable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public abstract class AbstractDatabaseManager extends AbstractManager implements Flushable {
  private final ArrayList<LogEvent> buffer;
  
  private final int bufferSize;
  
  private final Layout<? extends Serializable> layout;
  
  private boolean running;
  
  protected static abstract class AbstractFactoryData {
    private final int bufferSize;
    
    private final Layout<? extends Serializable> layout;
    
    protected AbstractFactoryData(int bufferSize, Layout<? extends Serializable> layout) {
      this.bufferSize = bufferSize;
      this.layout = layout;
    }
    
    public int getBufferSize() {
      return this.bufferSize;
    }
    
    public Layout<? extends Serializable> getLayout() {
      return this.layout;
    }
  }
  
  protected static <M extends AbstractDatabaseManager, T extends AbstractFactoryData> M getManager(String name, T data, ManagerFactory<M, T> factory) {
    return (M)AbstractManager.getManager(name, factory, data);
  }
  
  protected AbstractDatabaseManager(String name, int bufferSize) {
    this(name, bufferSize, (Layout<? extends Serializable>)null);
  }
  
  protected AbstractDatabaseManager(String name, int bufferSize, Layout<? extends Serializable> layout) {
    super(null, name);
    this.bufferSize = bufferSize;
    this.buffer = new ArrayList<>(bufferSize + 1);
    this.layout = layout;
  }
  
  protected void buffer(LogEvent event) {
    this.buffer.add(event.toImmutable());
    if (this.buffer.size() >= this.bufferSize || event.isEndOfBatch())
      flush(); 
  }
  
  protected abstract boolean commitAndClose();
  
  protected abstract void connectAndStart();
  
  public final synchronized void flush() {
    if (isRunning() && isBuffered()) {
      connectAndStart();
      try {
        for (LogEvent event : this.buffer)
          writeInternal(event, (this.layout != null) ? this.layout.toSerializable(event) : null); 
      } finally {
        commitAndClose();
        this.buffer.clear();
      } 
    } 
  }
  
  protected boolean isBuffered() {
    return (this.bufferSize > 0);
  }
  
  public final boolean isRunning() {
    return this.running;
  }
  
  public final boolean releaseSub(long timeout, TimeUnit timeUnit) {
    return shutdown();
  }
  
  public final synchronized boolean shutdown() {
    boolean closed = true;
    flush();
    if (isRunning())
      try {
        closed &= shutdownInternal();
      } catch (Exception e) {
        logWarn("Caught exception while performing database shutdown operations", e);
        closed = false;
      } finally {
        this.running = false;
      }  
    return closed;
  }
  
  protected abstract boolean shutdownInternal() throws Exception;
  
  public final synchronized void startup() {
    if (!isRunning())
      try {
        startupInternal();
        this.running = true;
      } catch (Exception e) {
        logError("Could not perform database startup operations", e);
      }  
  }
  
  protected abstract void startupInternal() throws Exception;
  
  public final String toString() {
    return getName();
  }
  
  @Deprecated
  public final synchronized void write(LogEvent event) {
    write(event, (Serializable)null);
  }
  
  public final synchronized void write(LogEvent event, Serializable serializable) {
    if (isBuffered()) {
      buffer(event);
    } else {
      writeThrough(event, serializable);
    } 
  }
  
  @Deprecated
  protected void writeInternal(LogEvent event) {
    writeInternal(event, (Serializable)null);
  }
  
  protected abstract void writeInternal(LogEvent paramLogEvent, Serializable paramSerializable);
  
  protected void writeThrough(LogEvent event, Serializable serializable) {
    connectAndStart();
    try {
      writeInternal(event, serializable);
    } finally {
      commitAndClose();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\db\AbstractDatabaseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */