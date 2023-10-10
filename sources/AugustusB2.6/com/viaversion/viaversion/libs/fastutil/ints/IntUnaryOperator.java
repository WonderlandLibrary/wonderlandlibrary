// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface IntUnaryOperator extends UnaryOperator<Integer>, java.util.function.IntUnaryOperator
{
    int apply(final int p0);
    
    default IntUnaryOperator identity() {
        return i -> i;
    }
    
    default IntUnaryOperator negation() {
        return i -> -i;
    }
    
    @Deprecated
    default int applyAsInt(final int x) {
        return this.apply(x);
    }
    
    @Deprecated
    default Integer apply(final Integer x) {
        return this.apply((int)x);
    }
}
