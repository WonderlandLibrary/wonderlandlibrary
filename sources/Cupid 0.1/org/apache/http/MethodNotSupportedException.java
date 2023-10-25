package org.apache.http;

public class MethodNotSupportedException extends HttpException {
  private static final long serialVersionUID = 3365359036840171201L;
  
  public MethodNotSupportedException(String message) {
    super(message);
  }
  
  public MethodNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\MethodNotSupportedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */