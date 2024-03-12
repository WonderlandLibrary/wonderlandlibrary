// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples.immutable;

import java.util.function.Consumer;
import java.util.function.Function;
import dev.tenacity.utils.tuples.Unit;

public final class ImmutableUnit<A> extends Unit<A>
{
    private final A a;
    
    ImmutableUnit(final A a) {
        this.a = a;
    }
    
    public static <A> ImmutableUnit<A> of(final A a) {
        return new ImmutableUnit<A>(a);
    }
    
    @Override
    public A get() {
        return this.a;
    }
    
    @Override
    public <R> R apply(final Function<? super A, ? extends R> func) {
        return (R)func.apply((Object)this.a);
    }
    
    @Override
    public void use(final Consumer<? super A> func) {
        func.accept((Object)this.a);
    }
}
