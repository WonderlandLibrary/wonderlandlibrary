package io.netty.handler.codec.compression;

import io.netty.handler.codec.EncoderException;

public class CompressionException extends EncoderException {
  private static final long serialVersionUID = 5603413481274811897L;
  
  public CompressionException() {}
  
  public CompressionException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public CompressionException(String message) {
    super(message);
  }
  
  public CompressionException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\compression\CompressionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */