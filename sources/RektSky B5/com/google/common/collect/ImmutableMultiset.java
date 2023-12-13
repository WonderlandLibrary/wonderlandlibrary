/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.RegularImmutableMultiset;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
public abstract class ImmutableMultiset<E>
extends ImmutableCollection<E>
implements Multiset<E> {
    private static final ImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(ImmutableMap.of(), 0);
    private transient ImmutableSet<Multiset.Entry<E>> entrySet;

    public static <E> ImmutableMultiset<E> of() {
        return EMPTY;
    }

    public static <E> ImmutableMultiset<E> of(E element) {
        return ImmutableMultiset.copyOfInternal(element);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2) {
        return ImmutableMultiset.copyOfInternal(e1, e2);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
        return ImmutableMultiset.copyOfInternal(e1, e2, e3);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
        return ImmutableMultiset.copyOfInternal(e1, e2, e3, e4);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
        return ImmutableMultiset.copyOfInternal(e1, e2, e3, e4, e5);
    }

    public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E ... others) {
        return ((Builder)((Builder)((Builder)((Builder)((Builder)((Builder)((Builder)new Builder().add((Object)e1)).add((Object)e2)).add((Object)e3)).add((Object)e4)).add((Object)e5)).add((Object)e6)).add((Object[])others)).build();
    }

    public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
        return ImmutableMultiset.copyOf(Arrays.asList(elements));
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
        ImmutableMultiset result;
        if (elements instanceof ImmutableMultiset && !(result = (ImmutableMultiset)elements).isPartialView()) {
            return result;
        }
        Multiset<? extends E> multiset = elements instanceof Multiset ? Multisets.cast(elements) : LinkedHashMultiset.create(elements);
        return ImmutableMultiset.copyOfInternal(multiset);
    }

    private static <E> ImmutableMultiset<E> copyOfInternal(E ... elements) {
        return ImmutableMultiset.copyOf(Arrays.asList(elements));
    }

    private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> multiset) {
        return ImmutableMultiset.copyFromEntries(multiset.entrySet());
    }

    static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> entries) {
        long size = 0L;
        ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
        for (Multiset.Entry<E> entry : entries) {
            int count = entry.getCount();
            if (count <= 0) continue;
            builder.put(entry.getElement(), count);
            size += (long)count;
        }
        if (size == 0L) {
            return ImmutableMultiset.of();
        }
        return new RegularImmutableMultiset(builder.build(), Ints.saturatedCast(size));
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
        LinkedHashMultiset multiset = LinkedHashMultiset.create();
        Iterators.addAll(multiset, elements);
        return ImmutableMultiset.copyOfInternal(multiset);
    }

    ImmutableMultiset() {
    }

    @Override
    public UnmodifiableIterator<E> iterator() {
        final Iterator entryIterator = ((ImmutableSet)this.entrySet()).iterator();
        return new UnmodifiableIterator<E>(){
            int remaining;
            E element;

            @Override
            public boolean hasNext() {
                return this.remaining > 0 || entryIterator.hasNext();
            }

            @Override
            public E next() {
                if (this.remaining <= 0) {
                    Multiset.Entry entry = (Multiset.Entry)entryIterator.next();
                    this.element = entry.getElement();
                    this.remaining = entry.getCount();
                }
                --this.remaining;
                return this.element;
            }
        };
    }

    @Override
    public boolean contains(@Nullable Object object) {
        return this.count(object) > 0;
    }

    @Override
    public boolean containsAll(Collection<?> targets) {
        return this.elementSet().containsAll(targets);
    }

    @Override
    @Deprecated
    public final int add(E element, int occurrences) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final int remove(Object element, int occurrences) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final int setCount(E element, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final boolean setCount(E element, int oldCount, int newCount) {
        throw new UnsupportedOperationException();
    }

    @Override
    @GwtIncompatible(value="not present in emulated superclass")
    int copyIntoArray(Object[] dst, int offset) {
        for (Multiset.Entry entry : this.entrySet()) {
            Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
            offset += entry.getCount();
        }
        return offset;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return Multisets.equalsImpl(this, object);
    }

    @Override
    public int hashCode() {
        return Sets.hashCodeImpl(this.entrySet());
    }

    @Override
    public String toString() {
        return ((AbstractCollection)((Object)this.entrySet())).toString();
    }

    @Override
    public ImmutableSet<Multiset.Entry<E>> entrySet() {
        ImmutableSet<Multiset.Entry<Multiset.Entry<E>>> es = this.entrySet;
        return es == null ? (this.entrySet = this.createEntrySet()) : es;
    }

    private final ImmutableSet<Multiset.Entry<E>> createEntrySet() {
        return this.isEmpty() ? ImmutableSet.of() : new EntrySet();
    }

    abstract Multiset.Entry<E> getEntry(int var1);

    @Override
    Object writeReplace() {
        return new SerializedForm(this);
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    public static class Builder<E>
    extends ImmutableCollection.Builder<E> {
        final Multiset<E> contents;

        public Builder() {
            this(LinkedHashMultiset.create());
        }

        Builder(Multiset<E> contents) {
            this.contents = contents;
        }

        @Override
        public Builder<E> add(E element) {
            this.contents.add(Preconditions.checkNotNull(element));
            return this;
        }

        public Builder<E> addCopies(E element, int occurrences) {
            this.contents.add(Preconditions.checkNotNull(element), occurrences);
            return this;
        }

        public Builder<E> setCount(E element, int count) {
            this.contents.setCount(Preconditions.checkNotNull(element), count);
            return this;
        }

        @Override
        public Builder<E> add(E ... elements) {
            super.add(elements);
            return this;
        }

        @Override
        public Builder<E> addAll(Iterable<? extends E> elements) {
            if (elements instanceof Multiset) {
                Multiset<E> multiset = Multisets.cast(elements);
                for (Multiset.Entry<E> entry : multiset.entrySet()) {
                    this.addCopies(entry.getElement(), entry.getCount());
                }
            } else {
                super.addAll(elements);
            }
            return this;
        }

        @Override
        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }

        @Override
        public ImmutableMultiset<E> build() {
            return ImmutableMultiset.copyOf(this.contents);
        }
    }

    private static class SerializedForm
    implements Serializable {
        final Object[] elements;
        final int[] counts;
        private static final long serialVersionUID = 0L;

        SerializedForm(Multiset<?> multiset) {
            int distinct = multiset.entrySet().size();
            this.elements = new Object[distinct];
            this.counts = new int[distinct];
            int i2 = 0;
            for (Multiset.Entry<?> entry : multiset.entrySet()) {
                this.elements[i2] = entry.getElement();
                this.counts[i2] = entry.getCount();
                ++i2;
            }
        }

        Object readResolve() {
            LinkedHashMultiset multiset = LinkedHashMultiset.create(this.elements.length);
            for (int i2 = 0; i2 < this.elements.length; ++i2) {
                multiset.add(this.elements[i2], this.counts[i2]);
            }
            return ImmutableMultiset.copyOf(multiset);
        }
    }

    static class EntrySetSerializedForm<E>
    implements Serializable {
        final ImmutableMultiset<E> multiset;

        EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
            this.multiset = multiset;
        }

        Object readResolve() {
            return this.multiset.entrySet();
        }
    }

    private final class EntrySet
    extends ImmutableSet<Multiset.Entry<E>> {
        private static final long serialVersionUID = 0L;

        private EntrySet() {
        }

        @Override
        boolean isPartialView() {
            return ImmutableMultiset.this.isPartialView();
        }

        @Override
        public UnmodifiableIterator<Multiset.Entry<E>> iterator() {
            return this.asList().iterator();
        }

        @Override
        ImmutableList<Multiset.Entry<E>> createAsList() {
            return new ImmutableAsList<Multiset.Entry<E>>(){

                @Override
                public Multiset.Entry<E> get(int index) {
                    return ImmutableMultiset.this.getEntry(index);
                }

                @Override
                ImmutableCollection<Multiset.Entry<E>> delegateCollection() {
                    return EntrySet.this;
                }
            };
        }

        @Override
        public int size() {
            return ImmutableMultiset.this.elementSet().size();
        }

        @Override
        public boolean contains(Object o2) {
            if (o2 instanceof Multiset.Entry) {
                Multiset.Entry entry = (Multiset.Entry)o2;
                if (entry.getCount() <= 0) {
                    return false;
                }
                int count = ImmutableMultiset.this.count(entry.getElement());
                return count == entry.getCount();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return ImmutableMultiset.this.hashCode();
        }

        @Override
        Object writeReplace() {
            return new EntrySetSerializedForm(ImmutableMultiset.this);
        }
    }
}

