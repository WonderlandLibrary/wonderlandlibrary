/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.pool;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@ThreadSafe
abstract class PoolEntryFuture<T>
implements Future<T> {
    private final Lock lock;
    private final FutureCallback<T> callback;
    private final Condition condition;
    private volatile boolean cancelled;
    private volatile boolean completed;
    private T result;

    PoolEntryFuture(Lock lock, FutureCallback<T> callback) {
        this.lock = lock;
        this.condition = lock.newCondition();
        this.callback = callback;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.lock.lock();
        try {
            if (this.completed) {
                boolean bl = false;
                return bl;
            }
            this.completed = true;
            this.cancelled = true;
            if (this.callback != null) {
                this.callback.cancelled();
            }
            this.condition.signalAll();
            boolean bl = true;
            return bl;
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public boolean isDone() {
        return this.completed;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return this.get(0L, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException ex) {
            throw new ExecutionException(ex);
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Args.notNull(unit, "Time unit");
        this.lock.lock();
        try {
            if (this.completed) {
                T t = this.result;
                return t;
            }
            this.result = this.getPoolEntry(timeout, unit);
            this.completed = true;
            if (this.callback != null) {
                this.callback.completed(this.result);
            }
            T t = this.result;
            return t;
        }
        catch (IOException ex) {
            this.completed = true;
            this.result = null;
            if (this.callback != null) {
                this.callback.failed(ex);
            }
            throw new ExecutionException(ex);
        }
        finally {
            this.lock.unlock();
        }
    }

    protected abstract T getPoolEntry(long var1, TimeUnit var3) throws IOException, InterruptedException, TimeoutException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean await(Date deadline) throws InterruptedException {
        this.lock.lock();
        try {
            boolean success;
            if (this.cancelled) {
                throw new InterruptedException("Operation interrupted");
            }
            if (deadline != null) {
                success = this.condition.awaitUntil(deadline);
            } else {
                this.condition.await();
                success = true;
            }
            if (this.cancelled) {
                throw new InterruptedException("Operation interrupted");
            }
            boolean bl = success;
            return bl;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void wakeup() {
        this.lock.lock();
        try {
            this.condition.signalAll();
        }
        finally {
            this.lock.unlock();
        }
    }
}

