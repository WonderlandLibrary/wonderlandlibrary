package org.apache.http.impl.execchain;

import java.io.InterruptedIOException;
import org.apache.http.annotation.Immutable;

@Immutable
public class RequestAbortedException extends InterruptedIOException {
  private static final long serialVersionUID = 4973849966012490112L;
  
  public RequestAbortedException(String message) {
    super(message);
  }
  
  public RequestAbortedException(String message, Throwable cause) {
    super(message);
    if (cause != null)
      initCause(cause); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\RequestAbortedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */