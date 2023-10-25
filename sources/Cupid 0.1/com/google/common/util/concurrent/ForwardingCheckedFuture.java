package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public abstract class ForwardingCheckedFuture<V, X extends Exception> extends ForwardingListenableFuture<V> implements CheckedFuture<V, X> {
  public V checkedGet() throws X {
    return delegate().checkedGet();
  }
  
  public V checkedGet(long timeout, TimeUnit unit) throws TimeoutException, X {
    return delegate().checkedGet(timeout, unit);
  }
  
  protected abstract CheckedFuture<V, X> delegate();
  
  @Beta
  public static abstract class SimpleForwardingCheckedFuture<V, X extends Exception> extends ForwardingCheckedFuture<V, X> {
    private final CheckedFuture<V, X> delegate;
    
    protected SimpleForwardingCheckedFuture(CheckedFuture<V, X> delegate) {
      this.delegate = (CheckedFuture<V, X>)Preconditions.checkNotNull(delegate);
    }
    
    protected final CheckedFuture<V, X> delegate() {
      return this.delegate;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\ForwardingCheckedFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */