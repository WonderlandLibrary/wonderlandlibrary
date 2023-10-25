package org.apache.http.cookie;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class MalformedCookieException extends ProtocolException {
  private static final long serialVersionUID = -6695462944287282185L;
  
  public MalformedCookieException() {}
  
  public MalformedCookieException(String message) {
    super(message);
  }
  
  public MalformedCookieException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\cookie\MalformedCookieException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */