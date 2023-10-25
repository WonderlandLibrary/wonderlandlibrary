package io.netty.handler.codec.spdy;

public class SpdyProtocolException extends Exception {
  private static final long serialVersionUID = 7870000537743847264L;
  
  public SpdyProtocolException() {}
  
  public SpdyProtocolException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public SpdyProtocolException(String message) {
    super(message);
  }
  
  public SpdyProtocolException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\SpdyProtocolException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */