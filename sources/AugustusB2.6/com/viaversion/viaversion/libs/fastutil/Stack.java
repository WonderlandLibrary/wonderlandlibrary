// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

public interface Stack<K>
{
    void push(final K p0);
    
    K pop();
    
    boolean isEmpty();
    
    default K top() {
        return this.peek(0);
    }
    
    default K peek(final int i) {
        throw new UnsupportedOperationException();
    }
}
