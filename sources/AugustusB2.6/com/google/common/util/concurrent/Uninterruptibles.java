// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Verify;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.Semaphore;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.google.common.base.Preconditions;
import java.util.concurrent.locks.Condition;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.CountDownLatch;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Uninterruptibles
{
    @GwtIncompatible
    public static void awaitUninterruptibly(final CountDownLatch latch) {
        boolean interrupted = false;
        while (true) {
            try {
                latch.await();
            }
            catch (InterruptedException e) {
                interrupted = true;
                continue;
            }
            finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
            break;
        }
    }
    
    @CanIgnoreReturnValue
    @GwtIncompatible
    @Beta
    public static boolean awaitUninterruptibly(final CountDownLatch latch, final Duration timeout) {
        return awaitUninterruptibly(latch, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static boolean awaitUninterruptibly(final CountDownLatch latch, final long timeout, final TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                return latch.await(remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @GwtIncompatible
    @Beta
    public static boolean awaitUninterruptibly(final Condition condition, final Duration timeout) {
        return awaitUninterruptibly(condition, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @GwtIncompatible
    public static boolean awaitUninterruptibly(final Condition condition, final long timeout, final TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                return condition.await(remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @GwtIncompatible
    public static void joinUninterruptibly(final Thread toJoin) {
        boolean interrupted = false;
        while (true) {
            try {
                toJoin.join();
            }
            catch (InterruptedException e) {
                interrupted = true;
                continue;
            }
            finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
            break;
        }
    }
    
    @GwtIncompatible
    @Beta
    public static void joinUninterruptibly(final Thread toJoin, final Duration timeout) {
        joinUninterruptibly(toJoin, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @GwtIncompatible
    public static void joinUninterruptibly(final Thread toJoin, final long timeout, final TimeUnit unit) {
        Preconditions.checkNotNull(toJoin);
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                TimeUnit.NANOSECONDS.timedJoin(toJoin, remainingNanos);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    public static <V> V getUninterruptibly(final Future<V> future) throws ExecutionException {
        boolean interrupted = false;
        try {
            return future.get();
        }
        catch (InterruptedException e) {
            interrupted = true;
            return future.get();
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @GwtIncompatible
    @Beta
    public static <V> V getUninterruptibly(final Future<V> future, final Duration timeout) throws ExecutionException, TimeoutException {
        return getUninterruptibly(future, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <V> V getUninterruptibly(final Future<V> future, final long timeout, final TimeUnit unit) throws ExecutionException, TimeoutException {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                return future.get(remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @GwtIncompatible
    public static <E> E takeUninterruptibly(final BlockingQueue<E> queue) {
        boolean interrupted = false;
        try {
            return queue.take();
        }
        catch (InterruptedException e) {
            interrupted = true;
            return queue.take();
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @GwtIncompatible
    public static <E> void putUninterruptibly(final BlockingQueue<E> queue, final E element) {
        boolean interrupted = false;
        while (true) {
            try {
                queue.put(element);
            }
            catch (InterruptedException e) {
                interrupted = true;
                continue;
            }
            finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
            break;
        }
    }
    
    @GwtIncompatible
    @Beta
    public static void sleepUninterruptibly(final Duration sleepFor) {
        sleepUninterruptibly(Internal.toNanosSaturated(sleepFor), TimeUnit.NANOSECONDS);
    }
    
    @GwtIncompatible
    public static void sleepUninterruptibly(final long sleepFor, final TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(sleepFor);
            final long end = System.nanoTime() + remainingNanos;
            try {
                TimeUnit.NANOSECONDS.sleep(remainingNanos);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @GwtIncompatible
    @Beta
    public static boolean tryAcquireUninterruptibly(final Semaphore semaphore, final Duration timeout) {
        return tryAcquireUninterruptibly(semaphore, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @GwtIncompatible
    public static boolean tryAcquireUninterruptibly(final Semaphore semaphore, final long timeout, final TimeUnit unit) {
        return tryAcquireUninterruptibly(semaphore, 1, timeout, unit);
    }
    
    @GwtIncompatible
    @Beta
    public static boolean tryAcquireUninterruptibly(final Semaphore semaphore, final int permits, final Duration timeout) {
        return tryAcquireUninterruptibly(semaphore, permits, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @GwtIncompatible
    public static boolean tryAcquireUninterruptibly(final Semaphore semaphore, final int permits, final long timeout, final TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                return semaphore.tryAcquire(permits, remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @GwtIncompatible
    @Beta
    public static boolean tryLockUninterruptibly(final Lock lock, final Duration timeout) {
        return tryLockUninterruptibly(lock, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @GwtIncompatible
    public static boolean tryLockUninterruptibly(final Lock lock, final long timeout, final TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @Beta
    @GwtIncompatible
    public static void awaitTerminationUninterruptibly(final ExecutorService executor) {
        Verify.verify(awaitTerminationUninterruptibly(executor, Long.MAX_VALUE, TimeUnit.NANOSECONDS));
    }
    
    @Beta
    @GwtIncompatible
    public static boolean awaitTerminationUninterruptibly(final ExecutorService executor, final Duration timeout) {
        return awaitTerminationUninterruptibly(executor, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @Beta
    @GwtIncompatible
    public static boolean awaitTerminationUninterruptibly(final ExecutorService executor, final long timeout, final TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            final long end = System.nanoTime() + remainingNanos;
            try {
                return executor.awaitTermination(remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException e) {
                interrupted = true;
                remainingNanos = end - System.nanoTime();
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private Uninterruptibles() {
    }
}
