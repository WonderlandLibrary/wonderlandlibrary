package org.apache.logging.log4j.core;

import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

public abstract class AbstractLogEvent implements LogEvent {
  private static final long serialVersionUID = 1L;
  
  private volatile MutableInstant instant;
  
  public LogEvent toImmutable() {
    return this;
  }
  
  public ReadOnlyStringMap getContextData() {
    return null;
  }
  
  public Map<String, String> getContextMap() {
    return Collections.emptyMap();
  }
  
  public ThreadContext.ContextStack getContextStack() {
    return (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK;
  }
  
  public Level getLevel() {
    return null;
  }
  
  public String getLoggerFqcn() {
    return null;
  }
  
  public String getLoggerName() {
    return null;
  }
  
  public Marker getMarker() {
    return null;
  }
  
  public Message getMessage() {
    return null;
  }
  
  public StackTraceElement getSource() {
    return null;
  }
  
  public long getThreadId() {
    return 0L;
  }
  
  public String getThreadName() {
    return null;
  }
  
  public int getThreadPriority() {
    return 0;
  }
  
  public Throwable getThrown() {
    return null;
  }
  
  public ThrowableProxy getThrownProxy() {
    return null;
  }
  
  public long getTimeMillis() {
    return 0L;
  }
  
  public Instant getInstant() {
    return (Instant)getMutableInstant();
  }
  
  protected final MutableInstant getMutableInstant() {
    if (this.instant == null)
      this.instant = new MutableInstant(); 
    return this.instant;
  }
  
  public boolean isEndOfBatch() {
    return false;
  }
  
  public boolean isIncludeLocation() {
    return false;
  }
  
  public void setEndOfBatch(boolean endOfBatch) {}
  
  public void setIncludeLocation(boolean locationRequired) {}
  
  public long getNanoTime() {
    return 0L;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\AbstractLogEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */