package io.netty.handler.ssl;

import javax.net.ssl.SSLException;

public class NotSslRecordException extends SSLException {
  private static final long serialVersionUID = -4316784434770656841L;
  
  public NotSslRecordException() {
    super("");
  }
  
  public NotSslRecordException(String message) {
    super(message);
  }
  
  public NotSslRecordException(Throwable cause) {
    super(cause);
  }
  
  public NotSslRecordException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ssl\NotSslRecordException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */