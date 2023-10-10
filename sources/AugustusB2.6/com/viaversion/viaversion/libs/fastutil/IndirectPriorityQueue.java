// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.Comparator;

public interface IndirectPriorityQueue<K>
{
    void enqueue(final int p0);
    
    int dequeue();
    
    default boolean isEmpty() {
        return this.size() == 0;
    }
    
    int size();
    
    void clear();
    
    int first();
    
    default int last() {
        throw new UnsupportedOperationException();
    }
    
    default void changed() {
        this.changed(this.first());
    }
    
    Comparator<? super K> comparator();
    
    default void changed(final int index) {
        throw new UnsupportedOperationException();
    }
    
    default void allChanged() {
        throw new UnsupportedOperationException();
    }
    
    default boolean contains(final int index) {
        throw new UnsupportedOperationException();
    }
    
    default boolean remove(final int index) {
        throw new UnsupportedOperationException();
    }
    
    default int front(final int[] a) {
        throw new UnsupportedOperationException();
    }
}
