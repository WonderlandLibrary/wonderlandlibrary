// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples;

import java.util.Objects;
import dev.tenacity.utils.tuples.immutable.ImmutableTriplet;
import java.io.Serializable;

public abstract class Triplet<A, B, C> implements Serializable
{
    public static <A, B, C> Triplet<A, B, C> of(final A a, final B b, final C c) {
        return ImmutableTriplet.of(a, b, c);
    }
    
    public static <A> Triplet<A, A, A> of(final A a) {
        return ImmutableTriplet.of(a, a, a);
    }
    
    public abstract A getFirst();
    
    public abstract B getSecond();
    
    public abstract C getThird();
    
    public abstract <R> R apply(final TriFunction<? super A, ? super B, ? super C, ? extends R> p0);
    
    public abstract void use(final TriConsumer<? super A, ? super B, ? super C> p0);
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getFirst(), this.getSecond(), this.getThird());
    }
    
    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof Triplet) {
            final Triplet<?, ?, ?> other = (Triplet<?, ?, ?>)that;
            return Objects.equals(this.getFirst(), other.getFirst()) && Objects.equals(this.getSecond(), other.getSecond()) && Objects.equals(this.getThird(), other.getThird());
        }
        return false;
    }
    
    public interface TriConsumer<T, U, V>
    {
        void accept(final T p0, final U p1, final V p2);
    }
    
    public interface TriFunction<T, U, V, R>
    {
        R apply(final T p0, final U p1, final V p2);
    }
}
