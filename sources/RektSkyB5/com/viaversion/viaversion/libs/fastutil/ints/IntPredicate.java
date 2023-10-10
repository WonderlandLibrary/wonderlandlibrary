/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface IntPredicate
extends Predicate<Integer>,
java.util.function.IntPredicate {
    @Override
    @Deprecated
    default public boolean test(Integer t2) {
        return this.test(t2.intValue());
    }

    @Override
    default public IntPredicate and(java.util.function.IntPredicate other) {
        Objects.requireNonNull(other);
        return t2 -> this.test(t2) && other.test(t2);
    }

    default public IntPredicate and(IntPredicate other) {
        return this.and((java.util.function.IntPredicate)other);
    }

    @Override
    @Deprecated
    default public Predicate<Integer> and(Predicate<? super Integer> other) {
        return Predicate.super.and(other);
    }

    @Override
    default public IntPredicate negate() {
        return t2 -> !this.test(t2);
    }

    @Override
    default public IntPredicate or(java.util.function.IntPredicate other) {
        Objects.requireNonNull(other);
        return t2 -> this.test(t2) || other.test(t2);
    }

    default public IntPredicate or(IntPredicate other) {
        return this.or((java.util.function.IntPredicate)other);
    }

    @Override
    @Deprecated
    default public Predicate<Integer> or(Predicate<? super Integer> other) {
        return Predicate.super.or(other);
    }
}

