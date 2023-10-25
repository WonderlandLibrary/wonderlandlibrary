package io.netty.util.concurrent;

public interface ProgressiveFuture<V> extends Future<V> {
  ProgressiveFuture<V> addListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);
  
  ProgressiveFuture<V> addListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);
  
  ProgressiveFuture<V> removeListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);
  
  ProgressiveFuture<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);
  
  ProgressiveFuture<V> sync() throws InterruptedException;
  
  ProgressiveFuture<V> syncUninterruptibly();
  
  ProgressiveFuture<V> await() throws InterruptedException;
  
  ProgressiveFuture<V> awaitUninterruptibly();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\ProgressiveFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */