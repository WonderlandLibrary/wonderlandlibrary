/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine;

import com.github.benmanes.caffeine.SCQHeader;
import com.github.benmanes.caffeine.base.UnsafeAccess;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Deprecated
public final class SingleConsumerQueue<E>
extends SCQHeader.HeadAndTailRef<E>
implements Queue<E>,
Serializable {
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    static final int ARENA_LENGTH = SingleConsumerQueue.ceilingPowerOfTwo((NCPU + 1) / 2);
    static final int ARENA_MASK = ARENA_LENGTH - 1;
    static final Function<?, ?> OPTIMISIC = Node::new;
    static final int SPINS = NCPU == 1 ? 0 : 2000;
    static final long PROBE = UnsafeAccess.objectFieldOffset(Thread.class, "threadLocalRandomProbe");
    final AtomicReference<Node<E>>[] arena = new AtomicReference[ARENA_LENGTH];
    final Function<E, Node<E>> factory;
    static final long serialVersionUID = 1L;

    static int ceilingPowerOfTwo(int x) {
        return 1 << -Integer.numberOfLeadingZeros(x - 1);
    }

    private SingleConsumerQueue(Function<E, Node<E>> factory) {
        for (int i = 0; i < ARENA_LENGTH; ++i) {
            this.arena[i] = new AtomicReference();
        }
        Node<Object> node = new Node<Object>(null);
        this.factory = factory;
        this.lazySetTail(node);
        this.head = node;
    }

    public static <E> SingleConsumerQueue<E> optimistic() {
        Function<?, ?> factory = OPTIMISIC;
        return new SingleConsumerQueue(factory);
    }

    public static <E> SingleConsumerQueue<E> linearizable() {
        return new SingleConsumerQueue<Object>(LinearizableNode::new);
    }

    @Override
    public boolean isEmpty() {
        return this.head == this.tail;
    }

    @Override
    public int size() {
        int size;
        Node cursor = this.head;
        Node t = this.tail;
        for (size = 0; cursor != t && size != Integer.MAX_VALUE; ++size) {
            Node next = cursor.getNextRelaxed();
            if (next == null) {
                while ((next = cursor.next) == null) {
                }
            }
            cursor = next;
        }
        return size;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            if (!o.equals(it.next())) continue;
            return true;
        }
        return false;
    }

    @Override
    public E peek() {
        Node h = this.head;
        Node t = this.tail;
        if (h == t) {
            return null;
        }
        Node next = h.getNextRelaxed();
        if (next == null) {
            while ((next = h.next) == null) {
            }
        }
        return next.value;
    }

    @Override
    public boolean offer(E e) {
        Objects.requireNonNull(e);
        Node<E> node = this.factory.apply(e);
        this.append(node, node);
        return true;
    }

    @Override
    public E poll() {
        Node h = this.head;
        Node next = h.getNextRelaxed();
        if (next == null) {
            if (h == this.tail) {
                return null;
            }
            while ((next = h.next) == null) {
            }
        }
        Object e = next.value;
        next.value = null;
        this.head = next;
        if (this.factory == OPTIMISIC) {
            h.next = null;
        }
        return e;
    }

    @Override
    public boolean add(E e) {
        return this.offer(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c);
        Node<E> first = null;
        Node<E> last = null;
        for (E e : c) {
            Objects.requireNonNull(e);
            if (first == null) {
                last = first = this.factory.apply(e);
                continue;
            }
            Node<E> newLast = new Node<E>(e);
            last.lazySetNext(newLast);
            last = newLast;
        }
        if (first == null) {
            return false;
        }
        this.append(first, last);
        return true;
    }

    void append(@NonNull Node<E> first, @NonNull Node<E> last) {
        while (true) {
            Node t;
            if (this.casTail(t = this.tail, last)) {
                t.lazySetNext(first);
                if (this.factory == OPTIMISIC) {
                    return;
                }
                while (true) {
                    first.complete();
                    if (first == last) {
                        return;
                    }
                    Node<E> next = first.getNextRelaxed();
                    if (next == null) {
                        return;
                    }
                    if (next.value == null) {
                        first.next = null;
                    }
                    first = next;
                }
            }
            Node<E> node = this.transferOrCombine(first, last);
            if (node == null) {
                first.await();
                return;
            }
            if (node == first) continue;
            last = node;
        }
    }

    @Nullable Node<E> transferOrCombine(@NonNull Node<E> first, Node<E> last) {
        Node<E> found;
        int index = SingleConsumerQueue.index();
        AtomicReference<Node<E>> slot = this.arena[index];
        while (true) {
            if ((found = slot.get()) == null) {
                if (!slot.compareAndSet(null, first)) continue;
                for (int spin = 0; spin < SPINS; ++spin) {
                    if (slot.get() == first) continue;
                    return null;
                }
                return slot.compareAndSet(first, null) ? first : null;
            }
            if (slot.compareAndSet(found, null)) break;
        }
        last.lazySetNext(found);
        last = SingleConsumerQueue.findLast(found);
        for (int i = 1; i < ARENA_LENGTH; ++i) {
            slot = this.arena[i + index & ARENA_MASK];
            found = slot.get();
            if (found == null || !slot.compareAndSet(found, null)) continue;
            last.lazySetNext(found);
            last = SingleConsumerQueue.findLast(found);
        }
        return last;
    }

    static int index() {
        int probe = UnsafeAccess.UNSAFE.getInt((Object)Thread.currentThread(), PROBE);
        if (probe == 0) {
            ThreadLocalRandom.current();
            probe = UnsafeAccess.UNSAFE.getInt((Object)Thread.currentThread(), PROBE);
        }
        return probe & ARENA_MASK;
    }

    static <E> @NonNull Node<E> findLast(@NonNull Node<E> node) {
        Node<E> next;
        while ((next = node.getNextRelaxed()) != null) {
            node = next;
        }
        return node;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>(){
            Node<E> prev;
            Node<E> t;
            Node<E> cursor;
            boolean failOnRemoval;
            {
                this.t = SingleConsumerQueue.this.tail;
                this.cursor = SingleConsumerQueue.this.head;
                this.failOnRemoval = true;
            }

            @Override
            public boolean hasNext() {
                return this.cursor != this.t;
            }

            @Override
            public E next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.advance();
                this.failOnRemoval = false;
                return this.cursor.value;
            }

            private void advance() {
                if (this.prev == null || !this.failOnRemoval) {
                    this.prev = this.cursor;
                }
                this.cursor = this.awaitNext();
            }

            @Override
            public void remove() {
                if (this.failOnRemoval) {
                    throw new IllegalStateException();
                }
                this.failOnRemoval = true;
                this.cursor.value = null;
                if (this.t == this.cursor) {
                    this.prev.lazySetNext(null);
                    if (SingleConsumerQueue.this.casTail(this.t, this.prev)) {
                        return;
                    }
                }
                this.prev.lazySetNext(this.awaitNext());
            }

            Node<E> awaitNext() {
                if (this.cursor.getNextRelaxed() == null) {
                    while (this.cursor.next == null) {
                    }
                }
                return this.cursor.getNextRelaxed();
            }
        };
    }

    Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    static final class LinearizableNode<E>
    extends Node<E> {
        volatile boolean done;

        LinearizableNode(@Nullable E value) {
            super(value);
        }

        @Override
        void complete() {
            this.done = true;
        }

        @Override
        void await() {
            while (!this.done) {
            }
        }

        @Override
        boolean isDone() {
            return this.done;
        }
    }

    static class Node<E> {
        static final long NEXT_OFFSET = UnsafeAccess.objectFieldOffset(Node.class, "next");
        @Nullable E value;
        volatile @Nullable Node<E> next;

        Node(@Nullable E value) {
            this.value = value;
        }

        @Nullable Node<E> getNextRelaxed() {
            return (Node)UnsafeAccess.UNSAFE.getObject((Object)this, NEXT_OFFSET);
        }

        void lazySetNext(@Nullable Node<E> newNext) {
            UnsafeAccess.UNSAFE.putOrderedObject(this, NEXT_OFFSET, newNext);
        }

        void complete() {
        }

        void await() {
        }

        boolean isDone() {
            return true;
        }

        public String toString() {
            return this.getClass().getSimpleName() + "[" + this.value + "]";
        }
    }

    static final class SerializationProxy<E>
    implements Serializable {
        final boolean linearizable;
        final List<E> elements;
        static final long serialVersionUID = 1L;

        SerializationProxy(SingleConsumerQueue<E> queue) {
            this.linearizable = queue.factory.apply(null) instanceof LinearizableNode;
            this.elements = new ArrayList<E>(queue);
        }

        Object readResolve() {
            SingleConsumerQueue<E> queue = this.linearizable ? SingleConsumerQueue.linearizable() : SingleConsumerQueue.optimistic();
            queue.addAll(this.elements);
            return queue;
        }
    }
}

