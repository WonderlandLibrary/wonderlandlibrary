// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import dev.tenacity.utils.tuples.immutable.ImmutablePair;
import java.io.Serializable;

public abstract class Pair<A, B> implements Serializable
{
    public static <A, B> Pair<A, B> of(final A a, final B b) {
        return ImmutablePair.of(a, b);
    }
    
    public static <A> Pair<A, A> of(final A a) {
        return ImmutablePair.of(a, a);
    }
    
    public abstract A getFirst();
    
    public abstract B getSecond();
    
    public abstract <R> R apply(final BiFunction<? super A, ? super B, ? extends R> p0);
    
    public abstract void use(final BiConsumer<? super A, ? super B> p0);
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getFirst(), this.getSecond());
    }
    
    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof Pair) {
            final Pair<?, ?> other = (Pair<?, ?>)that;
            return Objects.equals(this.getFirst(), other.getFirst()) && Objects.equals(this.getSecond(), other.getSecond());
        }
        return false;
    }
}
