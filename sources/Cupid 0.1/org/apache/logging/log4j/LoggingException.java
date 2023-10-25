package org.apache.logging.log4j;

public class LoggingException extends RuntimeException {
  private static final long serialVersionUID = 6366395965071580537L;
  
  public LoggingException(String message) {
    super(message);
  }
  
  public LoggingException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public LoggingException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\LoggingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */