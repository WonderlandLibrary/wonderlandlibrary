package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import javax.annotation.Nullable;

final class AsyncSettableFuture<V> extends ForwardingListenableFuture<V> {
  public static <V> AsyncSettableFuture<V> create() {
    return new AsyncSettableFuture<V>();
  }
  
  private final NestedFuture<V> nested = new NestedFuture<V>();
  
  private final ListenableFuture<V> dereferenced = Futures.dereference(this.nested);
  
  protected ListenableFuture<V> delegate() {
    return this.dereferenced;
  }
  
  public boolean setFuture(ListenableFuture<? extends V> future) {
    return this.nested.setFuture((ListenableFuture<? extends V>)Preconditions.checkNotNull(future));
  }
  
  public boolean setValue(@Nullable V value) {
    return setFuture(Futures.immediateFuture(value));
  }
  
  public boolean setException(Throwable exception) {
    return setFuture(Futures.immediateFailedFuture(exception));
  }
  
  public boolean isSet() {
    return this.nested.isDone();
  }
  
  private static final class NestedFuture<V> extends AbstractFuture<ListenableFuture<? extends V>> {
    private NestedFuture() {}
    
    boolean setFuture(ListenableFuture<? extends V> value) {
      boolean result = set(value);
      if (isCancelled())
        value.cancel(wasInterrupted()); 
      return result;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\AsyncSettableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */