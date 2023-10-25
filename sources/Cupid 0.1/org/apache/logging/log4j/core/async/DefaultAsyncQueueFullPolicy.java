package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;

public class DefaultAsyncQueueFullPolicy implements AsyncQueueFullPolicy {
  public EventRoute getRoute(long backgroundThreadId, Level level) {
    Thread currentThread = Thread.currentThread();
    if (currentThread.getId() == backgroundThreadId || currentThread instanceof org.apache.logging.log4j.core.util.Log4jThread)
      return EventRoute.SYNCHRONOUS; 
    return EventRoute.ENQUEUE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\DefaultAsyncQueueFullPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */