/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.chmv8;

import io.netty.util.internal.chmv8.CountedCompleter;
import io.netty.util.internal.chmv8.ForkJoinPool;
import io.netty.util.internal.chmv8.ForkJoinWorkerThread;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import sun.misc.Unsafe;

public abstract class ForkJoinTask<V>
implements Future<V>,
Serializable {
    volatile int status;
    static final int DONE_MASK = -268435456;
    static final int NORMAL = -268435456;
    static final int CANCELLED = -1073741824;
    static final int EXCEPTIONAL = Integer.MIN_VALUE;
    static final int SIGNAL = 65536;
    static final int SMASK = 65535;
    private static final ExceptionNode[] exceptionTable;
    private static final ReentrantLock exceptionTableLock;
    private static final ReferenceQueue<Object> exceptionTableRefQueue;
    private static final int EXCEPTION_MAP_CAPACITY = 32;
    private static final long serialVersionUID = -7721805057305804111L;
    private static final Unsafe U;
    private static final long STATUS;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int setCompletion(int completion) {
        int s2;
        do {
            if ((s2 = this.status) >= 0) continue;
            return s2;
        } while (!U.compareAndSwapInt(this, STATUS, s2, s2 | completion));
        if (s2 >>> 16 != 0) {
            ForkJoinTask forkJoinTask = this;
            synchronized (forkJoinTask) {
                this.notifyAll();
            }
        }
        return completion;
    }

    final int doExec() {
        int s2 = this.status;
        if (s2 >= 0) {
            boolean completed;
            try {
                completed = this.exec();
            }
            catch (Throwable rex) {
                return this.setExceptionalCompletion(rex);
            }
            if (completed) {
                s2 = this.setCompletion(-268435456);
            }
        }
        return s2;
    }

    final boolean trySetSignal() {
        int s2 = this.status;
        return s2 >= 0 && U.compareAndSwapInt(this, STATUS, s2, s2 | 0x10000);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int externalAwaitDone() {
        ForkJoinPool cp = ForkJoinPool.common;
        int s2 = this.status;
        if (s2 >= 0) {
            if (cp != null) {
                if (this instanceof CountedCompleter) {
                    s2 = cp.externalHelpComplete((CountedCompleter)this);
                } else if (cp.tryExternalUnpush(this)) {
                    s2 = this.doExec();
                }
            }
            if (s2 >= 0 && (s2 = this.status) >= 0) {
                boolean interrupted = false;
                do {
                    if (!U.compareAndSwapInt(this, STATUS, s2, s2 | 0x10000)) continue;
                    ForkJoinTask forkJoinTask = this;
                    synchronized (forkJoinTask) {
                        if (this.status >= 0) {
                            try {
                                this.wait();
                            }
                            catch (InterruptedException ie) {
                                interrupted = true;
                            }
                        } else {
                            this.notifyAll();
                        }
                    }
                } while ((s2 = this.status) >= 0);
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return s2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int externalInterruptibleAwaitDone() throws InterruptedException {
        ForkJoinPool cp = ForkJoinPool.common;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int s2 = this.status;
        if (s2 >= 0 && cp != null) {
            if (this instanceof CountedCompleter) {
                cp.externalHelpComplete((CountedCompleter)this);
            } else if (cp.tryExternalUnpush(this)) {
                this.doExec();
            }
        }
        while ((s2 = this.status) >= 0) {
            if (!U.compareAndSwapInt(this, STATUS, s2, s2 | 0x10000)) continue;
            ForkJoinTask forkJoinTask = this;
            synchronized (forkJoinTask) {
                if (this.status >= 0) {
                    this.wait();
                } else {
                    this.notifyAll();
                }
            }
        }
        return s2;
    }

    private int doJoin() {
        int n2;
        int s2 = this.status;
        if (s2 < 0) {
            n2 = s2;
        } else {
            Thread t2 = Thread.currentThread();
            if (t2 instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t2;
                ForkJoinPool.WorkQueue w2 = wt.workQueue;
                n2 = w2.tryUnpush(this) && (s2 = this.doExec()) < 0 ? s2 : wt.pool.awaitJoin(w2, this);
            } else {
                n2 = this.externalAwaitDone();
            }
        }
        return n2;
    }

    private int doInvoke() {
        int n2;
        int s2 = this.doExec();
        if (s2 < 0) {
            n2 = s2;
        } else {
            Thread t2 = Thread.currentThread();
            if (t2 instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t2;
                n2 = wt.pool.awaitJoin(wt.workQueue, this);
            } else {
                n2 = this.externalAwaitDone();
            }
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    final int recordExceptionalCompletion(Throwable ex) {
        int s2 = this.status;
        if (s2 >= 0) {
            int h2 = System.identityHashCode(this);
            ReentrantLock lock = exceptionTableLock;
            lock.lock();
            try {
                ForkJoinTask.expungeStaleExceptions();
                ExceptionNode[] t2 = exceptionTable;
                int i2 = h2 & t2.length - 1;
                ExceptionNode e2 = t2[i2];
                while (true) {
                    if (e2 == null) {
                        t2[i2] = new ExceptionNode(this, ex, t2[i2]);
                        break;
                    }
                    if (e2.get() == this) {
                        break;
                    }
                    e2 = e2.next;
                }
            }
            finally {
                lock.unlock();
            }
            s2 = this.setCompletion(Integer.MIN_VALUE);
        }
        return s2;
    }

    private int setExceptionalCompletion(Throwable ex) {
        int s2 = this.recordExceptionalCompletion(ex);
        if ((s2 & 0xF0000000) == Integer.MIN_VALUE) {
            this.internalPropagateException(ex);
        }
        return s2;
    }

    void internalPropagateException(Throwable ex) {
    }

    static final void cancelIgnoringExceptions(ForkJoinTask<?> t2) {
        if (t2 != null && t2.status >= 0) {
            try {
                t2.cancel(false);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void clearExceptionalCompletion() {
        int h2 = System.identityHashCode(this);
        ReentrantLock lock = exceptionTableLock;
        lock.lock();
        try {
            ExceptionNode[] t2 = exceptionTable;
            int i2 = h2 & t2.length - 1;
            ExceptionNode e2 = t2[i2];
            ExceptionNode pred = null;
            while (e2 != null) {
                ExceptionNode next = e2.next;
                if (e2.get() == this) {
                    if (pred == null) {
                        t2[i2] = next;
                        break;
                    }
                    pred.next = next;
                    break;
                }
                pred = e2;
                e2 = next;
            }
            ForkJoinTask.expungeStaleExceptions();
            this.status = 0;
        }
        finally {
            lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Throwable getThrowableException() {
        Throwable ex;
        ExceptionNode e2;
        if ((this.status & 0xF0000000) != Integer.MIN_VALUE) {
            return null;
        }
        int h2 = System.identityHashCode(this);
        ReentrantLock lock = exceptionTableLock;
        lock.lock();
        try {
            ForkJoinTask.expungeStaleExceptions();
            ExceptionNode[] t2 = exceptionTable;
            e2 = t2[h2 & t2.length - 1];
            while (e2 != null && e2.get() != this) {
                e2 = e2.next;
            }
        }
        finally {
            lock.unlock();
        }
        if (e2 == null || (ex = e2.ex) == null) {
            return null;
        }
        return ex;
    }

    private static void expungeStaleExceptions() {
        Reference<Object> x2;
        block0: while ((x2 = exceptionTableRefQueue.poll()) != null) {
            if (!(x2 instanceof ExceptionNode)) continue;
            ForkJoinTask key = (ForkJoinTask)((ExceptionNode)x2).get();
            ExceptionNode[] t2 = exceptionTable;
            int i2 = System.identityHashCode(key) & t2.length - 1;
            ExceptionNode e2 = t2[i2];
            ExceptionNode pred = null;
            while (e2 != null) {
                ExceptionNode next = e2.next;
                if (e2 == x2) {
                    if (pred == null) {
                        t2[i2] = next;
                        continue block0;
                    }
                    pred.next = next;
                    continue block0;
                }
                pred = e2;
                e2 = next;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static final void helpExpungeStaleExceptions() {
        ReentrantLock lock = exceptionTableLock;
        if (lock.tryLock()) {
            try {
                ForkJoinTask.expungeStaleExceptions();
            }
            finally {
                lock.unlock();
            }
        }
    }

    static void rethrow(Throwable ex) {
        if (ex != null) {
            ForkJoinTask.uncheckedThrow(ex);
        }
    }

    static <T extends Throwable> void uncheckedThrow(Throwable t2) throws T {
        throw t2;
    }

    private void reportException(int s2) {
        if (s2 == -1073741824) {
            throw new CancellationException();
        }
        if (s2 == Integer.MIN_VALUE) {
            ForkJoinTask.rethrow(this.getThrowableException());
        }
    }

    public final ForkJoinTask<V> fork() {
        Thread t2 = Thread.currentThread();
        if (t2 instanceof ForkJoinWorkerThread) {
            ((ForkJoinWorkerThread)t2).workQueue.push(this);
        } else {
            ForkJoinPool.common.externalPush(this);
        }
        return this;
    }

    public final V join() {
        int s2 = this.doJoin() & 0xF0000000;
        if (s2 != -268435456) {
            this.reportException(s2);
        }
        return this.getRawResult();
    }

    public final V invoke() {
        int s2 = this.doInvoke() & 0xF0000000;
        if (s2 != -268435456) {
            this.reportException(s2);
        }
        return this.getRawResult();
    }

    public static void invokeAll(ForkJoinTask<?> t1, ForkJoinTask<?> t2) {
        int s2;
        t2.fork();
        int s1 = super.doInvoke() & 0xF0000000;
        if (s1 != -268435456) {
            super.reportException(s1);
        }
        if ((s2 = super.doJoin() & 0xF0000000) != -268435456) {
            super.reportException(s2);
        }
    }

    public static void invokeAll(ForkJoinTask<?> ... tasks) {
        ForkJoinTask<?> t2;
        int last;
        int i2;
        Throwable ex = null;
        for (i2 = last = tasks.length - 1; i2 >= 0; --i2) {
            t2 = tasks[i2];
            if (t2 == null) {
                if (ex != null) continue;
                ex = new NullPointerException();
                continue;
            }
            if (i2 != 0) {
                t2.fork();
                continue;
            }
            if (super.doInvoke() >= -268435456 || ex != null) continue;
            ex = t2.getException();
        }
        for (i2 = 1; i2 <= last; ++i2) {
            t2 = tasks[i2];
            if (t2 == null) continue;
            if (ex != null) {
                t2.cancel(false);
                continue;
            }
            if (super.doJoin() >= -268435456) continue;
            ex = t2.getException();
        }
        if (ex != null) {
            ForkJoinTask.rethrow(ex);
        }
    }

    public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(Collection<T> tasks) {
        ForkJoinTask t2;
        int last;
        int i2;
        if (!(tasks instanceof RandomAccess) || !(tasks instanceof List)) {
            ForkJoinTask.invokeAll(tasks.toArray(new ForkJoinTask[tasks.size()]));
            return tasks;
        }
        List ts = (List)tasks;
        Throwable ex = null;
        for (i2 = last = ts.size() - 1; i2 >= 0; --i2) {
            t2 = (ForkJoinTask)ts.get(i2);
            if (t2 == null) {
                if (ex != null) continue;
                ex = new NullPointerException();
                continue;
            }
            if (i2 != 0) {
                t2.fork();
                continue;
            }
            if (t2.doInvoke() >= -268435456 || ex != null) continue;
            ex = t2.getException();
        }
        for (i2 = 1; i2 <= last; ++i2) {
            t2 = (ForkJoinTask)ts.get(i2);
            if (t2 == null) continue;
            if (ex != null) {
                t2.cancel(false);
                continue;
            }
            if (t2.doJoin() >= -268435456) continue;
            ex = t2.getException();
        }
        if (ex != null) {
            ForkJoinTask.rethrow(ex);
        }
        return tasks;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return (this.setCompletion(-1073741824) & 0xF0000000) == -1073741824;
    }

    @Override
    public final boolean isDone() {
        return this.status < 0;
    }

    @Override
    public final boolean isCancelled() {
        return (this.status & 0xF0000000) == -1073741824;
    }

    public final boolean isCompletedAbnormally() {
        return this.status < -268435456;
    }

    public final boolean isCompletedNormally() {
        return (this.status & 0xF0000000) == -268435456;
    }

    public final Throwable getException() {
        int s2 = this.status & 0xF0000000;
        return s2 >= -268435456 ? null : (s2 == -1073741824 ? new CancellationException() : this.getThrowableException());
    }

    public void completeExceptionally(Throwable ex) {
        this.setExceptionalCompletion(ex instanceof RuntimeException || ex instanceof Error ? ex : new RuntimeException(ex));
    }

    public void complete(V value) {
        try {
            this.setRawResult(value);
        }
        catch (Throwable rex) {
            this.setExceptionalCompletion(rex);
            return;
        }
        this.setCompletion(-268435456);
    }

    public final void quietlyComplete() {
        this.setCompletion(-268435456);
    }

    @Override
    public final V get() throws InterruptedException, ExecutionException {
        Throwable ex;
        int s2;
        int n2 = s2 = Thread.currentThread() instanceof ForkJoinWorkerThread ? this.doJoin() : this.externalInterruptibleAwaitDone();
        if ((s2 &= 0xF0000000) == -1073741824) {
            throw new CancellationException();
        }
        if (s2 == Integer.MIN_VALUE && (ex = this.getThrowableException()) != null) {
            throw new ExecutionException(ex);
        }
        return this.getRawResult();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        long ns = unit.toNanos(timeout);
        int s2 = this.status;
        if (s2 >= 0 && ns > 0L) {
            long deadline = System.nanoTime() + ns;
            ForkJoinPool p2 = null;
            ForkJoinPool.WorkQueue w2 = null;
            Thread t2 = Thread.currentThread();
            if (t2 instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t2;
                p2 = wt.pool;
                w2 = wt.workQueue;
                p2.helpJoinOnce(w2, this);
            } else {
                ForkJoinPool cp = ForkJoinPool.common;
                if (cp != null) {
                    if (this instanceof CountedCompleter) {
                        cp.externalHelpComplete((CountedCompleter)this);
                    } else if (cp.tryExternalUnpush(this)) {
                        this.doExec();
                    }
                }
            }
            boolean canBlock = false;
            boolean interrupted = false;
            try {
                while ((s2 = this.status) >= 0) {
                    if (w2 != null && w2.qlock < 0) {
                        ForkJoinTask.cancelIgnoringExceptions(this);
                        continue;
                    }
                    if (!canBlock) {
                        if (p2 != null && !p2.tryCompensate(p2.ctl)) continue;
                        canBlock = true;
                        continue;
                    }
                    long ms = TimeUnit.NANOSECONDS.toMillis(ns);
                    if (ms > 0L && U.compareAndSwapInt(this, STATUS, s2, s2 | 0x10000)) {
                        ForkJoinTask forkJoinTask = this;
                        synchronized (forkJoinTask) {
                            if (this.status >= 0) {
                                try {
                                    this.wait(ms);
                                }
                                catch (InterruptedException ie) {
                                    if (p2 == null) {
                                        interrupted = true;
                                    }
                                }
                            } else {
                                this.notifyAll();
                            }
                        }
                    }
                    if ((s2 = this.status) >= 0 && !interrupted && (ns = deadline - System.nanoTime()) > 0L) continue;
                    break;
                }
            }
            finally {
                if (p2 != null && canBlock) {
                    p2.incrementActiveCount();
                }
            }
            if (interrupted) {
                throw new InterruptedException();
            }
        }
        if ((s2 &= 0xF0000000) != -268435456) {
            if (s2 == -1073741824) {
                throw new CancellationException();
            }
            if (s2 != Integer.MIN_VALUE) {
                throw new TimeoutException();
            }
            Throwable ex = this.getThrowableException();
            if (ex != null) {
                throw new ExecutionException(ex);
            }
        }
        return this.getRawResult();
    }

    public final void quietlyJoin() {
        this.doJoin();
    }

    public final void quietlyInvoke() {
        this.doInvoke();
    }

    public static void helpQuiesce() {
        Thread t2 = Thread.currentThread();
        if (t2 instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t2;
            wt.pool.helpQuiescePool(wt.workQueue);
        } else {
            ForkJoinPool.quiesceCommonPool();
        }
    }

    public void reinitialize() {
        if ((this.status & 0xF0000000) == Integer.MIN_VALUE) {
            this.clearExceptionalCompletion();
        } else {
            this.status = 0;
        }
    }

    public static ForkJoinPool getPool() {
        Thread t2 = Thread.currentThread();
        return t2 instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)t2).pool : null;
    }

    public static boolean inForkJoinPool() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }

    public boolean tryUnfork() {
        Thread t2 = Thread.currentThread();
        return t2 instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)t2).workQueue.tryUnpush(this) : ForkJoinPool.common.tryExternalUnpush(this);
    }

    public static int getQueuedTaskCount() {
        Thread t2 = Thread.currentThread();
        ForkJoinPool.WorkQueue q2 = t2 instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)t2).workQueue : ForkJoinPool.commonSubmitterQueue();
        return q2 == null ? 0 : q2.queueSize();
    }

    public static int getSurplusQueuedTaskCount() {
        return ForkJoinPool.getSurplusQueuedTaskCount();
    }

    public abstract V getRawResult();

    protected abstract void setRawResult(V var1);

    protected abstract boolean exec();

    protected static ForkJoinTask<?> peekNextLocalTask() {
        Thread t2 = Thread.currentThread();
        ForkJoinPool.WorkQueue q2 = t2 instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)t2).workQueue : ForkJoinPool.commonSubmitterQueue();
        return q2 == null ? null : q2.peek();
    }

    protected static ForkJoinTask<?> pollNextLocalTask() {
        Thread t2 = Thread.currentThread();
        return t2 instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)t2).workQueue.nextLocalTask() : null;
    }

    protected static ForkJoinTask<?> pollTask() {
        ForkJoinTask<?> forkJoinTask;
        Thread t2 = Thread.currentThread();
        if (t2 instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread wt = (ForkJoinWorkerThread)t2;
            forkJoinTask = wt.pool.nextTaskFor(wt.workQueue);
        } else {
            forkJoinTask = null;
        }
        return forkJoinTask;
    }

    public final short getForkJoinTaskTag() {
        return (short)this.status;
    }

    public final short setForkJoinTaskTag(short tag) {
        int s2;
        while (!U.compareAndSwapInt(this, STATUS, s2 = this.status, s2 & 0xFFFF0000 | tag & 0xFFFF)) {
        }
        return (short)s2;
    }

    public final boolean compareAndSetForkJoinTaskTag(short e2, short tag) {
        int s2;
        do {
            if ((short)(s2 = this.status) == e2) continue;
            return false;
        } while (!U.compareAndSwapInt(this, STATUS, s2, s2 & 0xFFFF0000 | tag & 0xFFFF));
        return true;
    }

    public static ForkJoinTask<?> adapt(Runnable runnable) {
        return new AdaptedRunnableAction(runnable);
    }

    public static <T> ForkJoinTask<T> adapt(Runnable runnable, T result) {
        return new AdaptedRunnable<T>(runnable, result);
    }

    public static <T> ForkJoinTask<T> adapt(Callable<? extends T> callable) {
        return new AdaptedCallable<T>(callable);
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        s2.defaultWriteObject();
        s2.writeObject(this.getException());
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        Object ex = s2.readObject();
        if (ex != null) {
            this.setExceptionalCompletion((Throwable)ex);
        }
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
        exceptionTableLock = new ReentrantLock();
        exceptionTableRefQueue = new ReferenceQueue();
        exceptionTable = new ExceptionNode[32];
        try {
            U = ForkJoinTask.getUnsafe();
            Class<ForkJoinTask> k2 = ForkJoinTask.class;
            STATUS = U.objectFieldOffset(k2.getDeclaredField("status"));
        }
        catch (Exception e2) {
            throw new Error(e2);
        }
    }

    static final class AdaptedCallable<T>
    extends ForkJoinTask<T>
    implements RunnableFuture<T> {
        final Callable<? extends T> callable;
        T result;
        private static final long serialVersionUID = 2838392045355241008L;

        AdaptedCallable(Callable<? extends T> callable) {
            if (callable == null) {
                throw new NullPointerException();
            }
            this.callable = callable;
        }

        @Override
        public final T getRawResult() {
            return this.result;
        }

        @Override
        public final void setRawResult(T v2) {
            this.result = v2;
        }

        @Override
        public final boolean exec() {
            try {
                this.result = this.callable.call();
                return true;
            }
            catch (Error err) {
                throw err;
            }
            catch (RuntimeException rex) {
                throw rex;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public final void run() {
            this.invoke();
        }
    }

    static final class RunnableExecuteAction
    extends ForkJoinTask<Void> {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;

        RunnableExecuteAction(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
        }

        @Override
        public final Void getRawResult() {
            return null;
        }

        @Override
        public final void setRawResult(Void v2) {
        }

        @Override
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override
        void internalPropagateException(Throwable ex) {
            RunnableExecuteAction.rethrow(ex);
        }
    }

    static final class AdaptedRunnableAction
    extends ForkJoinTask<Void>
    implements RunnableFuture<Void> {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;

        AdaptedRunnableAction(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
        }

        @Override
        public final Void getRawResult() {
            return null;
        }

        @Override
        public final void setRawResult(Void v2) {
        }

        @Override
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override
        public final void run() {
            this.invoke();
        }
    }

    static final class AdaptedRunnable<T>
    extends ForkJoinTask<T>
    implements RunnableFuture<T> {
        final Runnable runnable;
        T result;
        private static final long serialVersionUID = 5232453952276885070L;

        AdaptedRunnable(Runnable runnable, T result) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
            this.result = result;
        }

        @Override
        public final T getRawResult() {
            return this.result;
        }

        @Override
        public final void setRawResult(T v2) {
            this.result = v2;
        }

        @Override
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override
        public final void run() {
            this.invoke();
        }
    }

    static final class ExceptionNode
    extends WeakReference<ForkJoinTask<?>> {
        final Throwable ex;
        ExceptionNode next;
        final long thrower;

        ExceptionNode(ForkJoinTask<?> task, Throwable ex, ExceptionNode next) {
            super(task, exceptionTableRefQueue);
            this.ex = ex;
            this.next = next;
            this.thrower = Thread.currentThread().getId();
        }
    }
}

