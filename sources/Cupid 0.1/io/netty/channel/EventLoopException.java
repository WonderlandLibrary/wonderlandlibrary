package io.netty.channel;

public class EventLoopException extends ChannelException {
  private static final long serialVersionUID = -8969100344583703616L;
  
  public EventLoopException() {}
  
  public EventLoopException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public EventLoopException(String message) {
    super(message);
  }
  
  public EventLoopException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\EventLoopException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */