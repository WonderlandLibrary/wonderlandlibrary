// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.function.BinaryOperator;

@FunctionalInterface
public interface IntBinaryOperator extends BinaryOperator<Integer>, java.util.function.IntBinaryOperator
{
    int apply(final int p0, final int p1);
    
    @Deprecated
    default int applyAsInt(final int x, final int y) {
        return this.apply(x, y);
    }
    
    @Deprecated
    default Integer apply(final Integer x, final Integer y) {
        return this.apply((int)x, (int)y);
    }
}
