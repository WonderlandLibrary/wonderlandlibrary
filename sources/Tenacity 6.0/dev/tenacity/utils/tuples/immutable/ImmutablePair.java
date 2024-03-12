// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples.immutable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import dev.tenacity.utils.tuples.Pair;

public final class ImmutablePair<A, B> extends Pair<A, B>
{
    private final A a;
    private final B b;
    
    ImmutablePair(final A a, final B b) {
        this.a = a;
        this.b = b;
    }
    
    public static <A, B> ImmutablePair<A, B> of(final A a, final B b) {
        return new ImmutablePair<A, B>(a, b);
    }
    
    public Pair<A, A> pairOfFirst() {
        return Pair.of(this.a);
    }
    
    public Pair<B, B> pairOfSecond() {
        return Pair.of(this.b);
    }
    
    @Override
    public A getFirst() {
        return this.a;
    }
    
    @Override
    public B getSecond() {
        return this.b;
    }
    
    @Override
    public <R> R apply(final BiFunction<? super A, ? super B, ? extends R> func) {
        return (R)func.apply((Object)this.a, (Object)this.b);
    }
    
    @Override
    public void use(final BiConsumer<? super A, ? super B> func) {
        func.accept((Object)this.a, (Object)this.b);
    }
}
