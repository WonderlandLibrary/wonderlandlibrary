// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples.immutable;

import dev.tenacity.utils.tuples.Triplet;

public final class ImmutableTriplet<A, B, C> extends Triplet<A, B, C>
{
    private final A a;
    private final B b;
    private final C c;
    
    ImmutableTriplet(final A a, final B b, final C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public static <A, B, C> ImmutableTriplet<A, B, C> of(final A a, final B b, final C c) {
        return new ImmutableTriplet<A, B, C>(a, b, c);
    }
    
    public Triplet<A, A, A> pairOfFirst() {
        return Triplet.of(this.a);
    }
    
    public Triplet<B, B, B> pairOfSecond() {
        return Triplet.of(this.b);
    }
    
    public Triplet<C, C, C> pairOfThird() {
        return Triplet.of(this.c);
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
    public C getThird() {
        return this.c;
    }
    
    @Override
    public <R> R apply(final TriFunction<? super A, ? super B, ? super C, ? extends R> func) {
        return (R)func.apply((Object)this.a, (Object)this.b, (Object)this.c);
    }
    
    @Override
    public void use(final TriConsumer<? super A, ? super B, ? super C> func) {
        func.accept((Object)this.a, (Object)this.b, (Object)this.c);
    }
}
