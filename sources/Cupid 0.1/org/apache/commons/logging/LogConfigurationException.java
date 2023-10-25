package org.apache.commons.logging;

public class LogConfigurationException extends RuntimeException {
  private static final long serialVersionUID = 8486587136871052495L;
  
  public LogConfigurationException() {}
  
  public LogConfigurationException(String message) {
    super(message);
  }
  
  public LogConfigurationException(Throwable cause) {
    this((cause == null) ? null : cause.toString(), cause);
  }
  
  public LogConfigurationException(String message, Throwable cause) {
    super(message + " (Caused by " + cause + ")");
    this.cause = cause;
  }
  
  protected Throwable cause = null;
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\logging\LogConfigurationException.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */