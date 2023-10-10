// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class IndirectPriorityQueues
{
    public static final EmptyIndirectPriorityQueue EMPTY_QUEUE;
    
    private IndirectPriorityQueues() {
    }
    
    public static <K> IndirectPriorityQueue<K> synchronize(final IndirectPriorityQueue<K> q) {
        return new SynchronizedIndirectPriorityQueue<K>(q);
    }
    
    public static <K> IndirectPriorityQueue<K> synchronize(final IndirectPriorityQueue<K> q, final Object sync) {
        return new SynchronizedIndirectPriorityQueue<K>(q, sync);
    }
    
    static {
        EMPTY_QUEUE = new EmptyIndirectPriorityQueue();
    }
    
    public static class EmptyIndirectPriorityQueue implements IndirectPriorityQueue
    {
        protected EmptyIndirectPriorityQueue() {
        }
        
        @Override
        public void enqueue(final int i) {
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
        public boolean contains(final int index) {
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
        
        @Override
        public Comparator<?> comparator() {
            return null;
        }
        
        @Override
        public void changed(final int i) {
            throw new IllegalArgumentException("Index " + i + " is not in the queue");
        }
        
        @Override
        public boolean remove(final int i) {
            return false;
        }
        
        @Override
        public int front(final int[] a) {
            return 0;
        }
    }
    
    public static class SynchronizedIndirectPriorityQueue<K> implements IndirectPriorityQueue<K>
    {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final IndirectPriorityQueue<K> q;
        protected final Object sync;
        
        protected SynchronizedIndirectPriorityQueue(final IndirectPriorityQueue<K> q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedIndirectPriorityQueue(final IndirectPriorityQueue<K> q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final int x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public int dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public boolean contains(final int index) {
            synchronized (this.sync) {
                return this.q.contains(index);
            }
        }
        
        @Override
        public int first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public int last() {
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
        public void allChanged() {
            synchronized (this.sync) {
                this.q.allChanged();
            }
        }
        
        @Override
        public void changed(final int i) {
            synchronized (this.sync) {
                this.q.changed(i);
            }
        }
        
        @Override
        public boolean remove(final int i) {
            synchronized (this.sync) {
                return this.q.remove(i);
            }
        }
        
        @Override
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public int front(final int[] a) {
            return this.q.front(a);
        }
    }
}
