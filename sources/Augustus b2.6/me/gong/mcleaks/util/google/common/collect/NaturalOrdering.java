// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class NaturalOrdering extends Ordering<Comparable> implements Serializable
{
    static final NaturalOrdering INSTANCE;
    private transient Ordering<Comparable> nullsFirst;
    private transient Ordering<Comparable> nullsLast;
    private static final long serialVersionUID = 0L;
    
    @Override
    public int compare(final Comparable left, final Comparable right) {
        Preconditions.checkNotNull(left);
        Preconditions.checkNotNull(right);
        return left.compareTo(right);
    }
    
    @Override
    public <S extends Comparable> Ordering<S> nullsFirst() {
        Ordering<Comparable> result = this.nullsFirst;
        if (result == null) {
            final Ordering<Object> nullsFirst = super.nullsFirst();
            this.nullsFirst = (Ordering<Comparable>)nullsFirst;
            result = (Ordering<Comparable>)nullsFirst;
        }
        return (Ordering<S>)result;
    }
    
    @Override
    public <S extends Comparable> Ordering<S> nullsLast() {
        Ordering<Comparable> result = this.nullsLast;
        if (result == null) {
            final Ordering<Object> nullsLast = super.nullsLast();
            this.nullsLast = (Ordering<Comparable>)nullsLast;
            result = (Ordering<Comparable>)nullsLast;
        }
        return (Ordering<S>)result;
    }
    
    @Override
    public <S extends Comparable> Ordering<S> reverse() {
        return (Ordering<S>)ReverseNaturalOrdering.INSTANCE;
    }
    
    private Object readResolve() {
        return NaturalOrdering.INSTANCE;
    }
    
    @Override
    public String toString() {
        return "Ordering.natural()";
    }
    
    private NaturalOrdering() {
    }
    
    static {
        INSTANCE = new NaturalOrdering();
    }
}
