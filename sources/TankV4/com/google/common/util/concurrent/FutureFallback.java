package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

@Beta
public interface FutureFallback {
   ListenableFuture create(Throwable var1) throws Exception;
}
