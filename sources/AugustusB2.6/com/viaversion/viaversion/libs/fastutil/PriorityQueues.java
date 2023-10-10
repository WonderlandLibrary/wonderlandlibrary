// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class PriorityQueues
{
    public static final EmptyPriorityQueue EMPTY_QUEUE;
    
    private PriorityQueues() {
    }
    
    public static <K> PriorityQueue<K> emptyQueue() {
        return (PriorityQueue<K>)PriorityQueues.EMPTY_QUEUE;
    }
    
    public static <K> PriorityQueue<K> synchronize(final PriorityQueue<K> q) {
        return new SynchronizedPriorityQueue<K>(q);
    }
    
    public static <K> PriorityQueue<K> synchronize(final PriorityQueue<K> q, final Object sync) {
        return new SynchronizedPriorityQueue<K>(q, sync);
    }
    
    static {
        EMPTY_QUEUE = new EmptyPriorityQueue();
    }
    
    public static class EmptyPriorityQueue implements PriorityQueue, Serializable
    {
        private static final long serialVersionUID = 0L;
        
        protected EmptyPriorityQueue() {
        }
        
        @Override
        public void enqueue(final Object o) {
            throw new UnsupportedOperationException();
        }
        
        @Override
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
        
        @Override
        public Object first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public void changed() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Comparator<?> comparator() {
            return null;
        }
        
        public Object clone() {
            return PriorityQueues.EMPTY_QUEUE;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof PriorityQueue && ((PriorityQueue)o).isEmpty();
        }
        
        private Object readResolve() {
            return PriorityQueues.EMPTY_QUEUE;
        }
    }
    
    public static class SynchronizedPriorityQueue<K> implements PriorityQueue<K>, Serializable
    {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final PriorityQueue<K> q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final PriorityQueue<K> q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final PriorityQueue<K> q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final K x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public K dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public K first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public K last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.q.isEmpty();
            }
        }
        
        @Override
        public int size() {
            synchronized (this.sync) {
                return this.q.size();
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.sync) {
                this.q.clear();
            }
        }
        
        @Override
        public void changed() {
            synchronized (this.sync) {
                this.q.changed();
            }
        }
        
        @Override
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public String toString() {
            synchronized (this.sync) {
                return this.q.toString();
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.q.hashCode();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.q.equals(o);
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }
}
