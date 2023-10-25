package io.netty.util.concurrent;

public final class SucceededFuture<V> extends CompleteFuture<V> {
  private final V result;
  
  public SucceededFuture(EventExecutor executor, V result) {
    super(executor);
    this.result = result;
  }
  
  public Throwable cause() {
    return null;
  }
  
  public boolean isSuccess() {
    return true;
  }
  
  public V getNow() {
    return this.result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\SucceededFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */