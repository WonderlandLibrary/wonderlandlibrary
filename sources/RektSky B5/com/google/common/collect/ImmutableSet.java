/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.EmptyImmutableSet;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableEnumSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.RegularImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.SingletonImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
public abstract class ImmutableSet<E>
extends ImmutableCollection<E>
implements Set<E> {
    static final int MAX_TABLE_SIZE = 0x40000000;
    private static final double DESIRED_LOAD_FACTOR = 0.7;
    private static final int CUTOFF = 0x2CCCCCCC;

    public static <E> ImmutableSet<E> of() {
        return EmptyImmutableSet.INSTANCE;
    }

    public static <E> ImmutableSet<E> of(E element) {
        return new SingletonImmutableSet<E>(element);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2) {
        return ImmutableSet.construct(2, e1, e2);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
        return ImmutableSet.construct(3, e1, e2, e3);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
        return ImmutableSet.construct(4, e1, e2, e3, e4);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
        return ImmutableSet.construct(5, e1, e2, e3, e4, e5);
    }

    public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E ... others) {
        int paramCount = 6;
        Object[] elements = new Object[6 + others.length];
        elements[0] = e1;
        elements[1] = e2;
        elements[2] = e3;
        elements[3] = e4;
        elements[4] = e5;
        elements[5] = e6;
        System.arraycopy(others, 0, elements, 6, others.length);
        return ImmutableSet.construct(elements.length, elements);
    }

    private static <E> ImmutableSet<E> construct(int n2, Object ... elements) {
        switch (n2) {
            case 0: {
                return ImmutableSet.of();
            }
            case 1: {
                Object elem = elements[0];
                return ImmutableSet.of(elem);
            }
        }
        int tableSize = ImmutableSet.chooseTableSize(n2);
        Object[] table = new Object[tableSize];
        int mask = tableSize - 1;
        int hashCode = 0;
        int uniques = 0;
        block4: for (int i2 = 0; i2 < n2; ++i2) {
            Object element = ObjectArrays.checkElementNotNull(elements[i2], i2);
            int hash = element.hashCode();
            int j2 = Hashing.smear(hash);
            while (true) {
                int index;
                Object value;
                if ((value = table[index = j2 & mask]) == null) {
                    elements[uniques++] = element;
                    table[index] = element;
                    hashCode += hash;
                    continue block4;
                }
                if (value.equals(element)) continue block4;
                ++j2;
            }
        }
        Arrays.fill(elements, uniques, n2, null);
        if (uniques == 1) {
            Object element = elements[0];
            return new SingletonImmutableSet<Object>(element, hashCode);
        }
        if (tableSize != ImmutableSet.chooseTableSize(uniques)) {
            return ImmutableSet.construct(uniques, elements);
        }
        Object[] uniqueElements = uniques < elements.length ? ObjectArrays.arraysCopyOf(elements, uniques) : elements;
        return new RegularImmutableSet(uniqueElements, hashCode, table, mask);
    }

    @VisibleForTesting
    static int chooseTableSize(int setSize) {
        if (setSize < 0x2CCCCCCC) {
            int tableSize = Integer.highestOneBit(setSize - 1) << 1;
            while ((double)tableSize * 0.7 < (double)setSize) {
                tableSize <<= 1;
            }
            return tableSize;
        }
        Preconditions.checkArgument(setSize < 0x40000000, "collection too large");
        return 0x40000000;
    }

    public static <E> ImmutableSet<E> copyOf(E[] elements) {
        switch (elements.length) {
            case 0: {
                return ImmutableSet.of();
            }
            case 1: {
                return ImmutableSet.of(elements[0]);
            }
        }
        return ImmutableSet.construct(elements.length, (Object[])elements.clone());
    }

    public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
        return elements instanceof Collection ? ImmutableSet.copyOf(Collections2.cast(elements)) : ImmutableSet.copyOf(elements.iterator());
    }

    public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return ImmutableSet.of();
        }
        E first = elements.next();
        if (!elements.hasNext()) {
            return ImmutableSet.of(first);
        }
        return ((Builder)((Builder)new Builder().add((Object)first)).addAll(elements)).build();
    }

    public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
        if (elements instanceof ImmutableSet && !(elements instanceof ImmutableSortedSet)) {
            ImmutableSet set = (ImmutableSet)elements;
            if (!set.isPartialView()) {
                return set;
            }
        } else if (elements instanceof EnumSet) {
            return ImmutableSet.copyOfEnumSet((EnumSet)elements);
        }
        Object[] array = elements.toArray();
        return ImmutableSet.construct(array.length, array);
    }

    private static <E extends Enum<E>> ImmutableSet<E> copyOfEnumSet(EnumSet<E> enumSet) {
        return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
    }

    ImmutableSet() {
    }

    boolean isHashCodeFast() {
        return false;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ImmutableSet && this.isHashCodeFast() && ((ImmutableSet)object).isHashCodeFast() && this.hashCode() != object.hashCode()) {
            return false;
        }
        return Sets.equalsImpl(this, object);
    }

    @Override
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    @Override
    public abstract UnmodifiableIterator<E> iterator();

    @Override
    Object writeReplace() {
        return new SerializedForm(this.toArray());
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    public static class Builder<E>
    extends ImmutableCollection.ArrayBasedBuilder<E> {
        public Builder() {
            this(4);
        }

        Builder(int capacity) {
            super(capacity);
        }

        @Override
        public Builder<E> add(E element) {
            super.add((Object)element);
            return this;
        }

        @Override
        public Builder<E> add(E ... elements) {
            super.add(elements);
            return this;
        }

        @Override
        public Builder<E> addAll(Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }

        @Override
        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }

        @Override
        public ImmutableSet<E> build() {
            ImmutableSet result = ImmutableSet.construct(this.size, this.contents);
            this.size = result.size();
            return result;
        }
    }

    private static class SerializedForm
    implements Serializable {
        final Object[] elements;
        private static final long serialVersionUID = 0L;

        SerializedForm(Object[] elements) {
            this.elements = elements;
        }

        Object readResolve() {
            return ImmutableSet.copyOf(this.elements);
        }
    }
}

