/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public final class Uninterruptibles {
    public static void awaitUninterruptibly(CountDownLatch latch) {
        boolean interrupted = false;
        while (true) {
            try {
                latch.await();
                return;
            }
            catch (InterruptedException e2) {
                interrupted = true;
                continue;
            }
            break;
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static boolean awaitUninterruptibly(CountDownLatch latch, long timeout, TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    boolean bl = latch.await(remainingNanos, TimeUnit.NANOSECONDS);
                    return bl;
                }
                catch (InterruptedException e2) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                    continue;
                }
                break;
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void joinUninterruptibly(Thread toJoin) {
        boolean interrupted = false;
        while (true) {
            try {
                toJoin.join();
                return;
            }
            catch (InterruptedException e2) {
                interrupted = true;
                continue;
            }
            break;
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        boolean interrupted = false;
        while (true) {
            try {
                V v2 = future.get();
                return v2;
            }
            catch (InterruptedException e2) {
                interrupted = true;
                continue;
            }
            break;
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <V> V getUninterruptibly(Future<V> future, long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                V v2;
                try {
                    v2 = future.get(remainingNanos, TimeUnit.NANOSECONDS);
                }
                catch (InterruptedException e2) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                    continue;
                }
                return v2;
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void joinUninterruptibly(Thread toJoin, long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(toJoin);
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    TimeUnit.NANOSECONDS.timedJoin(toJoin, remainingNanos);
                    return;
                }
                catch (InterruptedException e2) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                    continue;
                }
                break;
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <E> E takeUninterruptibly(BlockingQueue<E> queue) {
        boolean interrupted = false;
        while (true) {
            try {
                E e2 = queue.take();
                return e2;
            }
            catch (InterruptedException e3) {
                interrupted = true;
                continue;
            }
            break;
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <E> void putUninterruptibly(BlockingQueue<E> queue, E element) {
        boolean interrupted = false;
        while (true) {
            try {
                queue.put(element);
                return;
            }
            catch (InterruptedException e2) {
                interrupted = true;
                continue;
            }
            break;
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(sleepFor);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    TimeUnit.NANOSECONDS.sleep(remainingNanos);
                    return;
                }
                catch (InterruptedException e2) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                    continue;
                }
                break;
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

