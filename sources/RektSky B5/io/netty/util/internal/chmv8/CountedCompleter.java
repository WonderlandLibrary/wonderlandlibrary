/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.chmv8;

import io.netty.util.internal.chmv8.ForkJoinTask;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

public abstract class CountedCompleter<T>
extends ForkJoinTask<T> {
    private static final long serialVersionUID = 5232453752276485070L;
    final CountedCompleter<?> completer;
    volatile int pending;
    private static final Unsafe U;
    private static final long PENDING;

    protected CountedCompleter(CountedCompleter<?> completer, int initialPendingCount) {
        this.completer = completer;
        this.pending = initialPendingCount;
    }

    protected CountedCompleter(CountedCompleter<?> completer) {
        this.completer = completer;
    }

    protected CountedCompleter() {
        this.completer = null;
    }

    public abstract void compute();

    public void onCompletion(CountedCompleter<?> caller) {
    }

    public boolean onExceptionalCompletion(Throwable ex, CountedCompleter<?> caller) {
        return true;
    }

    public final CountedCompleter<?> getCompleter() {
        return this.completer;
    }

    public final int getPendingCount() {
        return this.pending;
    }

    public final void setPendingCount(int count) {
        this.pending = count;
    }

    public final void addToPendingCount(int delta) {
        int c2;
        while (!U.compareAndSwapInt(this, PENDING, c2 = this.pending, c2 + delta)) {
        }
    }

    public final boolean compareAndSetPendingCount(int expected, int count) {
        return U.compareAndSwapInt(this, PENDING, expected, count);
    }

    public final int decrementPendingCountUnlessZero() {
        int c2;
        while ((c2 = this.pending) != 0 && !U.compareAndSwapInt(this, PENDING, c2, c2 - 1)) {
        }
        return c2;
    }

    public final CountedCompleter<?> getRoot() {
        CountedCompleter<?> p2;
        CountedCompleter<?> a2 = this;
        while ((p2 = a2.completer) != null) {
            a2 = p2;
        }
        return a2;
    }

    public final void tryComplete() {
        CountedCompleter<?> a2;
        CountedCompleter<?> s2 = a2 = this;
        while (true) {
            int c2;
            if ((c2 = a2.pending) == 0) {
                a2.onCompletion(s2);
                s2 = a2;
                a2 = s2.completer;
                if (a2 != null) continue;
                s2.quietlyComplete();
                return;
            }
            if (U.compareAndSwapInt(a2, PENDING, c2, c2 - 1)) break;
        }
    }

    public final void propagateCompletion() {
        CountedCompleter<?> a2;
        CountedCompleter<?> s2 = a2 = this;
        while (true) {
            int c2;
            if ((c2 = a2.pending) == 0) {
                s2 = a2;
                a2 = s2.completer;
                if (a2 != null) continue;
                s2.quietlyComplete();
                return;
            }
            if (U.compareAndSwapInt(a2, PENDING, c2, c2 - 1)) break;
        }
    }

    @Override
    public void complete(T rawResult) {
        this.setRawResult(rawResult);
        this.onCompletion(this);
        this.quietlyComplete();
        CountedCompleter<?> p2 = this.completer;
        if (p2 != null) {
            p2.tryComplete();
        }
    }

    public final CountedCompleter<?> firstComplete() {
        int c2;
        do {
            if ((c2 = this.pending) != 0) continue;
            return this;
        } while (!U.compareAndSwapInt(this, PENDING, c2, c2 - 1));
        return null;
    }

    public final CountedCompleter<?> nextComplete() {
        CountedCompleter<?> p2 = this.completer;
        if (p2 != null) {
            return p2.firstComplete();
        }
        this.quietlyComplete();
        return null;
    }

    public final void quietlyCompleteRoot() {
        CountedCompleter<?> a2 = this;
        while (true) {
            CountedCompleter<?> p2;
            if ((p2 = a2.completer) == null) {
                a2.quietlyComplete();
                return;
            }
            a2 = p2;
        }
    }

    @Override
    void internalPropagateException(Throwable ex) {
        CountedCompleter<?> a2;
        CountedCompleter<?> s2 = a2 = this;
        while (a2.onExceptionalCompletion(ex, s2)) {
            s2 = a2;
            a2 = s2.completer;
            if (a2 != null && a2.status >= 0 && a2.recordExceptionalCompletion(ex) == Integer.MIN_VALUE) continue;
        }
    }

    @Override
    protected final boolean exec() {
        this.compute();
        return false;
    }

    @Override
    public T getRawResult() {
        return null;
    }

    @Override
    protected void setRawResult(T t2) {
    }

    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException tryReflectionInstead) {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>(){

                    @Override
                    public Unsafe run() throws Exception {
                        Class<Unsafe> k2 = Unsafe.class;
                        for (Field f2 : k2.getDeclaredFields()) {
                            f2.setAccessible(true);
                            Object x2 = f2.get(null);
                            if (!k2.isInstance(x2)) continue;
                            return (Unsafe)k2.cast(x2);
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException e2) {
                throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
            }
        }
    }

    static {
        try {
            U = CountedCompleter.getUnsafe();
            PENDING = U.objectFieldOffset(CountedCompleter.class.getDeclaredField("pending"));
        }
        catch (Exception e2) {
            throw new Error(e2);
        }
    }
}

