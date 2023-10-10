/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface Multiset<E>
extends Collection<E> {
    public int count(@Nullable Object var1);

    public int add(@Nullable E var1, int var2);

    public int remove(@Nullable Object var1, int var2);

    public int setCount(E var1, int var2);

    public boolean setCount(E var1, int var2, int var3);

    public Set<E> elementSet();

    public Set<Entry<E>> entrySet();

    @Override
    public boolean equals(@Nullable Object var1);

    @Override
    public int hashCode();

    public String toString();

    @Override
    public Iterator<E> iterator();

    @Override
    public boolean contains(@Nullable Object var1);

    @Override
    public boolean containsAll(Collection<?> var1);

    @Override
    public boolean add(E var1);

    @Override
    public boolean remove(@Nullable Object var1);

    @Override
    public boolean removeAll(Collection<?> var1);

    @Override
    public boolean retainAll(Collection<?> var1);

    public static interface Entry<E> {
        public E getElement();

        public int getCount();

        public boolean equals(Object var1);

        public int hashCode();

        public String toString();
    }
}

