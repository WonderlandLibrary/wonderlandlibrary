// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples.mutable;

import java.util.function.UnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import dev.tenacity.utils.tuples.Unit;

public class MutableUnit<A> extends Unit<A>
{
    private A a;
    
    MutableUnit(final A a) {
        this.a = a;
    }
    
    public static <A> MutableUnit<A> of(final A a) {
        return new MutableUnit<A>(a);
    }
    
    @Override
    public A get() {
        return this.a;
    }
    
    public void set(final A a) {
        this.a = a;
    }
    
    @Override
    public <R> R apply(final Function<? super A, ? extends R> func) {
        return (R)func.apply((Object)this.a);
    }
    
    @Override
    public void use(final Consumer<? super A> func) {
        func.accept((Object)this.a);
    }
    
    public void compute(final UnaryOperator<A> mapper) {
        this.a = mapper.apply(this.a);
    }
}
