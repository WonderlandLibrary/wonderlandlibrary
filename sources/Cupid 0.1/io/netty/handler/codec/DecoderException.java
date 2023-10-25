package io.netty.handler.codec;

public class DecoderException extends CodecException {
  private static final long serialVersionUID = 6926716840699621852L;
  
  public DecoderException() {}
  
  public DecoderException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public DecoderException(String message) {
    super(message);
  }
  
  public DecoderException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\DecoderException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */