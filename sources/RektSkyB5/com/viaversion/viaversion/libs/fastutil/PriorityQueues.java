/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil;

import com.viaversion.viaversion.libs.fastutil.PriorityQueue;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class PriorityQueues {
    public static final EmptyPriorityQueue EMPTY_QUEUE = new EmptyPriorityQueue();

    private PriorityQueues() {
    }

    public static <K> PriorityQueue<K> emptyQueue() {
        return EMPTY_QUEUE;
    }

    public static <K> PriorityQueue<K> synchronize(PriorityQueue<K> q2) {
        return new SynchronizedPriorityQueue<K>(q2);
    }

    public static <K> PriorityQueue<K> synchronize(PriorityQueue<K> q2, Object sync) {
        return new SynchronizedPriorityQueue<K>(q2, sync);
    }

    public static class EmptyPriorityQueue
    implements PriorityQueue,
    Serializable {
        private static final long serialVersionUID = 0L;

        protected EmptyPriorityQueue() {
        }

        public void enqueue(Object o2) {
            throw new UnsupportedOperationException();
        }

        public Object dequeue() {
            throw new NoSuchElementException();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public void clear() {
        }

        public Object first() {
            throw new NoSuchElementException();
        }

        public Object last() {
            throw new NoSuchElementException();
        }

        @Override
        public void changed() {
            throw new NoSuchElementException();
        }

        public Comparator<?> comparator() {
            return null;
        }

        public Object clone() {
            return EMPTY_QUEUE;
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o2) {
            return o2 instanceof PriorityQueue && ((PriorityQueue)o2).isEmpty();
        }

        private Object readResolve() {
            return EMPTY_QUEUE;
        }
    }

    public static class SynchronizedPriorityQueue<K>
    implements PriorityQueue<K>,
    Serializable {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final PriorityQueue<K> q;
        protected final Object sync;

        protected SynchronizedPriorityQueue(PriorityQueue<K> q2, Object sync) {
            this.q = q2;
            this.sync = sync;
        }

        protected SynchronizedPriorityQueue(PriorityQueue<K> q2) {
            this.q = q2;
            this.sync = this;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void enqueue(K x2) {
            Object object = this.sync;
            synchronized (object) {
                this.q.enqueue(x2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K dequeue() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.dequeue();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K first() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.first();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K last() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.last();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.isEmpty();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.sync;
            synchronized (object) {
                this.q.clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void changed() {
            Object object = this.sync;
            synchronized (object) {
                this.q.changed();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Comparator<? super K> comparator() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.comparator();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public String toString() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.toString();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public int hashCode() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.hashCode();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            Object object = this.sync;
            synchronized (object) {
                return this.q.equals(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void writeObject(ObjectOutputStream s2) throws IOException {
            Object object = this.sync;
            synchronized (object) {
                s2.defaultWriteObject();
            }
        }
    }
}

