/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ForwardingSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSortedSet<E>
extends ForwardingSet<E>
implements SortedSet<E> {
    protected ForwardingSortedSet() {
    }

    @Override
    protected abstract SortedSet<E> delegate();

    @Override
    public Comparator<? super E> comparator() {
        return this.delegate().comparator();
    }

    @Override
    public E first() {
        return this.delegate().first();
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return this.delegate().headSet(toElement);
    }

    @Override
    public E last() {
        return this.delegate().last();
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return this.delegate().subSet(fromElement, toElement);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return this.delegate().tailSet(fromElement);
    }

    private int unsafeCompare(Object o1, Object o2) {
        Comparator<E> comparator = this.comparator();
        return comparator == null ? ((Comparable)o1).compareTo(o2) : comparator.compare(o1, o2);
    }

    @Override
    @Beta
    protected boolean standardContains(@Nullable Object object) {
        try {
            ForwardingSortedSet self = this;
            Object ceiling = self.tailSet(object).first();
            return this.unsafeCompare(ceiling, object) == 0;
        }
        catch (ClassCastException e2) {
            return false;
        }
        catch (NoSuchElementException e3) {
            return false;
        }
        catch (NullPointerException e4) {
            return false;
        }
    }

    @Override
    @Beta
    protected boolean standardRemove(@Nullable Object object) {
        try {
            Object ceiling;
            ForwardingSortedSet self = this;
            Iterator iterator = self.tailSet(object).iterator();
            if (iterator.hasNext() && this.unsafeCompare(ceiling = iterator.next(), object) == 0) {
                iterator.remove();
                return true;
            }
        }
        catch (ClassCastException e2) {
            return false;
        }
        catch (NullPointerException e3) {
            return false;
        }
        return false;
    }

    @Beta
    protected SortedSet<E> standardSubSet(E fromElement, E toElement) {
        return this.tailSet(fromElement).headSet(toElement);
    }
}

