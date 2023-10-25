package io.netty.util.concurrent;

public interface Promise<V> extends Future<V> {
  Promise<V> setSuccess(V paramV);
  
  boolean trySuccess(V paramV);
  
  Promise<V> setFailure(Throwable paramThrowable);
  
  boolean tryFailure(Throwable paramThrowable);
  
  boolean setUncancellable();
  
  Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);
  
  Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);
  
  Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);
  
  Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);
  
  Promise<V> await() throws InterruptedException;
  
  Promise<V> awaitUninterruptibly();
  
  Promise<V> sync() throws InterruptedException;
  
  Promise<V> syncUninterruptibly();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\Promise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */