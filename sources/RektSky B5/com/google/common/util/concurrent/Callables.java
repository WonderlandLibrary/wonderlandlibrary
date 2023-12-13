/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public final class Callables {
    private Callables() {
    }

    public static <T> Callable<T> returning(final @Nullable T value) {
        return new Callable<T>(){

            @Override
            public T call() {
                return value;
            }
        };
    }

    static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(callable);
        return new Callable<T>(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public T call() throws Exception {
                Thread currentThread = Thread.currentThread();
                String oldName = currentThread.getName();
                boolean restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
                try {
                    Object v2 = callable.call();
                    return v2;
                }
                finally {
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }

    static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(task);
        return new Runnable(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                Thread currentThread = Thread.currentThread();
                String oldName = currentThread.getName();
                boolean restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
                try {
                    task.run();
                }
                finally {
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }

    private static boolean trySetName(String threadName, Thread currentThread) {
        try {
            currentThread.setName(threadName);
            return true;
        }
        catch (SecurityException e2) {
            return false;
        }
    }
}

