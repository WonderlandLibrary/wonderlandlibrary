/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.chmv8;

import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.chmv8.CountedCompleter;
import io.netty.util.internal.chmv8.ForkJoinTask;
import io.netty.util.internal.chmv8.ForkJoinWorkerThread;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import sun.misc.Unsafe;

public class ForkJoinPool
extends AbstractExecutorService {
    private static final int INT_SIGN = Integer.MIN_VALUE;
    volatile Object pad12;
    private static final long STOP_BIT = 0x80000000L;
    private static final int E_MASK = Integer.MAX_VALUE;
    private static final int MAX_HELP = 64;
    volatile Object pad13;
    volatile long pad05;
    private static final int UAC_SHIFT = 16;
    private static final long TIMEOUT_SLOP = 2000000L;
    volatile long pad03;
    private static final int SMASK = 65535;
    private static final int E_SEQ = 65536;
    volatile Object pad16;
    final short parallelism;
    volatile Object pad19;
    private static final long IDLE_TIMEOUT = 2000000000L;
    private static final int UAC_MASK = -65536;
    public static final ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory;
    private static final int SHORT_SIGN = 32768;
    volatile long stealCount;
    WorkQueue[] workQueues;
    static final ForkJoinPool common;
    private static final int SEED_INCREMENT = 1640531527;
    private static final long TC_UNIT = 0x100000000L;
    private static final long AC_MASK = -281474976710656L;
    private static final long PLOCK;
    volatile Object pad17;
    static final int commonParallelism;
    volatile Object pad10;
    private static final int ABASE;
    volatile Object pad1a;
    private static final long FAST_IDLE_TIMEOUT = 200000000L;
    volatile Object pad1b;
    final String workerNamePrefix;
    private static final int UTC_SHIFT = 0;
    volatile int plock;
    private static final int MAX_CAP = Short.MAX_VALUE;
    private static final int UAC_UNIT = 65536;
    private static final long QBASE;
    private static final long STEALCOUNT;
    private static final int SQMASK = 126;
    volatile long ctl;
    volatile long pad02;
    private static final int UTC_UNIT = 1;
    volatile long pad01;
    final ForkJoinWorkerThreadFactory factory;
    static final int LIFO_QUEUE = 0;
    private static final long AC_UNIT = 0x1000000000000L;
    static final int SHARED_QUEUE = -1;
    volatile Object pad15;
    private static final long TC_MASK = 0xFFFF00000000L;
    private static final long QLOCK;
    static final ThreadLocal<Submitter> submitters;
    volatile long pad04;
    private static final int TC_SHIFT = 32;
    final Thread.UncaughtExceptionHandler ueh;
    volatile long pad06;
    private static final int PL_LOCK = 2;
    private static final long PARKBLOCKER;
    private static final int PL_SIGNAL = 1;
    private static final int ASHIFT;
    private static final int SHUTDOWN = Integer.MIN_VALUE;
    volatile Object pad18;
    private static final Unsafe U;
    private static final int EVENMASK = 65534;
    private static int poolNumberSequence;
    private static final int ST_SHIFT = 31;
    private static final long CTL;
    private static final RuntimePermission modifyThreadPermission;
    final short mode;
    static final int FIFO_QUEUE = 1;
    private static final long INDEXSEED;
    volatile Object pad11;
    private static final int UTC_MASK = 65535;
    private static final int EC_SHIFT = 16;
    volatile int indexSeed;
    volatile long pad00;
    private static final int PL_SPINS = 256;
    private static final int AC_SHIFT = 48;
    volatile Object pad14;

    final ForkJoinTask<?> nextTaskFor(WorkQueue workQueue) {
        ForkJoinTask<?> forkJoinTask;
        WorkQueue workQueue2;
        int n;
        do {
            if ((forkJoinTask = workQueue.nextLocalTask()) != null) {
                return forkJoinTask;
            }
            workQueue2 = this.findNonEmptyStealQueue();
            if (workQueue2 != null) continue;
            return null;
        } while ((n = workQueue2.base) - workQueue2.top >= 0 || (forkJoinTask = workQueue2.pollAt(n)) == null);
        return forkJoinTask;
    }

    static {
        try {
            U = ForkJoinPool.getUnsafe();
            Class<ForkJoinPool> clazz = ForkJoinPool.class;
            CTL = U.objectFieldOffset(clazz.getDeclaredField("ctl"));
            STEALCOUNT = U.objectFieldOffset(clazz.getDeclaredField("stealCount"));
            PLOCK = U.objectFieldOffset(clazz.getDeclaredField("plock"));
            INDEXSEED = U.objectFieldOffset(clazz.getDeclaredField("indexSeed"));
            Class<Thread> clazz2 = Thread.class;
            PARKBLOCKER = U.objectFieldOffset(clazz2.getDeclaredField("parkBlocker"));
            Class<WorkQueue> clazz3 = WorkQueue.class;
            QBASE = U.objectFieldOffset(clazz3.getDeclaredField("base"));
            QLOCK = U.objectFieldOffset(clazz3.getDeclaredField("qlock"));
            Class<ForkJoinTask[]> clazz4 = ForkJoinTask[].class;
            ABASE = U.arrayBaseOffset(clazz4);
            int n = U.arrayIndexScale(clazz4);
            if ((n & n - 1) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(n);
        }
        catch (Exception exception) {
            throw new Error(exception);
        }
        submitters = new ThreadLocal();
        defaultForkJoinWorkerThreadFactory = new DefaultForkJoinWorkerThreadFactory();
        modifyThreadPermission = new RuntimePermission("modifyThread");
        common = AccessController.doPrivileged(new PrivilegedAction<ForkJoinPool>(){

            @Override
            public ForkJoinPool run() {
                return ForkJoinPool.makeCommonPool();
            }
        });
        short s = ForkJoinPool.common.parallelism;
        commonParallelism = s > 0 ? s : (short)1;
    }

    private WorkQueue findNonEmptyStealQueue() {
        int n;
        int n2 = ThreadLocalRandom.current().nextInt();
        do {
            int n3;
            n = this.plock;
            WorkQueue[] workQueueArray = this.workQueues;
            if (this.workQueues == null || (n3 = workQueueArray.length - 1) < 0) continue;
            for (int i = n3 + 1 << 2; i >= 0; --i) {
                WorkQueue workQueue = workQueueArray[(n2 - i << 1 | 1) & n3];
                if (workQueue == null || workQueue.base - workQueue.top >= 0) continue;
                return workQueue;
            }
        } while (this.plock != n);
        return null;
    }

    public boolean awaitQuiescence(long l, TimeUnit timeUnit) {
        long l2 = timeUnit.toNanos(l);
        Thread thread = Thread.currentThread();
        if (thread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread)thread;
            if (forkJoinWorkerThread.pool == this) {
                this.helpQuiescePool(forkJoinWorkerThread.workQueue);
                return true;
            }
        }
        long l3 = System.nanoTime();
        int n = 0;
        boolean bl = true;
        block0: while (!this.isQuiescent()) {
            int n2;
            WorkQueue[] workQueueArray = this.workQueues;
            if (this.workQueues == null || (n2 = workQueueArray.length - 1) < 0) break;
            if (!bl) {
                if (System.nanoTime() - l3 > l2) {
                    return false;
                }
                Thread.yield();
            }
            bl = false;
            for (int i = n2 + 1 << 2; i >= 0; --i) {
                int n3;
                WorkQueue workQueue;
                if ((workQueue = workQueueArray[n++ & n2]) == null || (n3 = workQueue.base) - workQueue.top >= 0) continue;
                bl = true;
                ForkJoinTask<?> forkJoinTask = workQueue.pollAt(n3);
                if (forkJoinTask == null) continue block0;
                forkJoinTask.doExec();
                continue block0;
            }
        }
        return true;
    }

    @Override
    public boolean isShutdown() {
        return this.plock < 0;
    }

    public <T> ForkJoinTask<T> submit(Runnable runnable, T t) {
        ForkJoinTask.AdaptedRunnable<T> adaptedRunnable = new ForkJoinTask.AdaptedRunnable<T>(runnable, t);
        this.externalPush(adaptedRunnable);
        return adaptedRunnable;
    }

    final void incrementActiveCount() {
        long l;
        while (!U.compareAndSwapLong(this, CTL, l = this.ctl, l & 0xFFFFFFFFFFFFL | (l & 0xFFFF000000000000L) + 0x1000000000000L)) {
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) {
        ArrayList<Future<T>> arrayList = new ArrayList<Future<T>>(collection.size());
        boolean bl = false;
        for (Callable<T> callable : collection) {
            ForkJoinTask.AdaptedCallable<T> adaptedCallable = new ForkJoinTask.AdaptedCallable<T>(callable);
            arrayList.add(adaptedCallable);
            this.externalPush(adaptedCallable);
        }
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            ((ForkJoinTask)arrayList.get(i)).quietlyJoin();
        }
        bl = true;
        ArrayList<Future<T>> arrayList2 = arrayList;
        if (!bl) {
            int n2 = arrayList.size();
            for (n = 0; n < n2; ++n) {
                arrayList.get(n).cancel(false);
            }
        }
        return arrayList2;
    }

    private final void helpRelease(long l, WorkQueue[] workQueueArray, WorkQueue workQueue, WorkQueue workQueue2, int n) {
        WorkQueue workQueue3;
        int n2;
        int n3;
        if (workQueue != null && workQueue.eventCount < 0 && (n3 = (int)l) > 0 && workQueueArray != null && workQueueArray.length > (n2 = n3 & 0xFFFF) && (workQueue3 = workQueueArray[n2]) != null && this.ctl == l) {
            long l2 = (long)(workQueue3.nextWait & Integer.MAX_VALUE) | (long)((int)(l >>> 32) + 65536) << 32;
            int n4 = n3 + 65536 & Integer.MAX_VALUE;
            if (workQueue2 != null && workQueue2.base == n && workQueue.eventCount < 0 && workQueue3.eventCount == (n3 | Integer.MIN_VALUE) && U.compareAndSwapLong(this, CTL, l, l2)) {
                workQueue3.eventCount = n4;
                Thread thread = workQueue3.parker;
                if (thread != null) {
                    U.unpark(thread);
                }
            }
        }
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

    public String toString() {
        int n;
        int n2;
        long l = 0L;
        long l2 = 0L;
        int n3 = 0;
        long l3 = this.stealCount;
        long l4 = this.ctl;
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            for (n2 = 0; n2 < workQueueArray.length; ++n2) {
                WorkQueue workQueue = workQueueArray[n2];
                if (workQueue == null) continue;
                n = workQueue.queueSize();
                if ((n2 & 1) == 0) {
                    l2 += (long)n;
                    continue;
                }
                l += (long)n;
                l3 += (long)workQueue.nsteals;
                if (!workQueue.isApparentlyUnblocked()) continue;
                ++n3;
            }
        }
        n2 = this.parallelism;
        n = n2 + (short)(l4 >>> 32);
        int n4 = n2 + (int)(l4 >> 48);
        if (n4 < 0) {
            n4 = 0;
        }
        String string = (l4 & 0x80000000L) != 0L ? (n == 0 ? "Terminated" : "Terminating") : (this.plock < 0 ? "Shutting down" : "Running");
        return super.toString() + "[" + string + ", parallelism = " + n2 + ", size = " + n + ", active = " + n4 + ", running = " + n3 + ", steals = " + l3 + ", tasks = " + l + ", submissions = " + l2 + "]";
    }

    private static void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(modifyThreadPermission);
        }
    }

    private void tryAddWorker() {
        int n;
        long l;
        int n2;
        while ((n2 = (int)((l = this.ctl) >>> 32)) < 0 && (n2 & 0x8000) != 0 && (n = (int)l) >= 0) {
            long l2 = (long)(n2 + 1 & 0xFFFF | n2 + 65536 & 0xFFFF0000) << 32 | (long)n;
            if (!U.compareAndSwapLong(this, CTL, l, l2)) continue;
            Throwable throwable = null;
            ForkJoinWorkerThread forkJoinWorkerThread = null;
            try {
                ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory = this.factory;
                if (forkJoinWorkerThreadFactory != null && (forkJoinWorkerThread = forkJoinWorkerThreadFactory.newThread(this)) != null) {
                    forkJoinWorkerThread.start();
                    break;
                }
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
            }
            this.deregisterWorker(forkJoinWorkerThread, throwable);
            break;
        }
    }

    public void execute(ForkJoinTask<?> forkJoinTask) {
        if (forkJoinTask == null) {
            throw new NullPointerException();
        }
        this.externalPush(forkJoinTask);
    }

    public <T> ForkJoinTask<T> submit(Callable<T> callable) {
        ForkJoinTask.AdaptedCallable<T> adaptedCallable = new ForkJoinTask.AdaptedCallable<T>(callable);
        this.externalPush(adaptedCallable);
        return adaptedCallable;
    }

    @Override
    public List<Runnable> shutdownNow() {
        ForkJoinPool.checkPermission();
        this.tryTerminate(true, true);
        return Collections.emptyList();
    }

    public static int getCommonPoolParallelism() {
        return commonParallelism;
    }

    public <T> ForkJoinTask<T> submit(ForkJoinTask<T> forkJoinTask) {
        if (forkJoinTask == null) {
            throw new NullPointerException();
        }
        this.externalPush(forkJoinTask);
        return forkJoinTask;
    }

    public int getPoolSize() {
        return this.parallelism + (short)(this.ctl >>> 32);
    }

    public ForkJoinWorkerThreadFactory getFactory() {
        return this.factory;
    }

    static int getSurplusQueuedTaskCount() {
        Thread thread = Thread.currentThread();
        if (thread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread)thread;
            ForkJoinPool forkJoinPool = forkJoinWorkerThread.pool;
            int n = forkJoinPool.parallelism;
            WorkQueue workQueue = forkJoinWorkerThread.workQueue;
            int n2 = workQueue.top - workQueue.base;
            int n3 = (int)(forkJoinPool.ctl >> 48) + n;
            return n2 - (n3 > (n >>>= 1) ? 0 : (n3 > (n >>>= 1) ? 1 : (n3 > (n >>>= 1) ? 2 : (n3 > (n >>>= 1) ? 4 : 8))));
        }
        return 0;
    }

    private static final synchronized int nextPoolId() {
        return ++poolNumberSequence;
    }

    private final int awaitWork(WorkQueue workQueue, long l, int n) {
        int n2 = workQueue.qlock;
        if (n2 >= 0 && workQueue.eventCount == n && this.ctl == l && !Thread.interrupted()) {
            int n3 = (int)l;
            int n4 = (int)(l >>> 32);
            int n5 = (n4 >> 16) + this.parallelism;
            if (n3 < 0 || n5 <= 0 && this.tryTerminate(false, false)) {
                workQueue.qlock = -1;
                n2 = -1;
            } else {
                int n6 = workQueue.nsteals;
                if (n6 != 0) {
                    long l2;
                    workQueue.nsteals = 0;
                    while (!U.compareAndSwapLong(this, STEALCOUNT, l2 = this.stealCount, l2 + (long)n6)) {
                    }
                } else {
                    long l3;
                    long l4;
                    long l5;
                    long l6 = l5 = n5 > 0 || n != (n3 | Integer.MIN_VALUE) ? 0L : (long)(workQueue.nextWait & Integer.MAX_VALUE) | (long)(n4 + 65536) << 32;
                    if (l5 != 0L) {
                        short s = -((short)(l >>> 32));
                        l4 = s < 0 ? 200000000L : (long)(s + 1) * 2000000000L;
                        l3 = System.nanoTime() + l4 - 2000000L;
                    } else {
                        l3 = 0L;
                        l4 = 0L;
                    }
                    if (workQueue.eventCount == n && this.ctl == l) {
                        Thread thread = Thread.currentThread();
                        U.putObject((Object)thread, PARKBLOCKER, (Object)this);
                        workQueue.parker = thread;
                        if (workQueue.eventCount == n && this.ctl == l) {
                            U.park(false, l4);
                        }
                        workQueue.parker = null;
                        U.putObject((Object)thread, PARKBLOCKER, null);
                        if (l4 != 0L && this.ctl == l && l3 - System.nanoTime() <= 0L && U.compareAndSwapLong(this, CTL, l, l5)) {
                            workQueue.qlock = -1;
                            n2 = -1;
                        }
                    }
                }
            }
        }
        return n2;
    }

    public int getActiveThreadCount() {
        int n = this.parallelism + (int)(this.ctl >> 48);
        return n <= 0 ? 0 : n;
    }

    @Override
    public boolean isTerminated() {
        long l = this.ctl;
        return (l & 0x80000000L) != 0L && (short)(l >>> 32) + this.parallelism <= 0;
    }

    final int externalHelpComplete(CountedCompleter<?> countedCompleter) {
        int n;
        WorkQueue workQueue;
        int n2;
        Submitter submitter = submitters.get();
        WorkQueue[] workQueueArray = this.workQueues;
        int n3 = 0;
        if (submitter != null && workQueueArray != null && (n2 = workQueueArray.length - 1) >= 0 && (workQueue = workQueueArray[(n = submitter.seed) & n2 & 0x7E]) != null && countedCompleter != null) {
            int n4 = n2 + n2 + 1;
            long l = 0L;
            n |= 1;
            int n5 = n4;
            while ((n3 = countedCompleter.status) >= 0) {
                if (workQueue.externalPopAndExecCC(countedCompleter)) {
                    n5 = n4;
                } else {
                    n3 = countedCompleter.status;
                    if (n3 < 0) break;
                    WorkQueue workQueue2 = workQueueArray[n & n2];
                    if (workQueue2 != null && workQueue2.pollAndExecCC(countedCompleter)) {
                        n5 = n4;
                    } else if (--n5 < 0) {
                        if (l == (l = this.ctl)) break;
                        n5 = n4;
                    }
                }
                n += 2;
            }
        }
        return n3;
    }

    private int helpComplete(WorkQueue workQueue, CountedCompleter<?> countedCompleter) {
        int n;
        int n2 = 0;
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null && (n = workQueueArray.length - 1) >= 0 && workQueue != null && countedCompleter != null) {
            int n3 = workQueue.poolIndex;
            int n4 = n + n + 1;
            long l = 0L;
            int n5 = n4;
            while ((n2 = countedCompleter.status) >= 0) {
                if (workQueue.internalPopAndExecCC(countedCompleter)) {
                    n5 = n4;
                } else {
                    n2 = countedCompleter.status;
                    if (n2 < 0) break;
                    WorkQueue workQueue2 = workQueueArray[n3 & n];
                    if (workQueue2 != null && workQueue2.pollAndExecCC(countedCompleter)) {
                        n5 = n4;
                    } else if (--n5 < 0) {
                        if (l == (l = this.ctl)) break;
                        n5 = n4;
                    }
                }
                n3 += 2;
            }
        }
        return n2;
    }

    public ForkJoinPool(int n) {
        this(n, defaultForkJoinWorkerThreadFactory, null, false);
    }

    private final int scan(WorkQueue workQueue, int n) {
        block6: {
            int n2;
            long l = this.ctl;
            WorkQueue[] workQueueArray = this.workQueues;
            if (this.workQueues == null || (n2 = workQueueArray.length - 1) < 0 || workQueue == null) break block6;
            int n3 = n2 + n2 + 1;
            int n4 = workQueue.eventCount;
            do {
                int n5;
                WorkQueue workQueue2;
                if ((workQueue2 = workQueueArray[n - n3 & n2]) == null || (n5 = workQueue2.base) - workQueue2.top >= 0) continue;
                ForkJoinTask<?>[] forkJoinTaskArray = workQueue2.array;
                if (workQueue2.array == null) continue;
                long l2 = ((forkJoinTaskArray.length - 1 & n5) << ASHIFT) + ABASE;
                ForkJoinTask forkJoinTask = (ForkJoinTask)U.getObjectVolatile(forkJoinTaskArray, l2);
                if (forkJoinTask == null) break block6;
                if (n4 < 0) {
                    this.helpRelease(l, workQueueArray, workQueue, workQueue2, n5);
                    break block6;
                }
                if (workQueue2.base != n5 || !U.compareAndSwapObject(forkJoinTaskArray, l2, forkJoinTask, null)) break block6;
                U.putOrderedInt(workQueue2, QBASE, n5 + 1);
                if (n5 + 1 - workQueue2.top < 0) {
                    this.signalWork(workQueueArray, workQueue2);
                }
                workQueue.runTask(forkJoinTask);
                break block6;
            } while (--n3 >= 0);
            int n6 = (int)l;
            if ((n4 | n6) < 0) {
                return this.awaitWork(workQueue, l, n4);
            }
            if (this.ctl == l) {
                long l3 = (long)n4 | l - 0x1000000000000L & 0xFFFFFFFF00000000L;
                workQueue.nextWait = n6;
                workQueue.eventCount = n4 | Integer.MIN_VALUE;
                if (!U.compareAndSwapLong(this, CTL, l, l3)) {
                    workQueue.eventCount = n4;
                }
            }
        }
        return 0;
    }

    final WorkQueue registerWorker(ForkJoinWorkerThread forkJoinWorkerThread) {
        int n;
        forkJoinWorkerThread.setDaemon(true);
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.ueh;
        if (uncaughtExceptionHandler != null) {
            forkJoinWorkerThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        do {
            n = this.indexSeed;
        } while (!U.compareAndSwapInt(this, INDEXSEED, n, n += 1640531527) || n == 0);
        WorkQueue workQueue = new WorkQueue(this, forkJoinWorkerThread, this.mode, n);
        int n2 = this.plock;
        if ((n2 & 2) != 0 || !U.compareAndSwapInt(this, PLOCK, n2, n2 += 2)) {
            n2 = this.acquirePlock();
        }
        int n3 = n2 & Integer.MIN_VALUE | n2 + 2 & Integer.MAX_VALUE;
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            int n4 = workQueueArray.length;
            int n5 = n4 - 1;
            int n6 = n << 1 | 1;
            if (workQueueArray[n6 &= n5] != null) {
                int n7;
                int n8 = 0;
                int n9 = n7 = n4 <= 4 ? 2 : (n4 >>> 1 & 0xFFFE) + 2;
                while (workQueueArray[n6 = n6 + n7 & n5] != null) {
                    if (++n8 < n4) continue;
                    this.workQueues = workQueueArray = Arrays.copyOf(workQueueArray, n4 <<= 1);
                    n5 = n4 - 1;
                    n8 = 0;
                }
            }
            workQueue.poolIndex = (short)n6;
            workQueue.eventCount = n6;
            workQueueArray[n6] = workQueue;
        }
        if (!U.compareAndSwapInt(this, PLOCK, n2, n3)) {
            this.releasePlock(n3);
        }
        forkJoinWorkerThread.setName(this.workerNamePrefix.concat(Integer.toString(workQueue.poolIndex >>> 1)));
        return workQueue;
    }

    public int getQueuedSubmissionCount() {
        int n = 0;
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            for (int i = 0; i < workQueueArray.length; i += 2) {
                WorkQueue workQueue = workQueueArray[i];
                if (workQueue == null) continue;
                n += workQueue.queueSize();
            }
        }
        return n;
    }

    final boolean tryCompensate(long l) {
        int n;
        WorkQueue[] workQueueArray = this.workQueues;
        short s = this.parallelism;
        int n2 = (int)l;
        if (workQueueArray != null && (n = workQueueArray.length - 1) >= 0 && n2 >= 0 && this.ctl == l) {
            WorkQueue workQueue = workQueueArray[n2 & n];
            if (n2 != 0 && workQueue != null) {
                long l2 = (long)(workQueue.nextWait & Integer.MAX_VALUE) | l & 0xFFFFFFFF00000000L;
                int n3 = n2 + 65536 & Integer.MAX_VALUE;
                if (workQueue.eventCount == (n2 | Integer.MIN_VALUE) && U.compareAndSwapLong(this, CTL, l, l2)) {
                    workQueue.eventCount = n3;
                    Thread thread = workQueue.parker;
                    if (thread != null) {
                        U.unpark(thread);
                    }
                    return true;
                }
            } else {
                long l3;
                short s2 = (short)(l >>> 32);
                if (s2 >= 0 && (int)(l >> 48) + s > 1) {
                    long l4 = l - 0x1000000000000L & 0xFFFF000000000000L | l & 0xFFFFFFFFFFFFL;
                    if (U.compareAndSwapLong(this, CTL, l, l4)) {
                        return true;
                    }
                } else if (s2 + s < Short.MAX_VALUE && U.compareAndSwapLong(this, CTL, l, l3 = l + 0x100000000L & 0xFFFF00000000L | l & 0xFFFF0000FFFFFFFFL)) {
                    Throwable throwable = null;
                    ForkJoinWorkerThread forkJoinWorkerThread = null;
                    try {
                        ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory = this.factory;
                        if (forkJoinWorkerThreadFactory != null && (forkJoinWorkerThread = forkJoinWorkerThreadFactory.newThread(this)) != null) {
                            forkJoinWorkerThread.start();
                            return true;
                        }
                    }
                    catch (Throwable throwable2) {
                        throwable = throwable2;
                    }
                    this.deregisterWorker(forkJoinWorkerThread, throwable);
                }
            }
        }
        return false;
    }

    public boolean hasQueuedSubmissions() {
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            for (int i = 0; i < workQueueArray.length; i += 2) {
                WorkQueue workQueue = workQueueArray[i];
                if (workQueue == null || workQueue.isEmpty()) continue;
                return true;
            }
        }
        return false;
    }

    public int getParallelism() {
        short s = this.parallelism;
        return s > 0 ? s : (short)1;
    }

    public long getStealCount() {
        long l = this.stealCount;
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            for (int i = 1; i < workQueueArray.length; i += 2) {
                WorkQueue workQueue = workQueueArray[i];
                if (workQueue == null) continue;
                l += (long)workQueue.nsteals;
            }
        }
        return l;
    }

    private void fullExternalPush(ForkJoinTask<?> forkJoinTask) {
        int n = 0;
        Submitter submitter = submitters.get();
        while (true) {
            int n2;
            int n3;
            int n4;
            int n5;
            WorkQueue[] workQueueArray;
            int n6;
            block20: {
                block19: {
                    if (submitter == null) {
                        n = this.indexSeed;
                        if (U.compareAndSwapInt(this, INDEXSEED, n, n += 1640531527) && n != 0) {
                            submitter = new Submitter(n);
                            submitters.set(submitter);
                        }
                    } else if (n == 0) {
                        n = submitter.seed;
                        n ^= n << 13;
                        n ^= n >>> 17;
                        n ^= n << 5;
                        submitter.seed = n;
                    }
                    if ((n6 = this.plock) < 0) {
                        throw new RejectedExecutionException();
                    }
                    if (n6 == 0) break block19;
                    workQueueArray = this.workQueues;
                    if (this.workQueues != null && (n5 = workQueueArray.length - 1) >= 0) break block20;
                }
                n3 = (n4 = this.parallelism) > 1 ? n4 - 1 : 1;
                n3 |= n3 >>> 1;
                n3 |= n3 >>> 2;
                n3 |= n3 >>> 4;
                n3 |= n3 >>> 8;
                n3 |= n3 >>> 16;
                n3 = n3 + 1 << 1;
                workQueueArray = this.workQueues;
                WorkQueue[] workQueueArray2 = this.workQueues == null || workQueueArray.length == 0 ? new WorkQueue[n3] : null;
                n6 = this.plock;
                if ((n6 & 2) != 0 || !U.compareAndSwapInt(this, PLOCK, n6, n6 += 2)) {
                    n6 = this.acquirePlock();
                }
                workQueueArray = this.workQueues;
                if ((this.workQueues == null || workQueueArray.length == 0) && workQueueArray2 != null) {
                    this.workQueues = workQueueArray2;
                }
                if (U.compareAndSwapInt(this, PLOCK, n6, n2 = n6 & Integer.MIN_VALUE | n6 + 2 & Integer.MAX_VALUE)) continue;
                this.releasePlock(n2);
                continue;
            }
            int n7 = n & n5 & 0x7E;
            WorkQueue workQueue = workQueueArray[n7];
            if (workQueue != null) {
                if (workQueue.qlock == 0 && U.compareAndSwapInt(workQueue, QLOCK, 0, 1)) {
                    ForkJoinTask<?>[] forkJoinTaskArray = workQueue.array;
                    n3 = workQueue.top;
                    boolean bl = false;
                    if (forkJoinTaskArray != null && forkJoinTaskArray.length > n3 + 1 - workQueue.base || (forkJoinTaskArray = workQueue.growArray()) != null) {
                        n2 = ((forkJoinTaskArray.length - 1 & n3) << ASHIFT) + ABASE;
                        U.putOrderedObject(forkJoinTaskArray, n2, forkJoinTask);
                        workQueue.top = n3 + 1;
                        bl = true;
                    }
                    workQueue.qlock = 0;
                    if (bl) {
                        this.signalWork(workQueueArray, workQueue);
                        return;
                    }
                }
                n = 0;
                continue;
            }
            n6 = this.plock;
            if ((n6 & 2) == 0) {
                workQueue = new WorkQueue(this, null, -1, n);
                workQueue.poolIndex = (short)n7;
                n6 = this.plock;
                if ((n6 & 2) != 0 || !U.compareAndSwapInt(this, PLOCK, n6, n6 += 2)) {
                    n6 = this.acquirePlock();
                }
                workQueueArray = this.workQueues;
                if (this.workQueues != null && n7 < workQueueArray.length && workQueueArray[n7] == null) {
                    workQueueArray[n7] = workQueue;
                }
                if (U.compareAndSwapInt(this, PLOCK, n6, n4 = n6 & Integer.MIN_VALUE | n6 + 2 & Integer.MAX_VALUE)) continue;
                this.releasePlock(n4);
                continue;
            }
            n = 0;
        }
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (this == common) {
            this.awaitQuiescence(l, timeUnit);
            return false;
        }
        long l2 = timeUnit.toNanos(l);
        if (this.isTerminated()) {
            return true;
        }
        if (l2 <= 0L) {
            return false;
        }
        long l3 = System.nanoTime() + l2;
        ForkJoinPool forkJoinPool = this;
        synchronized (forkJoinPool) {
            while (!this.isTerminated()) {
                if (l2 <= 0L) {
                    return false;
                }
                long l4 = TimeUnit.NANOSECONDS.toMillis(l2);
                this.wait(l4 > 0L ? l4 : 1L);
                l2 = l3 - System.nanoTime();
            }
            return true;
        }
    }

    public <T> T invoke(ForkJoinTask<T> forkJoinTask) {
        if (forkJoinTask == null) {
            throw new NullPointerException();
        }
        this.externalPush(forkJoinTask);
        return forkJoinTask.join();
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        ForkJoinTask forkJoinTask = runnable instanceof ForkJoinTask ? (ForkJoinTask)((Object)runnable) : new ForkJoinTask.RunnableExecuteAction(runnable);
        this.externalPush(forkJoinTask);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static WorkQueue commonSubmitterQueue() {
        Submitter submitter = submitters.get();
        if (submitter == null) return null;
        ForkJoinPool forkJoinPool = common;
        if (forkJoinPool == null) return null;
        WorkQueue[] workQueueArray = forkJoinPool.workQueues;
        if (forkJoinPool.workQueues == null) return null;
        int n = workQueueArray.length - 1;
        if (n < 0) return null;
        WorkQueue workQueue = workQueueArray[n & submitter.seed & 0x7E];
        return workQueue;
    }

    final void runWorker(WorkQueue workQueue) {
        workQueue.growArray();
        int n = workQueue.hint;
        while (this.scan(workQueue, n) == 0) {
            n ^= n << 13;
            n ^= n >>> 17;
            n ^= n << 5;
        }
    }

    final void externalPush(ForkJoinTask<?> forkJoinTask) {
        int n;
        WorkQueue workQueue;
        int n2;
        Submitter submitter = submitters.get();
        int n3 = this.plock;
        WorkQueue[] workQueueArray = this.workQueues;
        if (submitter != null && n3 > 0 && workQueueArray != null && (n2 = workQueueArray.length - 1) >= 0 && (workQueue = workQueueArray[n2 & (n = submitter.seed) & 0x7E]) != null && n != 0 && U.compareAndSwapInt(workQueue, QLOCK, 0, 1)) {
            int n4;
            int n5;
            int n6;
            ForkJoinTask<?>[] forkJoinTaskArray = workQueue.array;
            if (workQueue.array != null && (n6 = forkJoinTaskArray.length - 1) > (n5 = (n4 = workQueue.top) - workQueue.base)) {
                int n7 = ((n6 & n4) << ASHIFT) + ABASE;
                U.putOrderedObject(forkJoinTaskArray, n7, forkJoinTask);
                workQueue.top = n4 + 1;
                workQueue.qlock = 0;
                if (n5 <= 1) {
                    this.signalWork(workQueueArray, workQueue);
                }
                return;
            }
            workQueue.qlock = 0;
        }
        this.fullExternalPush(forkJoinTask);
    }

    private void releasePlock(int n) {
        this.plock = n;
        ForkJoinPool forkJoinPool = this;
        synchronized (forkJoinPool) {
            this.notifyAll();
        }
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T t) {
        return new ForkJoinTask.AdaptedRunnable<T>(runnable, t);
    }

    private int acquirePlock() {
        int n = 256;
        int n2;
        int n3;
        while (((n3 = this.plock) & 2) != 0 || !U.compareAndSwapInt(this, PLOCK, n3, n2 = n3 + 2)) {
            if (n >= 0) {
                if (ThreadLocalRandom.current().nextInt() < 0) continue;
                --n;
                continue;
            }
            if (!U.compareAndSwapInt(this, PLOCK, n3, n3 | 1)) continue;
            ForkJoinPool forkJoinPool = this;
            synchronized (forkJoinPool) {
                if ((this.plock & 1) != 0) {
                    try {
                        this.wait();
                    }
                    catch (InterruptedException interruptedException) {
                        try {
                            Thread.currentThread().interrupt();
                        }
                        catch (SecurityException securityException) {}
                    }
                } else {
                    this.notifyAll();
                }
            }
        }
        return n2;
    }

    public static ForkJoinPool commonPool() {
        return common;
    }

    protected int drainTasksTo(Collection<? super ForkJoinTask<?>> collection) {
        int n = 0;
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            for (int i = 0; i < workQueueArray.length; ++i) {
                ForkJoinTask<?> forkJoinTask;
                WorkQueue workQueue = workQueueArray[i];
                if (workQueue == null) continue;
                while ((forkJoinTask = workQueue.poll()) != null) {
                    collection.add(forkJoinTask);
                    ++n;
                }
            }
        }
        return n;
    }

    public boolean isTerminating() {
        long l = this.ctl;
        return (l & 0x80000000L) != 0L && (short)(l >>> 32) + this.parallelism > 0;
    }

    final boolean tryExternalUnpush(ForkJoinTask<?> forkJoinTask) {
        int n;
        WorkQueue workQueue;
        int n2;
        Submitter submitter = submitters.get();
        WorkQueue[] workQueueArray = this.workQueues;
        boolean bl = false;
        if (submitter != null && workQueueArray != null && (n2 = workQueueArray.length - 1) >= 0 && (workQueue = workQueueArray[submitter.seed & n2 & 0x7E]) != null && workQueue.base != (n = workQueue.top)) {
            long l;
            ForkJoinTask<?>[] forkJoinTaskArray = workQueue.array;
            if (workQueue.array != null && U.getObject(forkJoinTaskArray, l = (long)(((forkJoinTaskArray.length - 1 & n - 1) << ASHIFT) + ABASE)) == forkJoinTask && U.compareAndSwapInt(workQueue, QLOCK, 0, 1)) {
                if (workQueue.top == n && workQueue.array == forkJoinTaskArray && U.compareAndSwapObject(forkJoinTaskArray, l, forkJoinTask, null)) {
                    workQueue.top = n - 1;
                    bl = true;
                }
                workQueue.qlock = 0;
            }
        }
        return bl;
    }

    protected ForkJoinTask<?> pollSubmission() {
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            for (int i = 0; i < workQueueArray.length; i += 2) {
                ForkJoinTask<?> forkJoinTask;
                WorkQueue workQueue = workQueueArray[i];
                if (workQueue == null || (forkJoinTask = workQueue.poll()) == null) continue;
                return forkJoinTask;
            }
        }
        return null;
    }

    /*
     * Unable to fully structure code
     */
    private boolean tryTerminate(boolean var1_1, boolean var2_2) {
        if (this == ForkJoinPool.common) {
            return false;
        }
        var3_3 = this.plock;
        if (var3_3 >= 0) {
            if (!var2_2) {
                return false;
            }
            if ((var3_3 & 2) != 0 || !ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, var3_3, var3_3 += 2)) {
                var3_3 = this.acquirePlock();
            }
            if (!ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, var3_3, var4_4 = var3_3 + 2 & 0x7FFFFFFF | -2147483648)) {
                this.releasePlock(var4_4);
            }
        }
        block3: while (true) {
            if (((var4_5 = this.ctl) & 0x80000000L) != 0L) {
                if ((short)(var4_5 >>> 32) + this.parallelism <= 0) {
                    var6_6 = this;
                    synchronized (var6_6) {
                        this.notifyAll();
                    }
                }
                return true;
            }
            if (!var1_1) {
                if ((int)(var4_5 >> 48) + this.parallelism > 0) {
                    return false;
                }
                var6_7 = this.workQueues;
                if (this.workQueues != null) {
                    for (var8_10 = 0; var8_10 < var6_7.length; ++var8_10) {
                        var7_9 = var6_7[var8_10];
                        if (var7_9 == null || var7_9.isEmpty() && ((var8_10 & 1) == 0 || var7_9.eventCount < 0)) continue;
                        this.signalWork(var6_7, (WorkQueue)var7_9);
                        return false;
                    }
                }
            }
            if (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, var4_5, var4_5 | 0x80000000L)) continue;
            var6_8 = 0;
            while (true) {
                if (var6_8 < 3) ** break;
                continue block3;
                var7_9 = this.workQueues;
                if (this.workQueues != null) {
                    var10_14 = ((WorkQueue[])var7_9).length;
                    for (var11_15 = 0; var11_15 < var10_14; ++var11_15) {
                        var8_11 = var7_9[var11_15];
                        if (var8_11 == null) continue;
                        var8_11.qlock = -1;
                        if (var6_8 <= 0) continue;
                        var8_11.cancelAll();
                        if (var6_8 <= 1 || (var9_13 = var8_11.owner) == null) continue;
                        if (!var9_13.isInterrupted()) {
                            try {
                                var9_13.interrupt();
                            }
                            catch (Throwable var12_17) {
                                // empty catch block
                            }
                        }
                        ForkJoinPool.U.unpark(var9_13);
                    }
                    while ((var12_16 = (int)(var13_18 = this.ctl) & 0x7FFFFFFF) != 0 && (var11_15 = var12_16 & 65535) < var10_14 && var11_15 >= 0 && (var8_12 = var7_9[var11_15]) != null) {
                        var16_20 = (long)(var8_12.nextWait & 0x7FFFFFFF) | var13_18 + 0x1000000000000L & -281474976710656L | var13_18 & 0xFFFF80000000L;
                        if (var8_12.eventCount != (var12_16 | -2147483648) || !ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, var13_18, var16_20)) continue;
                        var8_12.eventCount = var12_16 + 65536 & 0x7FFFFFFF;
                        var8_12.qlock = -1;
                        var15_19 = var8_12.parker;
                        if (var15_19 == null) continue;
                        ForkJoinPool.U.unpark(var15_19);
                    }
                }
                ++var6_8;
            }
            break;
        }
    }

    private static ForkJoinWorkerThreadFactory checkFactory(ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory) {
        if (forkJoinWorkerThreadFactory == null) {
            throw new NullPointerException();
        }
        return forkJoinWorkerThreadFactory;
    }

    private ForkJoinPool(int n, ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, int n2, String string) {
        this.workerNamePrefix = string;
        this.factory = forkJoinWorkerThreadFactory;
        this.ueh = uncaughtExceptionHandler;
        this.mode = (short)n2;
        this.parallelism = (short)n;
        long l = -n;
        this.ctl = l << 48 & 0xFFFF000000000000L | l << 32 & 0xFFFF00000000L;
    }

    final void deregisterWorker(ForkJoinWorkerThread forkJoinWorkerThread, Throwable throwable) {
        long l;
        WorkQueue workQueue = null;
        if (forkJoinWorkerThread != null && (workQueue = forkJoinWorkerThread.workQueue) != null) {
            long l2;
            workQueue.qlock = -1;
            while (!U.compareAndSwapLong(this, STEALCOUNT, l2 = this.stealCount, l2 + (long)workQueue.nsteals)) {
            }
            int n = this.plock;
            if ((n & 2) != 0 || !U.compareAndSwapInt(this, PLOCK, n, n += 2)) {
                n = this.acquirePlock();
            }
            int n2 = n & Integer.MIN_VALUE | n + 2 & Integer.MAX_VALUE;
            short s = workQueue.poolIndex;
            WorkQueue[] workQueueArray = this.workQueues;
            if (workQueueArray != null && s >= 0 && s < workQueueArray.length && workQueueArray[s] == workQueue) {
                workQueueArray[s] = null;
            }
            if (!U.compareAndSwapInt(this, PLOCK, n, n2)) {
                this.releasePlock(n2);
            }
        }
        while (!U.compareAndSwapLong(this, CTL, l = this.ctl, l - 0x1000000000000L & 0xFFFF000000000000L | l - 0x100000000L & 0xFFFF00000000L | l & 0xFFFFFFFFL)) {
        }
        if (!this.tryTerminate(false, false) && workQueue != null && workQueue.array != null) {
            int n;
            int n3;
            workQueue.cancelAll();
            while ((n3 = (int)((l = this.ctl) >>> 32)) < 0 && (n = (int)l) >= 0) {
                if (n > 0) {
                    WorkQueue workQueue2;
                    int n4;
                    WorkQueue[] workQueueArray = this.workQueues;
                    if (this.workQueues == null || (n4 = n & 0xFFFF) >= workQueueArray.length || (workQueue2 = workQueueArray[n4]) == null) break;
                    long l3 = (long)(workQueue2.nextWait & Integer.MAX_VALUE) | (long)(n3 + 65536) << 32;
                    if (workQueue2.eventCount != (n | Integer.MIN_VALUE)) break;
                    if (!U.compareAndSwapLong(this, CTL, l, l3)) continue;
                    workQueue2.eventCount = n + 65536 & Integer.MAX_VALUE;
                    Thread thread = workQueue2.parker;
                    if (thread == null) break;
                    U.unpark(thread);
                    break;
                }
                if ((short)n3 >= 0) break;
                this.tryAddWorker();
                break;
            }
        }
        if (throwable == null) {
            ForkJoinTask.helpExpungeStaleExceptions();
        } else {
            ForkJoinTask.rethrow(throwable);
        }
    }

    final void helpJoinOnce(WorkQueue workQueue, ForkJoinTask<?> forkJoinTask) {
        int n;
        if (workQueue != null && forkJoinTask != null && (n = forkJoinTask.status) >= 0) {
            ForkJoinTask<?> forkJoinTask2 = workQueue.currentJoin;
            workQueue.currentJoin = forkJoinTask;
            while (workQueue.tryRemoveAndExec(forkJoinTask) && (n = forkJoinTask.status) >= 0) {
            }
            if (n >= 0) {
                if (forkJoinTask instanceof CountedCompleter) {
                    this.helpComplete(workQueue, (CountedCompleter)forkJoinTask);
                }
                while (forkJoinTask.status >= 0 && this.tryHelpStealer(workQueue, forkJoinTask) > 0) {
                }
            }
            workQueue.currentJoin = forkJoinTask2;
        }
    }

    public long getQueuedTaskCount() {
        long l = 0L;
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            for (int i = 1; i < workQueueArray.length; i += 2) {
                WorkQueue workQueue = workQueueArray[i];
                if (workQueue == null) continue;
                l += (long)workQueue.queueSize();
            }
        }
        return l;
    }

    /*
     * Unable to fully structure code
     */
    private int tryHelpStealer(WorkQueue var1_1, ForkJoinTask<?> var2_2) {
        var3_3 = 0;
        var4_4 = 0;
        if (var2_2 != null && var1_1 != null && var1_1.base - var1_1.top >= 0) {
            block0: while (true) {
                var5_5 = var2_2;
                var6_6 = var1_1;
                while (true) {
                    block9: {
                        block8: {
                            if ((var10_10 = var2_2.status) < 0) {
                                var3_3 = var10_10;
                                break block0;
                            }
                            var8_8 = this.workQueues;
                            if (this.workQueues == null || (var9_9 = var8_8.length - 1) <= 0) break block0;
                            var11_11 = (var6_6.hint | 1) & var9_9;
                            var7_7 = var8_8[var11_11];
                            if (var7_7 == null || var7_7.currentSteal != var5_5) {
                                var12_12 = var11_11;
                                do {
                                    if (((var11_11 = var11_11 + 2 & var9_9) & 15) == 1 && (var5_5.status < 0 || var6_6.currentJoin != var5_5)) continue block0;
                                    var7_7 = var8_8[var11_11];
                                    if (var7_7 == null || var7_7.currentSteal != var5_5) continue;
                                    var6_6.hint = var11_11;
                                    break block8;
                                } while (var11_11 != var12_12);
                                break block0;
                            }
                        }
                        while (true) {
                            if (var5_5.status < 0) continue block0;
                            var13_14 = var7_7.base;
                            if (var13_14 - var7_7.top >= 0) break block9;
                            var12_13 = var7_7.array;
                            if (var7_7.array == null) break block9;
                            var14_15 = ((var12_13.length - 1 & var13_14) << ForkJoinPool.ASHIFT) + ForkJoinPool.ABASE;
                            var15_17 = (ForkJoinTask<?>)ForkJoinPool.U.getObjectVolatile(var12_13, var14_15);
                            if (var5_5.status < 0 || var6_6.currentJoin != var5_5 || var7_7.currentSteal != var5_5) continue block0;
                            var3_3 = 1;
                            if (var7_7.base != var13_14) continue;
                            if (var15_17 == null) break block0;
                            if (ForkJoinPool.U.compareAndSwapObject(var12_13, var14_15, var15_17, null)) break;
                        }
                        ForkJoinPool.U.putOrderedInt(var7_7, ForkJoinPool.QBASE, var13_14 + 1);
                        var16_18 = var1_1.currentSteal;
                        var17_19 = var1_1.top;
                        do {
                            var1_1.currentSteal = var15_17;
                            var15_17.doExec();
                        } while (var2_2.status >= 0 && var1_1.top != var17_19 && (var15_17 = var1_1.pop()) != null);
                        var1_1.currentSteal = var16_18;
                        break block0;
                    }
                    var14_16 = var7_7.currentJoin;
                    if (var5_5.status >= 0 && var6_6.currentJoin == var5_5 && var7_7.currentSteal == var5_5) ** break;
                    continue block0;
                    if (var14_16 == null || ++var4_4 == 64) break block0;
                    var5_5 = var14_16;
                    var6_6 = var7_7;
                }
                break;
            }
        }
        return var3_3;
    }

    @Override
    public void shutdown() {
        ForkJoinPool.checkPermission();
        this.tryTerminate(false, true);
    }

    final int awaitJoin(WorkQueue workQueue, ForkJoinTask<?> forkJoinTask) {
        int n = 0;
        if (forkJoinTask != null && (n = forkJoinTask.status) >= 0 && workQueue != null) {
            ForkJoinTask<?> forkJoinTask2 = workQueue.currentJoin;
            workQueue.currentJoin = forkJoinTask;
            while (workQueue.tryRemoveAndExec(forkJoinTask) && (n = forkJoinTask.status) >= 0) {
            }
            if (n >= 0 && forkJoinTask instanceof CountedCompleter) {
                n = this.helpComplete(workQueue, (CountedCompleter)forkJoinTask);
            }
            long l = 0L;
            while (n >= 0 && (n = forkJoinTask.status) >= 0) {
                long l2;
                n = this.tryHelpStealer(workQueue, forkJoinTask);
                if (n != 0 || (n = forkJoinTask.status) < 0) continue;
                if (!this.tryCompensate(l)) {
                    l = this.ctl;
                    continue;
                }
                if (forkJoinTask.trySetSignal() && (n = forkJoinTask.status) >= 0) {
                    ForkJoinTask<?> forkJoinTask3 = forkJoinTask;
                    synchronized (forkJoinTask3) {
                        if (forkJoinTask.status >= 0) {
                            try {
                                forkJoinTask.wait();
                            }
                            catch (InterruptedException interruptedException) {}
                        } else {
                            forkJoinTask.notifyAll();
                        }
                    }
                }
                while (!U.compareAndSwapLong(this, CTL, l2 = this.ctl, l2 & 0xFFFFFFFFFFFFL | (l2 & 0xFFFF000000000000L) + 0x1000000000000L)) {
                }
            }
            workQueue.currentJoin = forkJoinTask2;
        }
        return n;
    }

    private static int checkParallelism(int n) {
        if (n <= 0 || n > Short.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        return n;
    }

    public static void managedBlock(ManagedBlocker managedBlocker) throws InterruptedException {
        Thread thread = Thread.currentThread();
        if (thread instanceof ForkJoinWorkerThread) {
            ForkJoinPool forkJoinPool = ((ForkJoinWorkerThread)thread).pool;
            while (!managedBlocker.isReleasable()) {
                if (!forkJoinPool.tryCompensate(forkJoinPool.ctl)) continue;
                while (!managedBlocker.isReleasable() && !managedBlocker.block()) {
                }
                forkJoinPool.incrementActiveCount();
                break;
            }
        } else {
            while (!managedBlocker.isReleasable() && !managedBlocker.block()) {
            }
        }
    }

    static void quiesceCommonPool() {
        common.awaitQuiescence(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    final void signalWork(WorkQueue[] workQueueArray, WorkQueue workQueue) {
        long l;
        int n;
        while ((n = (int)((l = this.ctl) >>> 32)) < 0) {
            WorkQueue workQueue2;
            int n2;
            int n3 = (int)l;
            if (n3 <= 0) {
                if ((short)n >= 0) break;
                this.tryAddWorker();
                break;
            }
            if (workQueueArray == null || workQueueArray.length <= (n2 = n3 & 0xFFFF) || (workQueue2 = workQueueArray[n2]) == null) break;
            long l2 = (long)(workQueue2.nextWait & Integer.MAX_VALUE) | (long)(n + 65536) << 32;
            int n4 = n3 + 65536 & Integer.MAX_VALUE;
            if (workQueue2.eventCount == (n3 | Integer.MIN_VALUE) && U.compareAndSwapLong(this, CTL, l, l2)) {
                workQueue2.eventCount = n4;
                Thread thread = workQueue2.parker;
                if (thread == null) break;
                U.unpark(thread);
                break;
            }
            if (workQueue == null || workQueue.base < workQueue.top) continue;
            break;
        }
    }

    public ForkJoinTask<?> submit(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        ForkJoinTask forkJoinTask = runnable instanceof ForkJoinTask ? (ForkJoinTask)((Object)runnable) : new ForkJoinTask.AdaptedRunnableAction(runnable);
        this.externalPush(forkJoinTask);
        return forkJoinTask;
    }

    public boolean getAsyncMode() {
        return this.mode == 1;
    }

    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.ueh;
    }

    public ForkJoinPool() {
        this(Math.min(Short.MAX_VALUE, Runtime.getRuntime().availableProcessors()), defaultForkJoinWorkerThreadFactory, null, false);
    }

    public ForkJoinPool(int n, ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, boolean bl) {
        this(ForkJoinPool.checkParallelism(n), ForkJoinPool.checkFactory(forkJoinWorkerThreadFactory), uncaughtExceptionHandler, bl ? 1 : 0, "ForkJoinPool-" + ForkJoinPool.nextPoolId() + "-worker-");
        ForkJoinPool.checkPermission();
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new ForkJoinTask.AdaptedCallable<T>(callable);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    final void helpQuiescePool(WorkQueue workQueue) {
        ForkJoinTask<?> forkJoinTask = workQueue.currentSteal;
        boolean bl = true;
        while (true) {
            long l;
            ForkJoinTask<?> forkJoinTask2;
            if ((forkJoinTask2 = workQueue.nextLocalTask()) != null) {
                forkJoinTask2.doExec();
                continue;
            }
            WorkQueue workQueue2 = this.findNonEmptyStealQueue();
            if (workQueue2 != null) {
                int n;
                if (!bl) {
                    bl = true;
                    while (!U.compareAndSwapLong(this, CTL, l = this.ctl, l & 0xFFFFFFFFFFFFL | (l & 0xFFFF000000000000L) + 0x1000000000000L)) {
                    }
                }
                if ((n = workQueue2.base) - workQueue2.top >= 0 || (forkJoinTask2 = workQueue2.pollAt(n)) == null) continue;
                workQueue.currentSteal = forkJoinTask2;
                workQueue.currentSteal.doExec();
                workQueue.currentSteal = forkJoinTask;
                continue;
            }
            if (bl) {
                l = this.ctl;
                long l2 = l & 0xFFFFFFFFFFFFL | (l & 0xFFFF000000000000L) - 0x1000000000000L;
                if ((int)(l2 >> 48) + this.parallelism == 0) return;
                if (!U.compareAndSwapLong(this, CTL, l, l2)) continue;
                bl = false;
                continue;
            }
            l = this.ctl;
            if ((int)(l >> 48) + this.parallelism <= 0 && U.compareAndSwapLong(this, CTL, l, l & 0xFFFFFFFFFFFFL | (l & 0xFFFF000000000000L) + 0x1000000000000L)) return;
        }
    }

    public boolean isQuiescent() {
        return this.parallelism + (int)(this.ctl >> 48) <= 0;
    }

    private static ForkJoinPool makeCommonPool() {
        int n = -1;
        ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory = defaultForkJoinWorkerThreadFactory;
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
        try {
            String string = System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism");
            String string2 = System.getProperty("java.util.concurrent.ForkJoinPool.common.threadFactory");
            String string3 = System.getProperty("java.util.concurrent.ForkJoinPool.common.exceptionHandler");
            if (string != null) {
                n = Integer.parseInt(string);
            }
            if (string2 != null) {
                forkJoinWorkerThreadFactory = (ForkJoinWorkerThreadFactory)ClassLoader.getSystemClassLoader().loadClass(string2).newInstance();
            }
            if (string3 != null) {
                uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler)ClassLoader.getSystemClassLoader().loadClass(string3).newInstance();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (n < 0 && (n = Runtime.getRuntime().availableProcessors() - 1) < 0) {
            n = 0;
        }
        if (n > Short.MAX_VALUE) {
            n = Short.MAX_VALUE;
        }
        return new ForkJoinPool(n, forkJoinWorkerThreadFactory, uncaughtExceptionHandler, 0, "ForkJoinPool.commonPool-worker-");
    }

    public int getRunningThreadCount() {
        int n = 0;
        WorkQueue[] workQueueArray = this.workQueues;
        if (this.workQueues != null) {
            for (int i = 1; i < workQueueArray.length; i += 2) {
                WorkQueue workQueue = workQueueArray[i];
                if (workQueue == null || !workQueue.isApparentlyUnblocked()) continue;
                ++n;
            }
        }
        return n;
    }

    static final class EmptyTask
    extends ForkJoinTask<Void> {
        private static final long serialVersionUID = -7721805057305804111L;

        @Override
        public final Void getRawResult() {
            return null;
        }

        EmptyTask() {
            this.status = -268435456;
        }

        @Override
        public final boolean exec() {
            return true;
        }

        @Override
        public final void setRawResult(Void void_) {
        }
    }

    public static interface ManagedBlocker {
        public boolean block() throws InterruptedException;

        public boolean isReleasable();
    }

    public static interface ForkJoinWorkerThreadFactory {
        public ForkJoinWorkerThread newThread(ForkJoinPool var1);
    }

    static final class Submitter {
        int seed;

        Submitter(int n) {
            this.seed = n;
        }
    }

    static final class DefaultForkJoinWorkerThreadFactory
    implements ForkJoinWorkerThreadFactory {
        DefaultForkJoinWorkerThreadFactory() {
        }

        @Override
        public final ForkJoinWorkerThread newThread(ForkJoinPool forkJoinPool) {
            return new ForkJoinWorkerThread(forkJoinPool);
        }
    }

    static final class WorkQueue {
        int top;
        volatile Object pad17;
        volatile Object pad13;
        short poolIndex;
        final short mode;
        ForkJoinTask<?>[] array;
        volatile int base;
        volatile Thread parker;
        volatile Object pad1b;
        volatile Object pad19;
        private static final long QBASE;
        private static final int ABASE;
        volatile Object pad12;
        volatile ForkJoinTask<?> currentJoin;
        volatile Object pad15;
        int hint;
        private static final int ASHIFT;
        volatile long pad04;
        volatile Object pad1d;
        volatile Object pad1a;
        volatile Object pad14;
        volatile Object pad11;
        volatile Object pad16;
        int nextWait;
        volatile long pad06;
        volatile int qlock;
        ForkJoinTask<?> currentSteal;
        volatile long pad05;
        static final int MAXIMUM_QUEUE_CAPACITY = 0x4000000;
        final ForkJoinWorkerThread owner;
        final ForkJoinPool pool;
        volatile int eventCount;
        static final int INITIAL_QUEUE_CAPACITY = 8192;
        volatile long pad01;
        volatile long pad03;
        volatile long pad02;
        private static final long QLOCK;
        volatile Object pad18;
        private static final Unsafe U;
        volatile Object pad10;
        volatile Object pad1c;
        volatile long pad00;
        int nsteals;

        final boolean internalPopAndExecCC(CountedCompleter<?> countedCompleter) {
            block3: {
                long l;
                Object object;
                int n = this.top;
                if (this.base - n >= 0) break block3;
                ForkJoinTask<?>[] forkJoinTaskArray = this.array;
                if (this.array != null && (object = U.getObject(forkJoinTaskArray, l = (long)(((forkJoinTaskArray.length - 1 & n - 1) << ASHIFT) + ABASE))) instanceof CountedCompleter) {
                    CountedCompleter<?> countedCompleter2;
                    CountedCompleter<?> countedCompleter3 = countedCompleter2 = (CountedCompleter<?>)object;
                    do {
                        if (countedCompleter3 != countedCompleter) continue;
                        if (U.compareAndSwapObject(forkJoinTaskArray, l, countedCompleter2, null)) {
                            this.top = n - 1;
                            countedCompleter2.doExec();
                        }
                        return true;
                    } while ((countedCompleter3 = countedCompleter3.completer) != null);
                }
            }
            return false;
        }

        final ForkJoinTask<?> poll() {
            int n;
            while ((n = this.base) - this.top < 0) {
                ForkJoinTask<?>[] forkJoinTaskArray = this.array;
                if (this.array == null) break;
                int n2 = ((forkJoinTaskArray.length - 1 & n) << ASHIFT) + ABASE;
                ForkJoinTask forkJoinTask = (ForkJoinTask)U.getObjectVolatile(forkJoinTaskArray, n2);
                if (forkJoinTask != null) {
                    if (!U.compareAndSwapObject(forkJoinTaskArray, n2, forkJoinTask, null)) continue;
                    U.putOrderedInt(this, QBASE, n + 1);
                    return forkJoinTask;
                }
                if (this.base != n) continue;
                if (n + 1 == this.top) break;
                Thread.yield();
            }
            return null;
        }

        static {
            try {
                U = ForkJoinPool.getUnsafe();
                Class<WorkQueue> clazz = WorkQueue.class;
                Class<ForkJoinTask[]> clazz2 = ForkJoinTask[].class;
                QBASE = U.objectFieldOffset(clazz.getDeclaredField("base"));
                QLOCK = U.objectFieldOffset(clazz.getDeclaredField("qlock"));
                ABASE = U.arrayBaseOffset(clazz2);
                int n = U.arrayIndexScale(clazz2);
                if ((n & n - 1) != 0) {
                    throw new Error("data type scale not a power of two");
                }
                ASHIFT = 31 - Integer.numberOfLeadingZeros(n);
            }
            catch (Exception exception) {
                throw new Error(exception);
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        final boolean tryRemoveAndExec(ForkJoinTask<?> forkJoinTask) {
            long l;
            ForkJoinTask forkJoinTask2;
            if (forkJoinTask == null) return false;
            ForkJoinTask<?>[] forkJoinTaskArray = this.array;
            if (this.array == null) return false;
            int n = forkJoinTaskArray.length - 1;
            if (n < 0) return false;
            int n2 = this.top;
            int n3 = this.base;
            int n4 = n2 - n3;
            if (n4 <= 0) return false;
            boolean bl = false;
            boolean bl2 = true;
            boolean bl3 = true;
            while ((forkJoinTask2 = (ForkJoinTask)U.getObject(forkJoinTaskArray, l = (long)(((--n2 & n) << ASHIFT) + ABASE))) != null) {
                if (forkJoinTask2 == forkJoinTask) {
                    if (n2 + 1 == this.top) {
                        if (!U.compareAndSwapObject(forkJoinTaskArray, l, forkJoinTask, null)) break;
                        this.top = n2;
                        bl = true;
                        break;
                    }
                    if (this.base != n3) break;
                    bl = U.compareAndSwapObject(forkJoinTaskArray, l, forkJoinTask, new EmptyTask());
                    break;
                }
                if (forkJoinTask2.status >= 0) {
                    bl2 = false;
                } else if (n2 + 1 == this.top) {
                    if (!U.compareAndSwapObject(forkJoinTaskArray, l, forkJoinTask2, null)) break;
                    this.top = n2;
                    break;
                }
                if (--n4 != 0) continue;
                if (bl2 || this.base != n3) break;
                bl3 = false;
                break;
            }
            if (!bl) return bl3;
            forkJoinTask.doExec();
            return bl3;
        }

        final boolean tryUnpush(ForkJoinTask<?> forkJoinTask) {
            int n;
            ForkJoinTask<?>[] forkJoinTaskArray = this.array;
            if (this.array != null && (n = this.top) != this.base && U.compareAndSwapObject(forkJoinTaskArray, ((forkJoinTaskArray.length - 1 & --n) << ASHIFT) + ABASE, forkJoinTask, null)) {
                this.top = n;
                return true;
            }
            return false;
        }

        final void pollAndExecAll() {
            ForkJoinTask<?> forkJoinTask;
            while ((forkJoinTask = this.poll()) != null) {
                forkJoinTask.doExec();
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        final boolean isEmpty() {
            int n = this.top;
            int n2 = this.base - n;
            if (n2 >= 0) return true;
            if (n2 != -1) return false;
            ForkJoinTask<?>[] forkJoinTaskArray = this.array;
            if (this.array == null) return true;
            int n3 = forkJoinTaskArray.length - 1;
            if (n3 < 0) return true;
            if (U.getObject(forkJoinTaskArray, (long)((n3 & n - 1) << ASHIFT) + (long)ABASE) != null) return false;
            return true;
        }

        final void cancelAll() {
            ForkJoinTask<?> forkJoinTask;
            ForkJoinTask.cancelIgnoringExceptions(this.currentJoin);
            ForkJoinTask.cancelIgnoringExceptions(this.currentSteal);
            while ((forkJoinTask = this.poll()) != null) {
                ForkJoinTask.cancelIgnoringExceptions(forkJoinTask);
            }
        }

        final ForkJoinTask<?>[] growArray() {
            int n;
            int n2;
            int n3;
            int n4;
            ForkJoinTask<?>[] forkJoinTaskArray = this.array;
            int n5 = n4 = forkJoinTaskArray != null ? forkJoinTaskArray.length << 1 : 8192;
            if (n4 > 0x4000000) {
                throw new RejectedExecutionException("Queue capacity exceeded");
            }
            this.array = new ForkJoinTask[n4];
            ForkJoinTask[] forkJoinTaskArray2 = this.array;
            if (forkJoinTaskArray != null && (n3 = forkJoinTaskArray.length - 1) >= 0 && (n2 = this.top) - (n = this.base) > 0) {
                int n6 = n4 - 1;
                do {
                    int n7 = ((n & n3) << ASHIFT) + ABASE;
                    int n8 = ((n & n6) << ASHIFT) + ABASE;
                    ForkJoinTask forkJoinTask = (ForkJoinTask)U.getObjectVolatile(forkJoinTaskArray, n7);
                    if (forkJoinTask == null || !U.compareAndSwapObject(forkJoinTaskArray, n7, forkJoinTask, null)) continue;
                    U.putObjectVolatile(forkJoinTaskArray2, n8, forkJoinTask);
                } while (++n != n2);
            }
            return forkJoinTaskArray2;
        }

        final boolean externalPopAndExecCC(CountedCompleter<?> countedCompleter) {
            block5: {
                long l;
                Object object;
                int n = this.top;
                if (this.base - n >= 0) break block5;
                ForkJoinTask<?>[] forkJoinTaskArray = this.array;
                if (this.array != null && (object = U.getObject(forkJoinTaskArray, l = (long)(((forkJoinTaskArray.length - 1 & n - 1) << ASHIFT) + ABASE))) instanceof CountedCompleter) {
                    CountedCompleter<?> countedCompleter2;
                    CountedCompleter<?> countedCompleter3 = countedCompleter2 = (CountedCompleter<?>)object;
                    do {
                        if (countedCompleter3 != countedCompleter) continue;
                        if (U.compareAndSwapInt(this, QLOCK, 0, 1)) {
                            if (this.top == n && this.array == forkJoinTaskArray && U.compareAndSwapObject(forkJoinTaskArray, l, countedCompleter2, null)) {
                                this.top = n - 1;
                                this.qlock = 0;
                                countedCompleter2.doExec();
                            } else {
                                this.qlock = 0;
                            }
                        }
                        return true;
                    } while ((countedCompleter3 = countedCompleter3.completer) != null);
                }
            }
            return false;
        }

        final ForkJoinTask<?> nextLocalTask() {
            return this.mode == 0 ? this.pop() : this.poll();
        }

        final int queueSize() {
            int n = this.base - this.top;
            return n >= 0 ? 0 : -n;
        }

        final void push(ForkJoinTask<?> forkJoinTask) {
            int n = this.top;
            ForkJoinTask<?>[] forkJoinTaskArray = this.array;
            if (this.array != null) {
                int n2 = forkJoinTaskArray.length - 1;
                U.putOrderedObject(forkJoinTaskArray, ((n2 & n) << ASHIFT) + ABASE, forkJoinTask);
                this.top = n + 1;
                int n3 = this.top - this.base;
                if (n3 <= 2) {
                    ForkJoinPool forkJoinPool = this.pool;
                    forkJoinPool.signalWork(forkJoinPool.workQueues, this);
                } else if (n3 >= n2) {
                    this.growArray();
                }
            }
        }

        WorkQueue(ForkJoinPool forkJoinPool, ForkJoinWorkerThread forkJoinWorkerThread, int n, int n2) {
            this.pool = forkJoinPool;
            this.owner = forkJoinWorkerThread;
            this.mode = (short)n;
            this.hint = n2;
            this.top = 4096;
            this.base = 4096;
        }

        final boolean pollAndExecCC(CountedCompleter<?> countedCompleter) {
            block5: {
                int n = this.base;
                if (n - this.top >= 0) break block5;
                ForkJoinTask<?>[] forkJoinTaskArray = this.array;
                if (this.array != null) {
                    long l = ((forkJoinTaskArray.length - 1 & n) << ASHIFT) + ABASE;
                    Object object = U.getObjectVolatile(forkJoinTaskArray, l);
                    if (object == null) {
                        return true;
                    }
                    if (object instanceof CountedCompleter) {
                        CountedCompleter<?> countedCompleter2;
                        CountedCompleter<?> countedCompleter3 = countedCompleter2 = (CountedCompleter<?>)object;
                        do {
                            if (countedCompleter3 != countedCompleter) continue;
                            if (this.base == n && U.compareAndSwapObject(forkJoinTaskArray, l, countedCompleter2, null)) {
                                U.putOrderedInt(this, QBASE, n + 1);
                                countedCompleter2.doExec();
                            }
                            return true;
                        } while ((countedCompleter3 = countedCompleter3.completer) != null);
                    }
                }
            }
            return false;
        }

        final ForkJoinTask<?> peek() {
            int n;
            ForkJoinTask<?>[] forkJoinTaskArray = this.array;
            if (forkJoinTaskArray == null || (n = forkJoinTaskArray.length - 1) < 0) {
                return null;
            }
            int n2 = this.mode == 0 ? this.top - 1 : this.base;
            int n3 = ((n2 & n) << ASHIFT) + ABASE;
            return (ForkJoinTask)U.getObjectVolatile(forkJoinTaskArray, n3);
        }

        final ForkJoinTask<?> pop() {
            int n;
            ForkJoinTask<?>[] forkJoinTaskArray = this.array;
            if (this.array != null && (n = forkJoinTaskArray.length - 1) >= 0) {
                long l;
                ForkJoinTask forkJoinTask;
                int n2;
                while ((n2 = this.top - 1) - this.base >= 0 && (forkJoinTask = (ForkJoinTask)U.getObject(forkJoinTaskArray, l = (long)(((n & n2) << ASHIFT) + ABASE))) != null) {
                    if (!U.compareAndSwapObject(forkJoinTaskArray, l, forkJoinTask, null)) continue;
                    this.top = n2;
                    return forkJoinTask;
                }
            }
            return null;
        }

        final void runTask(ForkJoinTask<?> forkJoinTask) {
            this.currentSteal = forkJoinTask;
            if (this.currentSteal != null) {
                forkJoinTask.doExec();
                ForkJoinTask<?>[] forkJoinTaskArray = this.array;
                short s = this.mode;
                ++this.nsteals;
                this.currentSteal = null;
                if (s != 0) {
                    this.pollAndExecAll();
                } else if (forkJoinTaskArray != null) {
                    long l;
                    ForkJoinTask forkJoinTask2;
                    int n;
                    int n2 = forkJoinTaskArray.length - 1;
                    while ((n = this.top - 1) - this.base >= 0 && (forkJoinTask2 = (ForkJoinTask)U.getObject(forkJoinTaskArray, l = (long)(((n2 & n) << ASHIFT) + ABASE))) != null) {
                        if (!U.compareAndSwapObject(forkJoinTaskArray, l, forkJoinTask2, null)) continue;
                        this.top = n;
                        forkJoinTask2.doExec();
                    }
                }
            }
        }

        final boolean isApparentlyUnblocked() {
            Thread.State state;
            ForkJoinWorkerThread forkJoinWorkerThread;
            return this.eventCount >= 0 && (forkJoinWorkerThread = this.owner) != null && (state = forkJoinWorkerThread.getState()) != Thread.State.BLOCKED && state != Thread.State.WAITING && state != Thread.State.TIMED_WAITING;
        }

        final ForkJoinTask<?> pollAt(int n) {
            int n2;
            ForkJoinTask forkJoinTask;
            ForkJoinTask<?>[] forkJoinTaskArray = this.array;
            if (this.array != null && (forkJoinTask = (ForkJoinTask)U.getObjectVolatile(forkJoinTaskArray, n2 = ((forkJoinTaskArray.length - 1 & n) << ASHIFT) + ABASE)) != null && this.base == n && U.compareAndSwapObject(forkJoinTaskArray, n2, forkJoinTask, null)) {
                U.putOrderedInt(this, QBASE, n + 1);
                return forkJoinTask;
            }
            return null;
        }
    }
}

