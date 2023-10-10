/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.function.BinaryOperator;

@FunctionalInterface
public interface IntBinaryOperator
extends BinaryOperator<Integer>,
java.util.function.IntBinaryOperator {
    @Override
    public int apply(int var1, int var2);

    @Override
    @Deprecated
    default public int applyAsInt(int x2, int y2) {
        return this.apply(x2, y2);
    }

    @Override
    @Deprecated
    default public Integer apply(Integer x2, Integer y2) {
        return this.apply((int)x2, (int)y2);
    }
}

