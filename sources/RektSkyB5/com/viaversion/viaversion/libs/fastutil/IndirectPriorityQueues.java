/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil;

import com.viaversion.viaversion.libs.fastutil.IndirectPriorityQueue;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class IndirectPriorityQueues {
    public static final EmptyIndirectPriorityQueue EMPTY_QUEUE = new EmptyIndirectPriorityQueue();

    private IndirectPriorityQueues() {
    }

    public static <K> IndirectPriorityQueue<K> synchronize(IndirectPriorityQueue<K> q2) {
        return new SynchronizedIndirectPriorityQueue<K>(q2);
    }

    public static <K> IndirectPriorityQueue<K> synchronize(IndirectPriorityQueue<K> q2, Object sync) {
        return new SynchronizedIndirectPriorityQueue<K>(q2, sync);
    }

    public static class SynchronizedIndirectPriorityQueue<K>
    implements IndirectPriorityQueue<K> {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final IndirectPriorityQueue<K> q;
        protected final Object sync;

        protected SynchronizedIndirectPriorityQueue(IndirectPriorityQueue<K> q2, Object sync) {
            this.q = q2;
            this.sync = sync;
        }

        protected SynchronizedIndirectPriorityQueue(IndirectPriorityQueue<K> q2) {
            this.q = q2;
            this.sync = this;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void enqueue(int x2) {
            Object object = this.sync;
            synchronized (object) {
                this.q.enqueue(x2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int dequeue() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.dequeue();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean contains(int index) {
            Object object = this.sync;
            synchronized (object) {
                return this.q.contains(index);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int first() {
            Object object = this.sync;
            synchronized (object) {
                return this.q.first();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int last() {
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
        public void allChanged() {
            Object object = this.sync;
            synchronized (object) {
                this.q.allChanged();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void changed(int i2) {
            Object object = this.sync;
            synchronized (object) {
                this.q.changed(i2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(int i2) {
            Object object = this.sync;
            synchronized (object) {
                return this.q.remove(i2);
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

        @Override
        public int front(int[] a2) {
            return this.q.front(a2);
        }
    }

    public static class EmptyIndirectPriorityQueue
    implements IndirectPriorityQueue {
        protected EmptyIndirectPriorityQueue() {
        }

        @Override
        public void enqueue(int i2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int dequeue() {
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
        public boolean contains(int index) {
            return false;
        }

        @Override
        public void clear() {
        }

        @Override
        public int first() {
            throw new NoSuchElementException();
        }

        @Override
        public int last() {
            throw new NoSuchElementException();
        }

        @Override
        public void changed() {
            throw new NoSuchElementException();
        }

        @Override
        public void allChanged() {
        }

        public Comparator<?> comparator() {
            return null;
        }

        @Override
        public void changed(int i2) {
            throw new IllegalArgumentException("Index " + i2 + " is not in the queue");
        }

        @Override
        public boolean remove(int i2) {
            return false;
        }

        @Override
        public int front(int[] a2) {
            return 0;
        }
    }
}

