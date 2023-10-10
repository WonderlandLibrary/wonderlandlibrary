// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Arrays;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.VisibleForTesting;
import java.util.Objects;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.Spliterator;
import com.google.j2objc.annotations.RetainedWith;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.annotations.Beta;
import javax.annotation.CheckForNull;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.SortedSet;
import java.util.Collection;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import com.google.common.base.Preconditions;
import java.util.stream.Collector;
import com.google.common.annotations.GwtCompatible;
import java.util.Set;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E>
{
    static final int SPLITERATOR_CHARACTERISTICS = 1297;
    static final int MAX_TABLE_SIZE = 1073741824;
    private static final double DESIRED_LOAD_FACTOR = 0.7;
    private static final int CUTOFF = 751619276;
    
    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return CollectCollectors.toImmutableSet();
    }
    
    public static <E> ImmutableSet<E> of() {
        return (ImmutableSet<E>)RegularImmutableSet.EMPTY;
    }
    
    public static <E> ImmutableSet<E> of(final E element) {
        return new SingletonImmutableSet<E>(element);
    }
    
    public static <E> ImmutableSet<E> of(final E e1, final E e2) {
        return construct(2, 2, e1, e2);
    }
    
    public static <E> ImmutableSet<E> of(final E e1, final E e2, final E e3) {
        return construct(3, 3, e1, e2, e3);
    }
    
    public static <E> ImmutableSet<E> of(final E e1, final E e2, final E e3, final E e4) {
        return construct(4, 4, e1, e2, e3, e4);
    }
    
    public static <E> ImmutableSet<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        return construct(5, 5, e1, e2, e3, e4, e5);
    }
    
    @SafeVarargs
    public static <E> ImmutableSet<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E... others) {
        Preconditions.checkArgument(others.length <= 2147483641, (Object)"the total number of elements must fit in an int");
        final int paramCount = 6;
        final Object[] elements = new Object[6 + others.length];
        elements[0] = e1;
        elements[1] = e2;
        elements[2] = e3;
        elements[3] = e4;
        elements[4] = e5;
        elements[5] = e6;
        System.arraycopy(others, 0, elements, 6, others.length);
        return construct(elements.length, elements.length, elements);
    }
    
    private static <E> ImmutableSet<E> constructUnknownDuplication(final int n, final Object... elements) {
        return construct(n, Math.max(4, IntMath.sqrt(n, RoundingMode.CEILING)), elements);
    }
    
    private static <E> ImmutableSet<E> construct(final int n, final int expectedSize, final Object... elements) {
        switch (n) {
            case 0: {
                return of();
            }
            case 1: {
                final E elem = (E)elements[0];
                return of(elem);
            }
            default: {
                SetBuilderImpl<E> builder = new RegularSetBuilderImpl<E>(expectedSize);
                for (int i = 0; i < n; ++i) {
                    final E e = Preconditions.checkNotNull(elements[i]);
                    builder = builder.add(e);
                }
                return builder.review().build();
            }
        }
    }
    
    public static <E> ImmutableSet<E> copyOf(final Collection<? extends E> elements) {
        if (elements instanceof ImmutableSet && !(elements instanceof SortedSet)) {
            final ImmutableSet<E> set = (ImmutableSet<E>)(ImmutableSet)elements;
            if (!set.isPartialView()) {
                return set;
            }
        }
        else if (elements instanceof EnumSet) {
            return (ImmutableSet<E>)copyOfEnumSet((EnumSet)elements);
        }
        final Object[] array = elements.toArray();
        if (elements instanceof Set) {
            return construct(array.length, array.length, array);
        }
        return constructUnknownDuplication(array.length, array);
    }
    
    public static <E> ImmutableSet<E> copyOf(final Iterable<? extends E> elements) {
        return (elements instanceof Collection) ? copyOf((Collection<? extends E>)(Collection)elements) : copyOf(elements.iterator());
    }
    
    public static <E> ImmutableSet<E> copyOf(final Iterator<? extends E> elements) {
        if (!elements.hasNext()) {
            return of();
        }
        final E first = (E)elements.next();
        if (!elements.hasNext()) {
            return of(first);
        }
        return new Builder<E>().add(first).addAll(elements).build();
    }
    
    public static <E> ImmutableSet<E> copyOf(final E[] elements) {
        switch (elements.length) {
            case 0: {
                return of();
            }
            case 1: {
                return of(elements[0]);
            }
            default: {
                return constructUnknownDuplication(elements.length, (Object[])elements.clone());
            }
        }
    }
    
    private static ImmutableSet copyOfEnumSet(final EnumSet enumSet) {
        return ImmutableEnumSet.asImmutable(EnumSet.copyOf((EnumSet<Enum>)enumSet));
    }
    
    ImmutableSet() {
    }
    
    boolean isHashCodeFast() {
        return false;
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return object == this || ((!(object instanceof ImmutableSet) || !this.isHashCodeFast() || !((ImmutableSet)object).isHashCodeFast() || this.hashCode() == object.hashCode()) && Sets.equalsImpl(this, object));
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
        return new Builder<E>();
    }
    
    @Beta
    public static <E> Builder<E> builderWithExpectedSize(final int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        return new Builder<E>(expectedSize);
    }
    
    static int chooseTableSize(int setSize) {
        setSize = Math.max(setSize, 2);
        if (setSize < 751619276) {
            int tableSize;
            for (tableSize = Integer.highestOneBit(setSize - 1) << 1; tableSize * 0.7 < setSize; tableSize <<= 1) {}
            return tableSize;
        }
        Preconditions.checkArgument(setSize < 1073741824, (Object)"collection too large");
        return 1073741824;
    }
    
    @GwtCompatible
    abstract static class CachingAsList<E> extends ImmutableSet<E>
    {
        @LazyInit
        @CheckForNull
        @RetainedWith
        private transient ImmutableList<E> asList;
        
        @Override
        public ImmutableList<E> asList() {
            final ImmutableList<E> result = this.asList;
            if (result == null) {
                return this.asList = this.createAsList();
            }
            return result;
        }
        
        ImmutableList<E> createAsList() {
            return new RegularImmutableAsList<E>(this, this.toArray());
        }
    }
    
    abstract static class Indexed<E> extends CachingAsList<E>
    {
        abstract E get(final int p0);
        
        @Override
        public UnmodifiableIterator<E> iterator() {
            return this.asList().iterator();
        }
        
        @Override
        public Spliterator<E> spliterator() {
            return CollectSpliterators.indexed(this.size(), 1297, this::get);
        }
        
        @Override
        public void forEach(final Consumer<? super E> consumer) {
            Preconditions.checkNotNull(consumer);
            for (int n = this.size(), i = 0; i < n; ++i) {
                consumer.accept(this.get(i));
            }
        }
        
        @Override
        int copyIntoArray(final Object[] dst, final int offset) {
            return this.asList().copyIntoArray(dst, offset);
        }
        
        @Override
        ImmutableList<E> createAsList() {
            return new ImmutableAsList<E>() {
                @Override
                public E get(final int index) {
                    return Indexed.this.get(index);
                }
                
                @Override
                Indexed<E> delegateCollection() {
                    return Indexed.this;
                }
            };
        }
    }
    
    private static class SerializedForm implements Serializable
    {
        final Object[] elements;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final Object[] elements) {
            this.elements = elements;
        }
        
        Object readResolve() {
            return ImmutableSet.copyOf(this.elements);
        }
    }
    
    public static class Builder<E> extends ImmutableCollection.Builder<E>
    {
        @CheckForNull
        private SetBuilderImpl<E> impl;
        boolean forceCopy;
        
        public Builder() {
            this(0);
        }
        
        Builder(final int capacity) {
            if (capacity > 0) {
                this.impl = new RegularSetBuilderImpl<E>(capacity);
            }
            else {
                this.impl = EmptySetBuilderImpl.instance();
            }
        }
        
        Builder(final boolean subclass) {
            this.impl = null;
        }
        
        @VisibleForTesting
        void forceJdk() {
            Objects.requireNonNull(this.impl);
            this.impl = new JdkBackedSetBuilderImpl<E>(this.impl);
        }
        
        final void copyIfNecessary() {
            if (this.forceCopy) {
                this.copy();
                this.forceCopy = false;
            }
        }
        
        void copy() {
            Objects.requireNonNull(this.impl);
            this.impl = this.impl.copy();
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E element) {
            Objects.requireNonNull(this.impl);
            Preconditions.checkNotNull(element);
            this.copyIfNecessary();
            this.impl = this.impl.add(element);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E... elements) {
            super.add(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        Builder<E> combine(final Builder<E> other) {
            Objects.requireNonNull(this.impl);
            Objects.requireNonNull(other.impl);
            this.copyIfNecessary();
            this.impl = this.impl.combine(other.impl);
            return this;
        }
        
        @Override
        public ImmutableSet<E> build() {
            Objects.requireNonNull(this.impl);
            this.forceCopy = true;
            this.impl = this.impl.review();
            return this.impl.build();
        }
    }
    
    private abstract static class SetBuilderImpl<E>
    {
        E[] dedupedElements;
        int distinct;
        
        SetBuilderImpl(final int expectedCapacity) {
            this.dedupedElements = (E[])new Object[expectedCapacity];
            this.distinct = 0;
        }
        
        SetBuilderImpl(final SetBuilderImpl<E> toCopy) {
            this.dedupedElements = Arrays.copyOf(toCopy.dedupedElements, toCopy.dedupedElements.length);
            this.distinct = toCopy.distinct;
        }
        
        private void ensureCapacity(final int minCapacity) {
            if (minCapacity > this.dedupedElements.length) {
                final int newCapacity = ImmutableCollection.Builder.expandedCapacity(this.dedupedElements.length, minCapacity);
                this.dedupedElements = Arrays.copyOf(this.dedupedElements, newCapacity);
            }
        }
        
        final void addDedupedElement(final E e) {
            this.ensureCapacity(this.distinct + 1);
            this.dedupedElements[this.distinct++] = e;
        }
        
        abstract SetBuilderImpl<E> add(final E p0);
        
        final SetBuilderImpl<E> combine(final SetBuilderImpl<E> other) {
            SetBuilderImpl<E> result = this;
            for (int i = 0; i < other.distinct; ++i) {
                result = result.add(Objects.requireNonNull(other.dedupedElements[i]));
            }
            return result;
        }
        
        abstract SetBuilderImpl<E> copy();
        
        SetBuilderImpl<E> review() {
            return this;
        }
        
        abstract ImmutableSet<E> build();
    }
    
    private static final class EmptySetBuilderImpl<E> extends SetBuilderImpl<E>
    {
        private static final EmptySetBuilderImpl<Object> INSTANCE;
        
        static <E> SetBuilderImpl<E> instance() {
            return (SetBuilderImpl<E>)EmptySetBuilderImpl.INSTANCE;
        }
        
        private EmptySetBuilderImpl() {
            super(0);
        }
        
        @Override
        SetBuilderImpl<E> add(final E e) {
            return new RegularSetBuilderImpl<E>(4).add(e);
        }
        
        @Override
        SetBuilderImpl<E> copy() {
            return this;
        }
        
        @Override
        ImmutableSet<E> build() {
            return ImmutableSet.of();
        }
        
        static {
            INSTANCE = new EmptySetBuilderImpl<Object>();
        }
    }
    
    private static final class RegularSetBuilderImpl<E> extends SetBuilderImpl<E>
    {
        private Object[] hashTable;
        private int maxRunBeforeFallback;
        private int expandTableThreshold;
        private int hashCode;
        static final int MAX_RUN_MULTIPLIER = 13;
        
        RegularSetBuilderImpl(final int expectedCapacity) {
            super(expectedCapacity);
            this.hashTable = null;
            this.maxRunBeforeFallback = 0;
            this.expandTableThreshold = 0;
        }
        
        RegularSetBuilderImpl(final RegularSetBuilderImpl<E> toCopy) {
            super(toCopy);
            this.hashTable = (Object[])((toCopy.hashTable == null) ? null : ((Object[])toCopy.hashTable.clone()));
            this.maxRunBeforeFallback = toCopy.maxRunBeforeFallback;
            this.expandTableThreshold = toCopy.expandTableThreshold;
            this.hashCode = toCopy.hashCode;
        }
        
        @Override
        SetBuilderImpl<E> add(final E e) {
            Preconditions.checkNotNull(e);
            if (this.hashTable != null) {
                return this.insertInHashTable(e);
            }
            if (this.distinct == 0) {
                this.addDedupedElement(e);
                return this;
            }
            this.ensureTableCapacity(this.dedupedElements.length);
            final E elem = this.dedupedElements[0];
            --this.distinct;
            return this.insertInHashTable(elem).add(e);
        }
        
        private SetBuilderImpl<E> insertInHashTable(final E e) {
            Objects.requireNonNull(this.hashTable);
            final int eHash = e.hashCode();
            final int i0 = Hashing.smear(eHash);
            final int mask = this.hashTable.length - 1;
            for (int j = i0; j - i0 < this.maxRunBeforeFallback; ++j) {
                final int index = j & mask;
                final Object tableEntry = this.hashTable[index];
                if (tableEntry == null) {
                    this.addDedupedElement(e);
                    this.hashTable[index] = e;
                    this.hashCode += eHash;
                    this.ensureTableCapacity(this.distinct);
                    return this;
                }
                if (tableEntry.equals(e)) {
                    return this;
                }
            }
            return new JdkBackedSetBuilderImpl<E>(this).add(e);
        }
        
        @Override
        SetBuilderImpl<E> copy() {
            return new RegularSetBuilderImpl((RegularSetBuilderImpl<Object>)this);
        }
        
        @Override
        SetBuilderImpl<E> review() {
            if (this.hashTable == null) {
                return this;
            }
            final int targetTableSize = ImmutableSet.chooseTableSize(this.distinct);
            if (targetTableSize * 2 < this.hashTable.length) {
                this.hashTable = rebuildHashTable(targetTableSize, this.dedupedElements, this.distinct);
                this.maxRunBeforeFallback = maxRunBeforeFallback(targetTableSize);
                this.expandTableThreshold = (int)(0.7 * targetTableSize);
            }
            return (SetBuilderImpl<E>)(hashFloodingDetected(this.hashTable) ? new JdkBackedSetBuilderImpl<E>((SetBuilderImpl<Object>)this) : this);
        }
        
        @Override
        ImmutableSet<E> build() {
            switch (this.distinct) {
                case 0: {
                    return ImmutableSet.of();
                }
                case 1: {
                    return ImmutableSet.of((E)Objects.requireNonNull((E)this.dedupedElements[0]));
                }
                default: {
                    final Object[] elements = (this.distinct == this.dedupedElements.length) ? this.dedupedElements : Arrays.copyOf(this.dedupedElements, this.distinct);
                    return new RegularImmutableSet<E>(elements, this.hashCode, Objects.requireNonNull(this.hashTable), this.hashTable.length - 1);
                }
            }
        }
        
        static Object[] rebuildHashTable(final int newTableSize, final Object[] elements, final int n) {
            final Object[] hashTable = new Object[newTableSize];
            final int mask = hashTable.length - 1;
            for (int i = 0; i < n; ++i) {
                final Object e = Objects.requireNonNull(elements[i]);
                int k;
                final int j0 = k = Hashing.smear(e.hashCode());
                int index;
                while (true) {
                    index = (k & mask);
                    if (hashTable[index] == null) {
                        break;
                    }
                    ++k;
                }
                hashTable[index] = e;
            }
            return hashTable;
        }
        
        void ensureTableCapacity(final int minCapacity) {
            int newTableSize;
            if (this.hashTable == null) {
                newTableSize = ImmutableSet.chooseTableSize(minCapacity);
                this.hashTable = new Object[newTableSize];
            }
            else {
                if (minCapacity <= this.expandTableThreshold || this.hashTable.length >= 1073741824) {
                    return;
                }
                newTableSize = this.hashTable.length * 2;
                this.hashTable = rebuildHashTable(newTableSize, this.dedupedElements, this.distinct);
            }
            this.maxRunBeforeFallback = maxRunBeforeFallback(newTableSize);
            this.expandTableThreshold = (int)(0.7 * newTableSize);
        }
        
        static boolean hashFloodingDetected(final Object[] hashTable) {
            final int maxRunBeforeFallback = maxRunBeforeFallback(hashTable.length);
            final int mask = hashTable.length - 1;
            int knownRunStart = 0;
            int knownRunEnd = 0;
        Label_0016:
            while (knownRunStart < hashTable.length) {
                if (knownRunStart != knownRunEnd || hashTable[knownRunStart] != null) {
                    for (int j = knownRunStart + maxRunBeforeFallback - 1; j >= knownRunEnd; --j) {
                        if (hashTable[j & mask] == null) {
                            knownRunEnd = knownRunStart + maxRunBeforeFallback;
                            knownRunStart = j + 1;
                            continue Label_0016;
                        }
                    }
                    return true;
                }
                if (hashTable[knownRunStart + maxRunBeforeFallback - 1 & mask] == null) {
                    knownRunStart += maxRunBeforeFallback;
                }
                else {
                    ++knownRunStart;
                }
                knownRunEnd = knownRunStart;
            }
            return false;
        }
        
        static int maxRunBeforeFallback(final int tableSize) {
            return 13 * IntMath.log2(tableSize, RoundingMode.UNNECESSARY);
        }
    }
    
    private static final class JdkBackedSetBuilderImpl<E> extends SetBuilderImpl<E>
    {
        private final Set<Object> delegate;
        
        JdkBackedSetBuilderImpl(final SetBuilderImpl<E> toCopy) {
            super(toCopy);
            this.delegate = Sets.newHashSetWithExpectedSize(this.distinct);
            for (int i = 0; i < this.distinct; ++i) {
                this.delegate.add(Objects.requireNonNull((E)this.dedupedElements[i]));
            }
        }
        
        @Override
        SetBuilderImpl<E> add(final E e) {
            Preconditions.checkNotNull(e);
            if (this.delegate.add(e)) {
                this.addDedupedElement(e);
            }
            return this;
        }
        
        @Override
        SetBuilderImpl<E> copy() {
            return new JdkBackedSetBuilderImpl((SetBuilderImpl<Object>)this);
        }
        
        @Override
        ImmutableSet<E> build() {
            switch (this.distinct) {
                case 0: {
                    return ImmutableSet.of();
                }
                case 1: {
                    return ImmutableSet.of((E)Objects.requireNonNull((E)this.dedupedElements[0]));
                }
                default: {
                    return new JdkBackedImmutableSet<E>(this.delegate, ImmutableList.asImmutableList(this.dedupedElements, this.distinct));
                }
            }
        }
    }
}
