/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
final class SingletonImmutableList<E>
extends ImmutableList<E> {
    final transient E element;

    SingletonImmutableList(E element) {
        this.element = Preconditions.checkNotNull(element);
    }

    @Override
    public E get(int index) {
        Preconditions.checkElementIndex(index, 1);
        return this.element;
    }

    @Override
    public int indexOf(@Nullable Object object) {
        return this.element.equals(object) ? 0 : -1;
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    @Override
    public int lastIndexOf(@Nullable Object object) {
        return this.indexOf(object);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ImmutableList<E> subList(int fromIndex, int toIndex) {
        Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
        return fromIndex == toIndex ? ImmutableList.of() : this;
    }

    @Override
    public ImmutableList<E> reverse() {
        return this;
    }

    @Override
    public boolean contains(@Nullable Object object) {
        return this.element.equals(object);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof List) {
            List that = (List)object;
            return that.size() == 1 && this.element.equals(that.get(0));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 + this.element.hashCode();
    }

    @Override
    public String toString() {
        String elementToString = this.element.toString();
        return new StringBuilder(elementToString.length() + 2).append('[').append(elementToString).append(']').toString();
    }

    @Override
    public boolean isEmpty() {
        return false;
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
}

