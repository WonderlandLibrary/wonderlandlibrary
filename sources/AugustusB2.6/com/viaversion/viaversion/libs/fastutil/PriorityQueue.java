// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.Comparator;

public interface PriorityQueue<K>
{
    void enqueue(final K p0);
    
    K dequeue();
    
    default boolean isEmpty() {
        return this.size() == 0;
    }
    
    int size();
    
    void clear();
    
    K first();
    
    default K last() {
        throw new UnsupportedOperationException();
    }
    
    default void changed() {
        throw new UnsupportedOperationException();
    }
    
    Comparator<? super K> comparator();
}
