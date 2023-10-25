package org.apache.http;

import java.io.IOException;

public class ConnectionClosedException extends IOException {
  private static final long serialVersionUID = 617550366255636674L;
  
  public ConnectionClosedException(String message) {
    super(message);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\ConnectionClosedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */