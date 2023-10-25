/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface IntIterator
extends PrimitiveIterator.OfInt {
    @Override
    public int nextInt();

    @Override
    @Deprecated
    default public Integer next() {
        return this.nextInt();
    }

    @Override
    @Deprecated
    default public void forEachRemaining(Consumer<? super Integer> action) {
        this.forEachRemaining(action::accept);
    }

    default public int skip(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextInt();
        }
        return n - i - 1;
    }
}

