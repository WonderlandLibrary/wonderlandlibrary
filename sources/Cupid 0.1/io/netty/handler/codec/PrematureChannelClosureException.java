package io.netty.handler.codec;

public class PrematureChannelClosureException extends CodecException {
  private static final long serialVersionUID = 4907642202594703094L;
  
  public PrematureChannelClosureException() {}
  
  public PrematureChannelClosureException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public PrematureChannelClosureException(String message) {
    super(message);
  }
  
  public PrematureChannelClosureException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\PrematureChannelClosureException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */