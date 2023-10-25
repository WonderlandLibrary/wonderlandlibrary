package org.apache.http.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class CircularRedirectException extends RedirectException {
  private static final long serialVersionUID = 6830063487001091803L;
  
  public CircularRedirectException() {}
  
  public CircularRedirectException(String message) {
    super(message);
  }
  
  public CircularRedirectException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\CircularRedirectException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */