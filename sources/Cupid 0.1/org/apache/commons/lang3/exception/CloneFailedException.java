package org.apache.commons.lang3.exception;

public class CloneFailedException extends RuntimeException {
  private static final long serialVersionUID = 20091223L;
  
  public CloneFailedException(String message) {
    super(message);
  }
  
  public CloneFailedException(Throwable cause) {
    super(cause);
  }
  
  public CloneFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\exception\CloneFailedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */