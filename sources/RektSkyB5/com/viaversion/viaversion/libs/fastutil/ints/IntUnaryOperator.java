/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface IntUnaryOperator
extends UnaryOperator<Integer>,
java.util.function.IntUnaryOperator {
    @Override
    public int apply(int var1);

    public static IntUnaryOperator identity() {
        return i2 -> i2;
    }

    public static IntUnaryOperator negation() {
        return i2 -> -i2;
    }

    @Override
    @Deprecated
    default public int applyAsInt(int x2) {
        return this.apply(x2);
    }

    @Override
    @Deprecated
    default public Integer apply(Integer x2) {
        return this.apply((int)x2);
    }
}

