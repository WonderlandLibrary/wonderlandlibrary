package io.netty.handler.codec.compression;

import io.netty.handler.codec.DecoderException;

public class DecompressionException extends DecoderException {
  private static final long serialVersionUID = 3546272712208105199L;
  
  public DecompressionException() {}
  
  public DecompressionException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public DecompressionException(String message) {
    super(message);
  }
  
  public DecompressionException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\compression\DecompressionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */