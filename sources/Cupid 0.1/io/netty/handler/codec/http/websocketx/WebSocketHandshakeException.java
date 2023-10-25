package io.netty.handler.codec.http.websocketx;

public class WebSocketHandshakeException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public WebSocketHandshakeException(String s) {
    super(s);
  }
  
  public WebSocketHandshakeException(String s, Throwable throwable) {
    super(s, throwable);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocketHandshakeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */