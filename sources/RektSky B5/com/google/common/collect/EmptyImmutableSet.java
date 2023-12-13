/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
final class EmptyImmutableSet
extends ImmutableSet<Object> {
    static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
    private static final long serialVersionUID = 0L;

    private EmptyImmutableSet() {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(@Nullable Object target) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> targets) {
        return targets.isEmpty();
    }

    @Override
    public UnmodifiableIterator<Object> iterator() {
        return Iterators.emptyIterator();
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    int copyIntoArray(Object[] dst, int offset) {
        return offset;
    }

    @Override
    public ImmutableList<Object> asList() {
        return ImmutableList.of();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Set) {
            Set that = (Set)object;
            return that.isEmpty();
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return 0;
    }

    @Override
    boolean isHashCodeFast() {
        return true;
    }

    @Override
    public String toString() {
        return "[]";
    }

    Object readResolve() {
        return INSTANCE;
    }
}

