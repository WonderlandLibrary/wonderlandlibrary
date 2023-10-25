package io.netty.util.concurrent;

public class BlockingOperationException extends IllegalStateException {
  private static final long serialVersionUID = 2462223247762460301L;
  
  public BlockingOperationException() {}
  
  public BlockingOperationException(String s) {
    super(s);
  }
  
  public BlockingOperationException(Throwable cause) {
    super(cause);
  }
  
  public BlockingOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\BlockingOperationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */