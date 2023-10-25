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
    private static final ReentrantLock exceptionTableLock = new ReentrantLock();
    private static final Unsafe U;
    static final int SIGNAL = 65536;
    private static final int EXCEPTION_MAP_CAPACITY = 32;
    static final int EXCEPTIONAL = Integer.MIN_VALUE;
    volatile int status;
    private static final long STATUS;
    static final int CANCELLED = -1073741824;
    static final int NORMAL = -268435456;
    static final int DONE_MASK = -268435456;
    private static final ReferenceQueue<Object> exceptionTableRefQueue;
    private static final long serialVersionUID = -7721805057305804111L;
    private static final ExceptionNode[] exceptionTable;
    static final int SMASK = 65535;

    public static int getQueuedTaskCount() {
        Thread thread = Thread.currentThread();
        ForkJoinPool.WorkQueue workQueue = thread instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)thread).workQueue : ForkJoinPool.commonSubmitterQueue();
        return workQueue == null ? 0 : workQueue.queueSize();
    }

    private int externalAwaitDone() {
        ForkJoinPool forkJoinPool = ForkJoinPool.common;
        int n = this.status;
        if (n >= 0) {
            if (forkJoinPool != null) {
                if (this instanceof CountedCompleter) {
                    n = forkJoinPool.externalHelpComplete((CountedCompleter)this);
                } else if (forkJoinPool.tryExternalUnpush(this)) {
                    n = this.doExec();
                }
            }
            if (n >= 0 && (n = this.status) >= 0) {
                boolean bl = false;
                do {
                    if (!U.compareAndSwapInt(this, STATUS, n, n | 0x10000)) continue;
                    ForkJoinTask forkJoinTask = this;
                    synchronized (forkJoinTask) {
                        if (this.status >= 0) {
                            try {
                                this.wait();
                            }
                            catch (InterruptedException interruptedException) {
                                bl = true;
                            }
                        } else {
                            this.notifyAll();
                        }
                    }
                } while ((n = this.status) >= 0);
                if (bl) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return n;
    }

    final int doExec() {
        int n = this.status;
        if (n >= 0) {
            boolean bl;
            try {
                bl = this.exec();
            }
            catch (Throwable throwable) {
                return this.setExceptionalCompletion(throwable);
            }
            if (bl) {
                n = this.setCompletion(-268435456);
            }
        }
        return n;
    }

    public static ForkJoinTask<?> adapt(Runnable runnable) {
        return new AdaptedRunnableAction(runnable);
    }

    private int setCompletion(int n) {
        block2: {
            int n2;
            do {
                if ((n2 = this.status) >= 0) continue;
                return n2;
            } while (!U.compareAndSwapInt(this, STATUS, n2, n2 | n));
            if (n2 >>> 16 == 0) break block2;
            ForkJoinTask forkJoinTask = this;
            synchronized (forkJoinTask) {
                this.notifyAll();
            }
        }
        return n;
    }

    private static void expungeStaleExceptions() {
        Reference<Object> reference;
        block0: while ((reference = exceptionTableRefQueue.poll()) != null) {
            if (!(reference instanceof ExceptionNode)) continue;
            ForkJoinTask forkJoinTask = (ForkJoinTask)((ExceptionNode)reference).get();
            ExceptionNode[] exceptionNodeArray = exceptionTable;
            int n = System.identityHashCode(forkJoinTask) & exceptionNodeArray.length - 1;
            ExceptionNode exceptionNode = exceptionNodeArray[n];
            ExceptionNode exceptionNode2 = null;
            while (exceptionNode != null) {
                ExceptionNode exceptionNode3 = exceptionNode.next;
                if (exceptionNode == reference) {
                    if (exceptionNode2 == null) {
                        exceptionNodeArray[n] = exceptionNode3;
                        continue block0;
                    }
                    exceptionNode2.next = exceptionNode3;
                    continue block0;
                }
                exceptionNode2 = exceptionNode;
                exceptionNode = exceptionNode3;
            }
        }
    }

    public final boolean compareAndSetForkJoinTaskTag(short s, short s2) {
        int n;
        do {
            if ((short)(n = this.status) == s) continue;
            return false;
        } while (!U.compareAndSwapInt(this, STATUS, n, n & 0xFFFF0000 | s2 & 0xFFFF));
        return true;
    }

    private void reportException(int n) {
        if (n == -1073741824) {
            throw new CancellationException();
        }
        if (n == Integer.MIN_VALUE) {
            ForkJoinTask.rethrow(this.getThrowableException());
        }
    }

    public boolean tryUnfork() {
        Thread thread = Thread.currentThread();
        return thread instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)thread).workQueue.tryUnpush(this) : ForkJoinPool.common.tryExternalUnpush(this);
    }

    @Override
    public boolean cancel(boolean bl) {
        return (this.setCompletion(-1073741824) & 0xF0000000) == -1073741824;
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

    public final V invoke() {
        int n = this.doInvoke() & 0xF0000000;
        if (n != -268435456) {
            this.reportException(n);
        }
        return this.getRawResult();
    }

    public void completeExceptionally(Throwable throwable) {
        this.setExceptionalCompletion(throwable instanceof RuntimeException || throwable instanceof Error ? throwable : new RuntimeException(throwable));
    }

    public final void quietlyInvoke() {
        this.doInvoke();
    }

    private Throwable getThrowableException() {
        if ((this.status & 0xF0000000) != Integer.MIN_VALUE) {
            return null;
        }
        int n = System.identityHashCode(this);
        ReentrantLock reentrantLock = exceptionTableLock;
        reentrantLock.lock();
        ForkJoinTask.expungeStaleExceptions();
        Object object = exceptionTable;
        ExceptionNode exceptionNode = object[n & ((ExceptionNode[])object).length - 1];
        while (exceptionNode != null && exceptionNode.get() != this) {
            exceptionNode = exceptionNode.next;
        }
        reentrantLock.unlock();
        if (exceptionNode == null || (object = exceptionNode.ex) == null) {
            return null;
        }
        return object;
    }

    @Override
    public final V get() throws InterruptedException, ExecutionException {
        Throwable throwable;
        int n;
        int n2 = n = Thread.currentThread() instanceof ForkJoinWorkerThread ? this.doJoin() : this.externalInterruptibleAwaitDone();
        if ((n &= 0xF0000000) == -1073741824) {
            throw new CancellationException();
        }
        if (n == Integer.MIN_VALUE && (throwable = this.getThrowableException()) != null) {
            throw new ExecutionException(throwable);
        }
        return this.getRawResult();
    }

    static void rethrow(Throwable throwable) {
        if (throwable != null) {
            ForkJoinTask.uncheckedThrow(throwable);
        }
    }

    public final void quietlyComplete() {
        this.setCompletion(-268435456);
    }

    public final V join() {
        int n = this.doJoin() & 0xF0000000;
        if (n != -268435456) {
            this.reportException(n);
        }
        return this.getRawResult();
    }

    public static <T> ForkJoinTask<T> adapt(Callable<? extends T> callable) {
        return new AdaptedCallable<T>(callable);
    }

    public static <T> ForkJoinTask<T> adapt(Runnable runnable, T t) {
        return new AdaptedRunnable<T>(runnable, t);
    }

    public static boolean inForkJoinPool() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }

    private int externalInterruptibleAwaitDone() throws InterruptedException {
        ForkJoinPool forkJoinPool = ForkJoinPool.common;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int n = this.status;
        if (n >= 0 && forkJoinPool != null) {
            if (this instanceof CountedCompleter) {
                forkJoinPool.externalHelpComplete((CountedCompleter)this);
            } else if (forkJoinPool.tryExternalUnpush(this)) {
                this.doExec();
            }
        }
        while ((n = this.status) >= 0) {
            if (!U.compareAndSwapInt(this, STATUS, n, n | 0x10000)) continue;
            ForkJoinTask forkJoinTask = this;
            synchronized (forkJoinTask) {
                if (this.status >= 0) {
                    this.wait();
                } else {
                    this.notifyAll();
                }
            }
        }
        return n;
    }

    protected abstract void setRawResult(V var1);

    static <T extends Throwable> void uncheckedThrow(Throwable throwable) throws T {
        throw throwable;
    }

    public final boolean isCompletedNormally() {
        return (this.status & 0xF0000000) == -268435456;
    }

    public void reinitialize() {
        if ((this.status & 0xF0000000) == Integer.MIN_VALUE) {
            this.clearExceptionalCompletion();
        } else {
            this.status = 0;
        }
    }

    public static ForkJoinPool getPool() {
        Thread thread = Thread.currentThread();
        return thread instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)thread).pool : null;
    }

    private void clearExceptionalCompletion() {
        int n = System.identityHashCode(this);
        ReentrantLock reentrantLock = exceptionTableLock;
        reentrantLock.lock();
        ExceptionNode[] exceptionNodeArray = exceptionTable;
        int n2 = n & exceptionNodeArray.length - 1;
        ExceptionNode exceptionNode = exceptionNodeArray[n2];
        ExceptionNode exceptionNode2 = null;
        while (exceptionNode != null) {
            ExceptionNode exceptionNode3 = exceptionNode.next;
            if (exceptionNode.get() == this) {
                if (exceptionNode2 == null) {
                    exceptionNodeArray[n2] = exceptionNode3;
                    break;
                }
                exceptionNode2.next = exceptionNode3;
                break;
            }
            exceptionNode2 = exceptionNode;
            exceptionNode = exceptionNode3;
        }
        ForkJoinTask.expungeStaleExceptions();
        this.status = 0;
        reentrantLock.unlock();
    }

    public abstract V getRawResult();

    public final void quietlyJoin() {
        this.doJoin();
    }

    static final void helpExpungeStaleExceptions() {
        block0: {
            ReentrantLock reentrantLock = exceptionTableLock;
            if (!reentrantLock.tryLock()) break block0;
            ForkJoinTask.expungeStaleExceptions();
            reentrantLock.unlock();
        }
    }

    static {
        exceptionTableRefQueue = new ReferenceQueue();
        exceptionTable = new ExceptionNode[32];
        try {
            U = ForkJoinTask.getUnsafe();
            Class<ForkJoinTask> clazz = ForkJoinTask.class;
            STATUS = U.objectFieldOffset(clazz.getDeclaredField("status"));
        }
        catch (Exception exception) {
            throw new Error(exception);
        }
    }

    public final boolean isCompletedAbnormally() {
        return this.status < -268435456;
    }

    protected static ForkJoinTask<?> pollTask() {
        ForkJoinTask<?> forkJoinTask;
        Thread thread = Thread.currentThread();
        if (thread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread)thread;
            forkJoinTask = forkJoinWorkerThread.pool.nextTaskFor(forkJoinWorkerThread.workQueue);
        } else {
            forkJoinTask = null;
        }
        return forkJoinTask;
    }

    protected static ForkJoinTask<?> pollNextLocalTask() {
        Thread thread = Thread.currentThread();
        return thread instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)thread).workQueue.nextLocalTask() : null;
    }

    private int doJoin() {
        int n;
        int n2 = this.status;
        if (n2 < 0) {
            n = n2;
        } else {
            Thread thread = Thread.currentThread();
            if (thread instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread)thread;
                ForkJoinPool.WorkQueue workQueue = forkJoinWorkerThread.workQueue;
                n = workQueue.tryUnpush(this) && (n2 = this.doExec()) < 0 ? n2 : forkJoinWorkerThread.pool.awaitJoin(workQueue, this);
            } else {
                n = this.externalAwaitDone();
            }
        }
        return n;
    }

    @Override
    public final V get(long l, TimeUnit timeUnit) throws TimeoutException, ExecutionException, InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        long l2 = timeUnit.toNanos(l);
        int n = this.status;
        if (n >= 0 && l2 > 0L) {
            long l3 = System.nanoTime() + l2;
            ForkJoinPool forkJoinPool = null;
            ForkJoinPool.WorkQueue workQueue = null;
            Thread thread = Thread.currentThread();
            if (thread instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread)thread;
                forkJoinPool = forkJoinWorkerThread.pool;
                workQueue = forkJoinWorkerThread.workQueue;
                forkJoinPool.helpJoinOnce(workQueue, this);
            } else {
                ForkJoinPool forkJoinPool2 = ForkJoinPool.common;
                if (forkJoinPool2 != null) {
                    if (this instanceof CountedCompleter) {
                        forkJoinPool2.externalHelpComplete((CountedCompleter)this);
                    } else if (forkJoinPool2.tryExternalUnpush(this)) {
                        this.doExec();
                    }
                }
            }
            boolean bl = false;
            boolean bl2 = false;
            while ((n = this.status) >= 0) {
                if (workQueue != null && workQueue.qlock < 0) {
                    ForkJoinTask.cancelIgnoringExceptions(this);
                    continue;
                }
                if (!bl) {
                    if (forkJoinPool != null && !forkJoinPool.tryCompensate(forkJoinPool.ctl)) continue;
                    bl = true;
                    continue;
                }
                long l4 = TimeUnit.NANOSECONDS.toMillis(l2);
                if (l4 > 0L && U.compareAndSwapInt(this, STATUS, n, n | 0x10000)) {
                    ForkJoinTask forkJoinTask = this;
                    synchronized (forkJoinTask) {
                        if (this.status >= 0) {
                            try {
                                this.wait(l4);
                            }
                            catch (InterruptedException interruptedException) {
                                if (forkJoinPool == null) {
                                    bl2 = true;
                                }
                            }
                        } else {
                            this.notifyAll();
                        }
                    }
                }
                if ((n = this.status) >= 0 && !bl2 && (l2 = l3 - System.nanoTime()) > 0L) continue;
            }
            if (forkJoinPool != null && bl) {
                forkJoinPool.incrementActiveCount();
            }
            if (bl2) {
                throw new InterruptedException();
            }
        }
        if ((n &= 0xF0000000) != -268435456) {
            if (n == -1073741824) {
                throw new CancellationException();
            }
            if (n != Integer.MIN_VALUE) {
                throw new TimeoutException();
            }
            Throwable throwable = this.getThrowableException();
            if (throwable != null) {
                throw new ExecutionException(throwable);
            }
        }
        return this.getRawResult();
    }

    @Override
    public final boolean isCancelled() {
        return (this.status & 0xF0000000) == -1073741824;
    }

    protected static ForkJoinTask<?> peekNextLocalTask() {
        Thread thread = Thread.currentThread();
        ForkJoinPool.WorkQueue workQueue = thread instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread)thread).workQueue : ForkJoinPool.commonSubmitterQueue();
        return workQueue == null ? null : workQueue.peek();
    }

    public void complete(V v) {
        try {
            this.setRawResult(v);
        }
        catch (Throwable throwable) {
            this.setExceptionalCompletion(throwable);
            return;
        }
        this.setCompletion(-268435456);
    }

    public static void helpQuiesce() {
        Thread thread = Thread.currentThread();
        if (thread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread)thread;
            forkJoinWorkerThread.pool.helpQuiescePool(forkJoinWorkerThread.workQueue);
        } else {
            ForkJoinPool.quiesceCommonPool();
        }
    }

    public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(Collection<T> collection) {
        ForkJoinTask forkJoinTask;
        int n;
        int n2;
        if (!(collection instanceof RandomAccess) || !(collection instanceof List)) {
            ForkJoinTask.invokeAll(collection.toArray(new ForkJoinTask[collection.size()]));
            return collection;
        }
        List list = (List)collection;
        Throwable throwable = null;
        for (n2 = n = list.size() - 1; n2 >= 0; --n2) {
            forkJoinTask = (ForkJoinTask)list.get(n2);
            if (forkJoinTask == null) {
                if (throwable != null) continue;
                throwable = new NullPointerException();
                continue;
            }
            if (n2 != 0) {
                forkJoinTask.fork();
                continue;
            }
            if (forkJoinTask.doInvoke() >= -268435456 || throwable != null) continue;
            throwable = forkJoinTask.getException();
        }
        for (n2 = 1; n2 <= n; ++n2) {
            forkJoinTask = (ForkJoinTask)list.get(n2);
            if (forkJoinTask == null) continue;
            if (throwable != null) {
                forkJoinTask.cancel(false);
                continue;
            }
            if (forkJoinTask.doJoin() >= -268435456) continue;
            throwable = forkJoinTask.getException();
        }
        if (throwable != null) {
            ForkJoinTask.rethrow(throwable);
        }
        return collection;
    }

    static final void cancelIgnoringExceptions(ForkJoinTask<?> forkJoinTask) {
        if (forkJoinTask != null && forkJoinTask.status >= 0) {
            try {
                forkJoinTask.cancel(false);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    void internalPropagateException(Throwable throwable) {
    }

    public final short getForkJoinTaskTag() {
        return (short)this.status;
    }

    final boolean trySetSignal() {
        int n = this.status;
        return n >= 0 && U.compareAndSwapInt(this, STATUS, n, n | 0x10000);
    }

    private int setExceptionalCompletion(Throwable throwable) {
        int n = this.recordExceptionalCompletion(throwable);
        if ((n & 0xF0000000) == Integer.MIN_VALUE) {
            this.internalPropagateException(throwable);
        }
        return n;
    }

    public static int getSurplusQueuedTaskCount() {
        return ForkJoinPool.getSurplusQueuedTaskCount();
    }

    private int doInvoke() {
        int n;
        int n2 = this.doExec();
        if (n2 < 0) {
            n = n2;
        } else {
            Thread thread = Thread.currentThread();
            if (thread instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread)thread;
                n = forkJoinWorkerThread.pool.awaitJoin(forkJoinWorkerThread.workQueue, this);
            } else {
                n = this.externalAwaitDone();
            }
        }
        return n;
    }

    protected abstract boolean exec();

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.getException());
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        Object object = objectInputStream.readObject();
        if (object != null) {
            this.setExceptionalCompletion((Throwable)object);
        }
    }

    public final ForkJoinTask<V> fork() {
        Thread thread = Thread.currentThread();
        if (thread instanceof ForkJoinWorkerThread) {
            ((ForkJoinWorkerThread)thread).workQueue.push(this);
        } else {
            ForkJoinPool.common.externalPush(this);
        }
        return this;
    }

    public static void invokeAll(ForkJoinTask<?> ... forkJoinTaskArray) {
        ForkJoinTask<?> forkJoinTask;
        int n;
        int n2;
        Throwable throwable = null;
        for (n2 = n = forkJoinTaskArray.length - 1; n2 >= 0; --n2) {
            forkJoinTask = forkJoinTaskArray[n2];
            if (forkJoinTask == null) {
                if (throwable != null) continue;
                throwable = new NullPointerException();
                continue;
            }
            if (n2 != 0) {
                forkJoinTask.fork();
                continue;
            }
            if (super.doInvoke() >= -268435456 || throwable != null) continue;
            throwable = forkJoinTask.getException();
        }
        for (n2 = 1; n2 <= n; ++n2) {
            forkJoinTask = forkJoinTaskArray[n2];
            if (forkJoinTask == null) continue;
            if (throwable != null) {
                forkJoinTask.cancel(false);
                continue;
            }
            if (super.doJoin() >= -268435456) continue;
            throwable = forkJoinTask.getException();
        }
        if (throwable != null) {
            ForkJoinTask.rethrow(throwable);
        }
    }

    final int recordExceptionalCompletion(Throwable throwable) {
        int n = this.status;
        if (n >= 0) {
            int n2 = System.identityHashCode(this);
            ReentrantLock reentrantLock = exceptionTableLock;
            reentrantLock.lock();
            ForkJoinTask.expungeStaleExceptions();
            ExceptionNode[] exceptionNodeArray = exceptionTable;
            int n3 = n2 & exceptionNodeArray.length - 1;
            ExceptionNode exceptionNode = exceptionNodeArray[n3];
            while (true) {
                if (exceptionNode == null) {
                    exceptionNodeArray[n3] = new ExceptionNode(this, throwable, exceptionNodeArray[n3]);
                    break;
                }
                if (exceptionNode.get() == this) break;
                exceptionNode = exceptionNode.next;
            }
            reentrantLock.unlock();
            n = this.setCompletion(Integer.MIN_VALUE);
        }
        return n;
    }

    public final short setForkJoinTaskTag(short s) {
        int n;
        while (!U.compareAndSwapInt(this, STATUS, n = this.status, n & 0xFFFF0000 | s & 0xFFFF)) {
        }
        return (short)n;
    }

    public final Throwable getException() {
        int n = this.status & 0xF0000000;
        return n >= -268435456 ? null : (n == -1073741824 ? new CancellationException() : this.getThrowableException());
    }

    public static void invokeAll(ForkJoinTask<?> forkJoinTask, ForkJoinTask<?> forkJoinTask2) {
        int n;
        forkJoinTask2.fork();
        int n2 = super.doInvoke() & 0xF0000000;
        if (n2 != -268435456) {
            super.reportException(n2);
        }
        if ((n = super.doJoin() & 0xF0000000) != -268435456) {
            super.reportException(n);
        }
    }

    @Override
    public final boolean isDone() {
        return this.status < 0;
    }

    static final class AdaptedRunnableAction
    extends ForkJoinTask<Void>
    implements RunnableFuture<Void> {
        private static final long serialVersionUID = 5232453952276885070L;
        final Runnable runnable;

        @Override
        public final void run() {
            this.invoke();
        }

        AdaptedRunnableAction(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
        }

        @Override
        public final void setRawResult(Void void_) {
        }

        @Override
        public final Void getRawResult() {
            return null;
        }

        @Override
        public final boolean exec() {
            this.runnable.run();
            return true;
        }
    }

    static final class AdaptedRunnable<T>
    extends ForkJoinTask<T>
    implements RunnableFuture<T> {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;
        T result;

        @Override
        public final void run() {
            this.invoke();
        }

        @Override
        public final void setRawResult(T t) {
            this.result = t;
        }

        @Override
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override
        public final T getRawResult() {
            return this.result;
        }

        AdaptedRunnable(Runnable runnable, T t) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
            this.result = t;
        }
    }

    static final class RunnableExecuteAction
    extends ForkJoinTask<Void> {
        private static final long serialVersionUID = 5232453952276885070L;
        final Runnable runnable;

        @Override
        public final Void getRawResult() {
            return null;
        }

        @Override
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override
        void internalPropagateException(Throwable throwable) {
            RunnableExecuteAction.rethrow(throwable);
        }

        @Override
        public final void setRawResult(Void void_) {
        }

        RunnableExecuteAction(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
        }
    }

    static final class ExceptionNode
    extends WeakReference<ForkJoinTask<?>> {
        final Throwable ex;
        final long thrower;
        ExceptionNode next;

        ExceptionNode(ForkJoinTask<?> forkJoinTask, Throwable throwable, ExceptionNode exceptionNode) {
            super(forkJoinTask, exceptionTableRefQueue);
            this.ex = throwable;
            this.next = exceptionNode;
            this.thrower = Thread.currentThread().getId();
        }
    }

    static final class AdaptedCallable<T>
    extends ForkJoinTask<T>
    implements RunnableFuture<T> {
        T result;
        private static final long serialVersionUID = 2838392045355241008L;
        final Callable<? extends T> callable;

        @Override
        public final void run() {
            this.invoke();
        }

        AdaptedCallable(Callable<? extends T> callable) {
            if (callable == null) {
                throw new NullPointerException();
            }
            this.callable = callable;
        }

        @Override
        public final void setRawResult(T t) {
            this.result = t;
        }

        @Override
        public final T getRawResult() {
            return this.result;
        }

        @Override
        public final boolean exec() {
            try {
                this.result = this.callable.call();
                return true;
            }
            catch (Error error) {
                throw error;
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}

