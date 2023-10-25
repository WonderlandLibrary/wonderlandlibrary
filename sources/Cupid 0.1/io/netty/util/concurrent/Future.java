package io.netty.util.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface Future<V> extends Future<V> {
  boolean isSuccess();
  
  boolean isCancellable();
  
  Throwable cause();
  
  Future<V> addListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);
  
  Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);
  
  Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);
  
  Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);
  
  Future<V> sync() throws InterruptedException;
  
  Future<V> syncUninterruptibly();
  
  Future<V> await() throws InterruptedException;
  
  Future<V> awaitUninterruptibly();
  
  boolean await(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException;
  
  boolean await(long paramLong) throws InterruptedException;
  
  boolean awaitUninterruptibly(long paramLong, TimeUnit paramTimeUnit);
  
  boolean awaitUninterruptibly(long paramLong);
  
  V getNow();
  
  boolean cancel(boolean paramBoolean);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\Future.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */