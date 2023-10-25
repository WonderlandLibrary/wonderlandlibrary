package org.apache.commons.lang3;

public class SerializationException extends RuntimeException {
  private static final long serialVersionUID = 4029025366392702726L;
  
  public SerializationException() {}
  
  public SerializationException(String msg) {
    super(msg);
  }
  
  public SerializationException(Throwable cause) {
    super(cause);
  }
  
  public SerializationException(String msg, Throwable cause) {
    super(msg, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\SerializationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */