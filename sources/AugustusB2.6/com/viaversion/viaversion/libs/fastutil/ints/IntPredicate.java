// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface IntPredicate extends Predicate<Integer>, java.util.function.IntPredicate
{
    @Deprecated
    default boolean test(final Integer t) {
        return this.test(t);
    }
    
    default IntPredicate and(final java.util.function.IntPredicate other) {
        Objects.requireNonNull(other);
        return t -> this.test(t) && other.test(t);
    }
    
    default IntPredicate and(final IntPredicate other) {
        return this.and((java.util.function.IntPredicate)other);
    }
    
    @Deprecated
    default Predicate<Integer> and(final Predicate<? super Integer> other) {
        return super.and(other);
    }
    
    default IntPredicate negate() {
        return t -> !this.test(t);
    }
    
    default IntPredicate or(final java.util.function.IntPredicate other) {
        Objects.requireNonNull(other);
        return t -> this.test(t) || other.test(t);
    }
    
    default IntPredicate or(final IntPredicate other) {
        return this.or((java.util.function.IntPredicate)other);
    }
    
    @Deprecated
    default Predicate<Integer> or(final Predicate<? super Integer> other) {
        return super.or(other);
    }
}
