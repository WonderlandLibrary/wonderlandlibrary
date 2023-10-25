package org.apache.http.conn;

import org.apache.http.annotation.Immutable;

@Immutable
public class ConnectionPoolTimeoutException extends ConnectTimeoutException {
  private static final long serialVersionUID = -7898874842020245128L;
  
  public ConnectionPoolTimeoutException() {}
  
  public ConnectionPoolTimeoutException(String message) {
    super(message);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ConnectionPoolTimeoutException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */