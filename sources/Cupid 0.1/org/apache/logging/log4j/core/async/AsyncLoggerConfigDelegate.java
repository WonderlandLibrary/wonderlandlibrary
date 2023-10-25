package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;

public interface AsyncLoggerConfigDelegate {
  RingBufferAdmin createRingBufferAdmin(String paramString1, String paramString2);
  
  EventRoute getEventRoute(Level paramLevel);
  
  void enqueueEvent(LogEvent paramLogEvent, AsyncLoggerConfig paramAsyncLoggerConfig);
  
  boolean tryEnqueue(LogEvent paramLogEvent, AsyncLoggerConfig paramAsyncLoggerConfig);
  
  void setLogEventFactory(LogEventFactory paramLogEventFactory);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfigDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */