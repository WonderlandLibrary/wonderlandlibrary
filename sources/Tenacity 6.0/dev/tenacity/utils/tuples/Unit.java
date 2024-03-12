// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import dev.tenacity.utils.tuples.immutable.ImmutableUnit;
import java.io.Serializable;

public abstract class Unit<A> implements Serializable
{
    public static <A> Unit<A> of(final A a) {
        return ImmutableUnit.of(a);
    }
    
    public abstract A get();
    
    public abstract <R> R apply(final Function<? super A, ? extends R> p0);
    
    public abstract void use(final Consumer<? super A> p0);
    
    @Override
    public int hashCode() {
        return Objects.hash(this.get());
    }
    
    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof Unit) {
            final Unit<?> other = (Unit<?>)that;
            return Objects.equals(this.get(), other.get());
        }
        return false;
    }
}
