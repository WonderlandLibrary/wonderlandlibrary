// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.ListIterator;

public interface IntListIterator extends IntBidirectionalIterator, ListIterator<Integer>
{
    default void set(final int k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final int k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Integer k) {
        this.set((int)k);
    }
    
    @Deprecated
    default void add(final Integer k) {
        this.add((int)k);
    }
    
    @Deprecated
    default Integer next() {
        return super.next();
    }
    
    @Deprecated
    default Integer previous() {
        return super.previous();
    }
}
