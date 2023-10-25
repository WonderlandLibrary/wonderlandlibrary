package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

@Beta
public interface FutureFallback<V> {
  ListenableFuture<V> create(Throwable paramThrowable) throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\FutureFallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */