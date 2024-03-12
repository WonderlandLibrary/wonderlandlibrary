// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.tuples.mutable;

import java.util.function.UnaryOperator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import dev.tenacity.utils.tuples.Pair;

public class MutablePair<A, B> extends Pair<A, B>
{
    private A a;
    private B b;
    
    MutablePair(final A a, final B b) {
        this.a = a;
        this.b = b;
    }
    
    public static <A, B> MutablePair<A, B> of(final A a, final B b) {
        return new MutablePair<A, B>(a, b);
    }
    
    public static <A> MutablePair<A, A> of(final A a) {
        return new MutablePair<A, A>(a, a);
    }
    
    public MutablePair<A, A> pairOfFirst() {
        return of(this.a);
    }
    
    public MutablePair<B, B> pairOfSecond() {
        return of(this.b);
    }
    
    @Override
    public A getFirst() {
        return this.a;
    }
    
    @Override
    public B getSecond() {
        return this.b;
    }
    
    public void setFirst(final A a) {
        this.a = a;
    }
    
    public void setSecond(final B b) {
        this.b = b;
    }
    
    @Override
    public <R> R apply(final BiFunction<? super A, ? super B, ? extends R> func) {
        return (R)func.apply((Object)this.a, (Object)this.b);
    }
    
    @Override
    public void use(final BiConsumer<? super A, ? super B> func) {
        func.accept((Object)this.a, (Object)this.b);
    }
    
    public void computeFirst(final UnaryOperator<A> operator) {
        this.a = operator.apply(this.a);
    }
    
    public void computeSecond(final UnaryOperator<B> operator) {
        this.b = operator.apply(this.b);
    }
}
