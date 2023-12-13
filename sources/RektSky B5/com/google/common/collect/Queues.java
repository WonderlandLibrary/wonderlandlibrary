/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Synchronized;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public final class Queues {
    private Queues() {
    }

    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
        return new ArrayBlockingQueue(capacity);
    }

    public static <E> ArrayDeque<E> newArrayDeque() {
        return new ArrayDeque();
    }

    public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new ArrayDeque<E>(Collections2.cast(elements));
        }
        ArrayDeque deque = new ArrayDeque();
        Iterables.addAll(deque, elements);
        return deque;
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue();
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new ConcurrentLinkedQueue<E>(Collections2.cast(elements));
        }
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        Iterables.addAll(queue, elements);
        return queue;
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
        return new LinkedBlockingDeque();
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
        return new LinkedBlockingDeque(capacity);
    }

    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedBlockingDeque<E>(Collections2.cast(elements));
        }
        LinkedBlockingDeque deque = new LinkedBlockingDeque();
        Iterables.addAll(deque, elements);
        return deque;
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue();
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
        return new LinkedBlockingQueue(capacity);
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new LinkedBlockingQueue<E>(Collections2.cast(elements));
        }
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        Iterables.addAll(queue, elements);
        return queue;
    }

    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return new PriorityBlockingQueue();
    }

    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new PriorityBlockingQueue<E>(Collections2.cast(elements));
        }
        PriorityBlockingQueue queue = new PriorityBlockingQueue();
        Iterables.addAll(queue, elements);
        return queue;
    }

    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
        return new PriorityQueue();
    }

    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return new PriorityQueue<E>(Collections2.cast(elements));
        }
        PriorityQueue queue = new PriorityQueue();
        Iterables.addAll(queue, elements);
        return queue;
    }

    public static <E> SynchronousQueue<E> newSynchronousQueue() {
        return new SynchronousQueue();
    }

    @Beta
    public static <E> int drain(BlockingQueue<E> q2, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
        Preconditions.checkNotNull(buffer);
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        int added = 0;
        while (added < numElements) {
            if ((added += q2.drainTo(buffer, numElements - added)) >= numElements) continue;
            E e2 = q2.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
            if (e2 == null) break;
            buffer.add(e2);
            ++added;
        }
        return added;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Beta
    public static <E> int drainUninterruptibly(BlockingQueue<E> q2, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(buffer);
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        int added = 0;
        boolean interrupted = false;
        try {
            while (added < numElements) {
                E e2;
                if ((added += q2.drainTo(buffer, numElements - added)) >= numElements) continue;
                while (true) {
                    try {
                        e2 = q2.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
                    }
                    catch (InterruptedException ex) {
                        interrupted = true;
                        continue;
                    }
                    break;
                }
                if (e2 == null) {
                    break;
                }
                buffer.add(e2);
                ++added;
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return added;
    }

    @Beta
    public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
        return Synchronized.queue(queue, null);
    }

    @Beta
    public static <E> Deque<E> synchronizedDeque(Deque<E> deque) {
        return Synchronized.deque(deque, null);
    }
}

