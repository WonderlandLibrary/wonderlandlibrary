package io.netty.handler.ssl;

public final class SslHandshakeCompletionEvent {
  public static final SslHandshakeCompletionEvent SUCCESS = new SslHandshakeCompletionEvent();
  
  private final Throwable cause;
  
  private SslHandshakeCompletionEvent() {
    this.cause = null;
  }
  
  public SslHandshakeCompletionEvent(Throwable cause) {
    if (cause == null)
      throw new NullPointerException("cause"); 
    this.cause = cause;
  }
  
  public boolean isSuccess() {
    return (this.cause == null);
  }
  
  public Throwable cause() {
    return this.cause;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ssl\SslHandshakeCompletionEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */