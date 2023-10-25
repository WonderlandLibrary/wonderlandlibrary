package com.google.common.util.concurrent;

import javax.annotation.Nullable;

public final class SettableFuture<V> extends AbstractFuture<V> {
  public static <V> SettableFuture<V> create() {
    return new SettableFuture<V>();
  }
  
  public boolean set(@Nullable V value) {
    return super.set(value);
  }
  
  public boolean setException(Throwable throwable) {
    return super.setException(throwable);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\SettableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */