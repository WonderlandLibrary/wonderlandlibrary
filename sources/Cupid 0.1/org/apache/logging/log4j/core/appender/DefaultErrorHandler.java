package org.apache.logging.log4j.core.appender;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;

public class DefaultErrorHandler implements ErrorHandler {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final int MAX_EXCEPTION_COUNT = 3;
  
  private static final long EXCEPTION_INTERVAL_NANOS = TimeUnit.MINUTES.toNanos(5L);
  
  private int exceptionCount = 0;
  
  private long lastExceptionInstantNanos = System.nanoTime() - EXCEPTION_INTERVAL_NANOS - 1L;
  
  private final Appender appender;
  
  public DefaultErrorHandler(Appender appender) {
    this.appender = Objects.<Appender>requireNonNull(appender, "appender");
  }
  
  public void error(String msg) {
    boolean allowed = acquirePermit();
    if (allowed)
      LOGGER.error(msg); 
  }
  
  public void error(String msg, Throwable error) {
    boolean allowed = acquirePermit();
    if (allowed)
      LOGGER.error(msg, error); 
    if (!this.appender.ignoreExceptions() && error != null && !(error instanceof AppenderLoggingException))
      throw new AppenderLoggingException(msg, error); 
  }
  
  public void error(String msg, LogEvent event, Throwable error) {
    boolean allowed = acquirePermit();
    if (allowed)
      LOGGER.error(msg, error); 
    if (!this.appender.ignoreExceptions() && error != null && !(error instanceof AppenderLoggingException))
      throw new AppenderLoggingException(msg, error); 
  }
  
  private boolean acquirePermit() {
    long currentInstantNanos = System.nanoTime();
    synchronized (this) {
      if (currentInstantNanos - this.lastExceptionInstantNanos > EXCEPTION_INTERVAL_NANOS) {
        this.lastExceptionInstantNanos = currentInstantNanos;
        return true;
      } 
      if (this.exceptionCount < 3) {
        this.exceptionCount++;
        this.lastExceptionInstantNanos = currentInstantNanos;
        return true;
      } 
      return false;
    } 
  }
  
  public Appender getAppender() {
    return this.appender;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\DefaultErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */