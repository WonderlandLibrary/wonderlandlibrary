package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.LoggingException;

public class AppenderLoggingException extends LoggingException {
  private static final long serialVersionUID = 6545990597472958303L;
  
  public AppenderLoggingException(String message) {
    super(message);
  }
  
  public AppenderLoggingException(String format, Object... args) {
    super(String.format(format, args));
  }
  
  public AppenderLoggingException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public AppenderLoggingException(Throwable cause) {
    super(cause);
  }
  
  public AppenderLoggingException(Throwable cause, String format, Object... args) {
    super(String.format(format, args), cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\AppenderLoggingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */