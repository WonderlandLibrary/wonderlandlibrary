/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.NaturalOrdering;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Iterator;

@GwtCompatible(serializable=true)
final class ReverseNaturalOrdering
extends Ordering<Comparable>
implements Serializable {
    static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
    private static final long serialVersionUID = 0L;

    @Override
    public int compare(Comparable left, Comparable right) {
        Preconditions.checkNotNull(left);
        if (left == right) {
            return 0;
        }
        return right.compareTo(left);
    }

    @Override
    public <S extends Comparable> Ordering<S> reverse() {
        return Ordering.natural();
    }

    @Override
    public <E extends Comparable> E min(E a2, E b2) {
        return NaturalOrdering.INSTANCE.max(a2, b2);
    }

    @Override
    public <E extends Comparable> E min(E a2, E b2, E c2, E ... rest) {
        return NaturalOrdering.INSTANCE.max(a2, b2, c2, rest);
    }

    @Override
    public <E extends Comparable> E min(Iterator<E> iterator) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.max(iterator));
    }

    @Override
    public <E extends Comparable> E min(Iterable<E> iterable) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.max(iterable));
    }

    @Override
    public <E extends Comparable> E max(E a2, E b2) {
        return NaturalOrdering.INSTANCE.min(a2, b2);
    }

    @Override
    public <E extends Comparable> E max(E a2, E b2, E c2, E ... rest) {
        return NaturalOrdering.INSTANCE.min(a2, b2, c2, rest);
    }

    @Override
    public <E extends Comparable> E max(Iterator<E> iterator) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.min(iterator));
    }

    @Override
    public <E extends Comparable> E max(Iterable<E> iterable) {
        return (E)((Comparable)NaturalOrdering.INSTANCE.min(iterable));
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public String toString() {
        return "Ordering.natural().reverse()";
    }

    private ReverseNaturalOrdering() {
    }
}

