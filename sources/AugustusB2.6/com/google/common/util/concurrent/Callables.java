// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Callables
{
    private Callables() {
    }
    
    public static <T> Callable<T> returning(@ParametricNullness final T value) {
        return new Callable<T>() {
            @ParametricNullness
            @Override
            public T call() {
                return value;
            }
        };
    }
    
    @Beta
    @GwtIncompatible
    public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> callable, final ListeningExecutorService listeningExecutorService) {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(listeningExecutorService);
        return new AsyncCallable<T>() {
            @Override
            public ListenableFuture<T> call() throws Exception {
                return listeningExecutorService.submit((Callable<T>)callable);
            }
        };
    }
    
    @GwtIncompatible
    static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(callable);
        return new Callable<T>() {
            @ParametricNullness
            @Override
            public T call() throws Exception {
                final Thread currentThread = Thread.currentThread();
                final String oldName = currentThread.getName();
                final boolean restoreName = trySetName(nameSupplier.get(), currentThread);
                try {
                    return callable.call();
                }
                finally {
                    if (restoreName) {
                        trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }
    
    @GwtIncompatible
    static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(task);
        return new Runnable() {
            @Override
            public void run() {
                final Thread currentThread = Thread.currentThread();
                final String oldName = currentThread.getName();
                final boolean restoreName = trySetName(nameSupplier.get(), currentThread);
                try {
                    task.run();
                }
                finally {
                    if (restoreName) {
                        trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }
    
    @GwtIncompatible
    private static boolean trySetName(final String threadName, final Thread currentThread) {
        try {
            currentThread.setName(threadName);
            return true;
        }
        catch (SecurityException e) {
            return false;
        }
    }
}
