package io.netty.util.concurrent;

import io.netty.util.internal.PlatformDependent;

public final class FailedFuture<V> extends CompleteFuture<V> {
  private final Throwable cause;
  
  public FailedFuture(EventExecutor executor, Throwable cause) {
    super(executor);
    if (cause == null)
      throw new NullPointerException("cause"); 
    this.cause = cause;
  }
  
  public Throwable cause() {
    return this.cause;
  }
  
  public boolean isSuccess() {
    return false;
  }
  
  public Future<V> sync() {
    PlatformDependent.throwException(this.cause);
    return this;
  }
  
  public Future<V> syncUninterruptibly() {
    PlatformDependent.throwException(this.cause);
    return this;
  }
  
  public V getNow() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\FailedFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */