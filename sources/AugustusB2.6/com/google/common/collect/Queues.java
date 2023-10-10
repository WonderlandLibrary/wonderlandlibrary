// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Deque;
import java.util.Queue;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Collection;
import java.util.ArrayDeque;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.ArrayBlockingQueue;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Queues
{
    private Queues() {
    }
    
    @GwtIncompatible
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(final int capacity) {
        return new ArrayBlockingQueue<E>(capacity);
    }
    
    public static <E> ArrayDeque<E> newArrayDeque() {
        return new ArrayDeque<E>();
    }
    
    public static <E> ArrayDeque<E> newArrayDeque(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new ArrayDeque<E>((Collection)elements);
        }
        final ArrayDeque<E> deque = new ArrayDeque<E>();
        Iterables.addAll(deque, elements);
        return deque;
    }
    
    @GwtIncompatible
    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue<E>();
    }
    
    @GwtIncompatible
    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new ConcurrentLinkedQueue<E>((Collection)elements);
        }
        final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();
        Iterables.addAll(queue, elements);
        return queue;
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
        return new LinkedBlockingDeque<E>();
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(final int capacity) {
        return new LinkedBlockingDeque<E>(capacity);
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedBlockingDeque<E>((Collection)elements);
        }
        final LinkedBlockingDeque<E> deque = new LinkedBlockingDeque<E>();
        Iterables.addAll(deque, elements);
        return deque;
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue<E>();
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(final int capacity) {
        return new LinkedBlockingQueue<E>(capacity);
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedBlockingQueue<E>((Collection)elements);
        }
        final LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<E>();
        Iterables.addAll(queue, elements);
        return queue;
    }
    
    @GwtIncompatible
    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return new PriorityBlockingQueue<E>();
    }
    
    @GwtIncompatible
    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new PriorityBlockingQueue<E>((Collection)elements);
        }
        final PriorityBlockingQueue<E> queue = new PriorityBlockingQueue<E>();
        Iterables.addAll(queue, elements);
        return queue;
    }
    
    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
        return new PriorityQueue<E>();
    }
    
    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new PriorityQueue<E>((Collection)elements);
        }
        final PriorityQueue<E> queue = new PriorityQueue<E>();
        Iterables.addAll(queue, elements);
        return queue;
    }
    
    @GwtIncompatible
    public static <E> SynchronousQueue<E> newSynchronousQueue() {
        return new SynchronousQueue<E>();
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <E> int drain(final BlockingQueue<E> q, final Collection<? super E> buffer, final int numElements, final Duration timeout) throws InterruptedException {
        return drain(q, buffer, numElements, timeout.toNanos(), TimeUnit.NANOSECONDS);
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <E> int drain(final BlockingQueue<E> q, final Collection<? super E> buffer, final int numElements, final long timeout, final TimeUnit unit) throws InterruptedException {
        Preconditions.checkNotNull(buffer);
        final long deadline = System.nanoTime() + unit.toNanos(timeout);
        int added;
        for (added = 0; added < numElements; ++added) {
            added += q.drainTo(buffer, numElements - added);
            if (added < numElements) {
                final E e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
                if (e == null) {
                    break;
                }
                buffer.add((Object)e);
            }
        }
        return added;
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <E> int drainUninterruptibly(final BlockingQueue<E> q, final Collection<? super E> buffer, final int numElements, final Duration timeout) {
        return drainUninterruptibly(q, buffer, numElements, timeout.toNanos(), TimeUnit.NANOSECONDS);
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <E> int drainUninterruptibly(final BlockingQueue<E> q, final Collection<? super E> buffer, final int numElements, final long timeout, final TimeUnit unit) {
        Preconditions.checkNotNull(buffer);
        final long deadline = System.nanoTime() + unit.toNanos(timeout);
        int added = 0;
        boolean interrupted = false;
        try {
            while (added < numElements) {
                added += q.drainTo(buffer, numElements - added);
                if (added < numElements) {
                    E e;
                    while (true) {
                        try {
                            e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
                        }
                        catch (InterruptedException ex) {
                            interrupted = true;
                            continue;
                        }
                        break;
                    }
                    if (e == null) {
                        break;
                    }
                    buffer.add((Object)e);
                    ++added;
                }
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return added;
    }
    
    public static <E> Queue<E> synchronizedQueue(final Queue<E> queue) {
        return Synchronized.queue(queue, null);
    }
    
    public static <E> Deque<E> synchronizedDeque(final Deque<E> deque) {
        return Synchronized.deque(deque, null);
    }
}
