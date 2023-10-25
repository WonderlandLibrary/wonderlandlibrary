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
    volatile int pending;
    final CountedCompleter<?> completer;
    private static final long PENDING;
    private static final Unsafe U;

    static {
        try {
            U = CountedCompleter.getUnsafe();
            PENDING = U.objectFieldOffset(CountedCompleter.class.getDeclaredField("pending"));
        }
        catch (Exception exception) {
            throw new Error(exception);
        }
    }

    protected CountedCompleter() {
        this.completer = null;
    }

    @Override
    public void complete(T t) {
        this.setRawResult(t);
        this.onCompletion(this);
        this.quietlyComplete();
        CountedCompleter<?> countedCompleter = this.completer;
        if (countedCompleter != null) {
            countedCompleter.tryComplete();
        }
    }

    public final void propagateCompletion() {
        CountedCompleter<?> countedCompleter;
        CountedCompleter<?> countedCompleter2 = countedCompleter = this;
        while (true) {
            int n;
            if ((n = countedCompleter.pending) == 0) {
                countedCompleter2 = countedCompleter;
                countedCompleter = countedCompleter2.completer;
                if (countedCompleter != null) continue;
                countedCompleter2.quietlyComplete();
                return;
            }
            if (U.compareAndSwapInt(countedCompleter, PENDING, n, n - 1)) break;
        }
    }

    public final int getPendingCount() {
        return this.pending;
    }

    public abstract void compute();

    protected CountedCompleter(CountedCompleter<?> countedCompleter) {
        this.completer = countedCompleter;
    }

    public final boolean compareAndSetPendingCount(int n, int n2) {
        return U.compareAndSwapInt(this, PENDING, n, n2);
    }

    public final void tryComplete() {
        CountedCompleter<?> countedCompleter;
        CountedCompleter<?> countedCompleter2 = countedCompleter = this;
        while (true) {
            int n;
            if ((n = countedCompleter.pending) == 0) {
                countedCompleter.onCompletion(countedCompleter2);
                countedCompleter2 = countedCompleter;
                countedCompleter = countedCompleter2.completer;
                if (countedCompleter != null) continue;
                countedCompleter2.quietlyComplete();
                return;
            }
            if (U.compareAndSwapInt(countedCompleter, PENDING, n, n - 1)) break;
        }
    }

    public boolean onExceptionalCompletion(Throwable throwable, CountedCompleter<?> countedCompleter) {
        return true;
    }

    public final int decrementPendingCountUnlessZero() {
        int n;
        while ((n = this.pending) != 0 && !U.compareAndSwapInt(this, PENDING, n, n - 1)) {
        }
        return n;
    }

    public final void addToPendingCount(int n) {
        int n2;
        while (!U.compareAndSwapInt(this, PENDING, n2 = this.pending, n2 + n)) {
        }
    }

    public final CountedCompleter<?> nextComplete() {
        CountedCompleter<?> countedCompleter = this.completer;
        if (countedCompleter != null) {
            return countedCompleter.firstComplete();
        }
        this.quietlyComplete();
        return null;
    }

    public final void quietlyCompleteRoot() {
        CountedCompleter<?> countedCompleter = this;
        while (true) {
            CountedCompleter<?> countedCompleter2;
            if ((countedCompleter2 = countedCompleter.completer) == null) {
                countedCompleter.quietlyComplete();
                return;
            }
            countedCompleter = countedCompleter2;
        }
    }

    @Override
    public T getRawResult() {
        return null;
    }

    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException securityException) {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>(){

                    @Override
                    public Unsafe run() throws Exception {
                        Class<Unsafe> clazz = Unsafe.class;
                        for (Field field : clazz.getDeclaredFields()) {
                            field.setAccessible(true);
                            Object object = field.get(null);
                            if (!clazz.isInstance(object)) continue;
                            return (Unsafe)clazz.cast(object);
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException privilegedActionException) {
                throw new RuntimeException("Could not initialize intrinsics", privilegedActionException.getCause());
            }
        }
    }

    @Override
    protected final boolean exec() {
        this.compute();
        return false;
    }

    public final CountedCompleter<?> getCompleter() {
        return this.completer;
    }

    protected CountedCompleter(CountedCompleter<?> countedCompleter, int n) {
        this.completer = countedCompleter;
        this.pending = n;
    }

    public void onCompletion(CountedCompleter<?> countedCompleter) {
    }

    @Override
    protected void setRawResult(T t) {
    }

    @Override
    void internalPropagateException(Throwable throwable) {
        CountedCompleter<?> countedCompleter;
        CountedCompleter<?> countedCompleter2 = countedCompleter = this;
        while (countedCompleter.onExceptionalCompletion(throwable, countedCompleter2)) {
            countedCompleter2 = countedCompleter;
            countedCompleter = countedCompleter2.completer;
            if (countedCompleter != null && countedCompleter.status >= 0 && countedCompleter.recordExceptionalCompletion(throwable) == Integer.MIN_VALUE) continue;
        }
    }

    public final CountedCompleter<?> firstComplete() {
        int n;
        do {
            if ((n = this.pending) != 0) continue;
            return this;
        } while (!U.compareAndSwapInt(this, PENDING, n, n - 1));
        return null;
    }

    public final void setPendingCount(int n) {
        this.pending = n;
    }

    public final CountedCompleter<?> getRoot() {
        CountedCompleter<?> countedCompleter;
        CountedCompleter<?> countedCompleter2 = this;
        while ((countedCompleter = countedCompleter2.completer) != null) {
            countedCompleter2 = countedCompleter;
        }
        return countedCompleter2;
    }
}

