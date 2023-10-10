/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface IntConsumer
extends Consumer<Integer>,
java.util.function.IntConsumer {
    @Override
    @Deprecated
    default public void accept(Integer t2) {
        this.accept(t2.intValue());
    }

    @Override
    default public IntConsumer andThen(java.util.function.IntConsumer after) {
        Objects.requireNonNull(after);
        return t2 -> {
            this.accept(t2);
            after.accept(t2);
        };
    }

    default public IntConsumer andThen(IntConsumer after) {
        return this.andThen((java.util.function.IntConsumer)after);
    }

    @Override
    @Deprecated
    default public Consumer<Integer> andThen(Consumer<? super Integer> after) {
        return Consumer.super.andThen(after);
    }
}

