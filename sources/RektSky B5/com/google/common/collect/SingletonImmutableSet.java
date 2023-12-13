/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
final class SingletonImmutableSet<E>
extends ImmutableSet<E> {
    final transient E element;
    private transient int cachedHashCode;

    SingletonImmutableSet(E element) {
        this.element = Preconditions.checkNotNull(element);
    }

    SingletonImmutableSet(E element, int hashCode) {
        this.element = element;
        this.cachedHashCode = hashCode;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object target) {
        return this.element.equals(target);
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    int copyIntoArray(Object[] dst, int offset) {
        dst[offset] = this.element;
        return offset + 1;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Set) {
            Set that = (Set)object;
            return that.size() == 1 && this.element.equals(that.iterator().next());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        int code = this.cachedHashCode;
        if (code == 0) {
            this.cachedHashCode = code = this.element.hashCode();
        }
        return code;
    }

    @Override
    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }

    @Override
    public String toString() {
        String elementToString = this.element.toString();
        return new StringBuilder(elementToString.length() + 2).append('[').append(elementToString).append(']').toString();
    }
}

