// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples.mutable;

import java.util.function.UnaryOperator;
import dev.tenacity.utils.tuples.Triplet;

public class MutableTriplet<A, B, C> extends Triplet<A, B, C>
{
    private A a;
    private B b;
    private C c;
    
    MutableTriplet(final A a, final B b, final C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public static <A, B, C> MutableTriplet<A, B, C> of(final A a, final B b, final C c) {
        return new MutableTriplet<A, B, C>(a, b, c);
    }
    
    public static <A> MutableTriplet<A, A, A> of(final A a) {
        return new MutableTriplet<A, A, A>(a, a, a);
    }
    
    public MutableTriplet<A, A, A> pairOfFirst() {
        return of(this.a);
    }
    
    public MutableTriplet<B, B, B> pairOfSecond() {
        return of(this.b);
    }
    
    public MutableTriplet<C, C, C> pairOfThird() {
        return of(this.c);
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
    
    public void setFirst(final A a) {
        this.a = a;
    }
    
    public void setSecond(final B b) {
        this.b = b;
    }
    
    public void setThird(final C c) {
        this.c = c;
    }
    
    @Override
    public <R> R apply(final TriFunction<? super A, ? super B, ? super C, ? extends R> func) {
        return (R)func.apply((Object)this.a, (Object)this.b, (Object)this.c);
    }
    
    @Override
    public void use(final TriConsumer<? super A, ? super B, ? super C> func) {
        func.accept((Object)this.a, (Object)this.b, (Object)this.c);
    }
    
    public void computeFirst(final UnaryOperator<A> operator) {
        this.a = operator.apply(this.a);
    }
    
    public void computeSecond(final UnaryOperator<B> operator) {
        this.b = operator.apply(this.b);
    }
    
    public void computeThird(final UnaryOperator<C> operator) {
        this.c = operator.apply(this.c);
    }
}
