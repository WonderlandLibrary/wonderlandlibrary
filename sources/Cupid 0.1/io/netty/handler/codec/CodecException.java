package io.netty.handler.codec;

public class CodecException extends RuntimeException {
  private static final long serialVersionUID = -1464830400709348473L;
  
  public CodecException() {}
  
  public CodecException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public CodecException(String message) {
    super(message);
  }
  
  public CodecException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\CodecException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */