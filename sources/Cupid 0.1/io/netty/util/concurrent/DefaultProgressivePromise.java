package io.netty.util.concurrent;

public class DefaultProgressivePromise<V> extends DefaultPromise<V> implements ProgressivePromise<V> {
  public DefaultProgressivePromise(EventExecutor executor) {
    super(executor);
  }
  
  protected DefaultProgressivePromise() {}
  
  public ProgressivePromise<V> setProgress(long progress, long total) {
    if (total < 0L) {
      total = -1L;
      if (progress < 0L)
        throw new IllegalArgumentException("progress: " + progress + " (expected: >= 0)"); 
    } else if (progress < 0L || progress > total) {
      throw new IllegalArgumentException("progress: " + progress + " (expected: 0 <= progress <= total (" + total + "))");
    } 
    if (isDone())
      throw new IllegalStateException("complete already"); 
    notifyProgressiveListeners(progress, total);
    return this;
  }
  
  public boolean tryProgress(long progress, long total) {
    if (total < 0L) {
      total = -1L;
      if (progress < 0L || isDone())
        return false; 
    } else if (progress < 0L || progress > total || isDone()) {
      return false;
    } 
    notifyProgressiveListeners(progress, total);
    return true;
  }
  
  public ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
    super.addListener(listener);
    return this;
  }
  
  public ProgressivePromise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
    super.addListeners(listeners);
    return this;
  }
  
  public ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {
    super.removeListener(listener);
    return this;
  }
  
  public ProgressivePromise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
    super.removeListeners(listeners);
    return this;
  }
  
  public ProgressivePromise<V> sync() throws InterruptedException {
    super.sync();
    return this;
  }
  
  public ProgressivePromise<V> syncUninterruptibly() {
    super.syncUninterruptibly();
    return this;
  }
  
  public ProgressivePromise<V> await() throws InterruptedException {
    super.await();
    return this;
  }
  
  public ProgressivePromise<V> awaitUninterruptibly() {
    super.awaitUninterruptibly();
    return this;
  }
  
  public ProgressivePromise<V> setSuccess(V result) {
    super.setSuccess(result);
    return this;
  }
  
  public ProgressivePromise<V> setFailure(Throwable cause) {
    super.setFailure(cause);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\DefaultProgressivePromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */