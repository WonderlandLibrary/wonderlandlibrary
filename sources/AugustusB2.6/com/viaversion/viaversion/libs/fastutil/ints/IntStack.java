// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Stack;

public interface IntStack extends Stack<Integer>
{
    void push(final int p0);
    
    int popInt();
    
    int topInt();
    
    int peekInt(final int p0);
    
    @Deprecated
    default void push(final Integer o) {
        this.push((int)o);
    }
    
    @Deprecated
    default Integer pop() {
        return this.popInt();
    }
    
    @Deprecated
    default Integer top() {
        return this.topInt();
    }
    
    @Deprecated
    default Integer peek(final int i) {
        return this.peekInt(i);
    }
}
