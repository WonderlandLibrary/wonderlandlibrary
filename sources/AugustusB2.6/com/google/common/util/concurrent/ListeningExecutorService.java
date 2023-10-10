// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.DoNotMock;
import java.util.concurrent.ExecutorService;

@DoNotMock("Use TestingExecutors.sameThreadScheduledExecutor, or wrap a real Executor from java.util.concurrent.Executors with MoreExecutors.listeningDecorator")
@ElementTypesAreNonnullByDefault
@GwtIncompatible
public interface ListeningExecutorService extends ExecutorService
{
     <T> ListenableFuture<T> submit(final Callable<T> p0);
    
    ListenableFuture<?> submit(final Runnable p0);
    
     <T> ListenableFuture<T> submit(final Runnable p0, @ParametricNullness final T p1);
    
     <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> p0) throws InterruptedException;
    
     <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> p0, final long p1, final TimeUnit p2) throws InterruptedException;
}
