package org.apache.http;

public class ProtocolException extends HttpException {
  private static final long serialVersionUID = -2143571074341228994L;
  
  public ProtocolException() {}
  
  public ProtocolException(String message) {
    super(message);
  }
  
  public ProtocolException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\ProtocolException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */