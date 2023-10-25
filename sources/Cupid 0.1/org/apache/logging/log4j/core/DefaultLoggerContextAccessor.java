package org.apache.logging.log4j.core;

public class DefaultLoggerContextAccessor implements LoggerContextAccessor {
  public static DefaultLoggerContextAccessor INSTANCE = new DefaultLoggerContextAccessor();
  
  public LoggerContext getLoggerContext() {
    return LoggerContext.getContext();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\DefaultLoggerContextAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */