// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.util.RandomAccess;
import java.util.AbstractList;
import com.google.errorprone.annotations.CheckReturnValue;
import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Spliterators;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.Collection;
import java.util.Arrays;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;

@Immutable
@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible
public final class ImmutableIntArray implements Serializable
{
    private static final ImmutableIntArray EMPTY;
    private final int[] array;
    private final transient int start;
    private final int end;
    
    public static ImmutableIntArray of() {
        return ImmutableIntArray.EMPTY;
    }
    
    public static ImmutableIntArray of(final int e0) {
        return new ImmutableIntArray(new int[] { e0 });
    }
    
    public static ImmutableIntArray of(final int e0, final int e1) {
        return new ImmutableIntArray(new int[] { e0, e1 });
    }
    
    public static ImmutableIntArray of(final int e0, final int e1, final int e2) {
        return new ImmutableIntArray(new int[] { e0, e1, e2 });
    }
    
    public static ImmutableIntArray of(final int e0, final int e1, final int e2, final int e3) {
        return new ImmutableIntArray(new int[] { e0, e1, e2, e3 });
    }
    
    public static ImmutableIntArray of(final int e0, final int e1, final int e2, final int e3, final int e4) {
        return new ImmutableIntArray(new int[] { e0, e1, e2, e3, e4 });
    }
    
    public static ImmutableIntArray of(final int e0, final int e1, final int e2, final int e3, final int e4, final int e5) {
        return new ImmutableIntArray(new int[] { e0, e1, e2, e3, e4, e5 });
    }
    
    public static ImmutableIntArray of(final int first, final int... rest) {
        Preconditions.checkArgument(rest.length <= 2147483646, (Object)"the total number of elements must fit in an int");
        final int[] array = new int[rest.length + 1];
        array[0] = first;
        System.arraycopy(rest, 0, array, 1, rest.length);
        return new ImmutableIntArray(array);
    }
    
    public static ImmutableIntArray copyOf(final int[] values) {
        return (values.length == 0) ? ImmutableIntArray.EMPTY : new ImmutableIntArray(Arrays.copyOf(values, values.length));
    }
    
    public static ImmutableIntArray copyOf(final Collection<Integer> values) {
        return values.isEmpty() ? ImmutableIntArray.EMPTY : new ImmutableIntArray(Ints.toArray(values));
    }
    
    public static ImmutableIntArray copyOf(final Iterable<Integer> values) {
        if (values instanceof Collection) {
            return copyOf((Collection)values);
        }
        return builder().addAll(values).build();
    }
    
    public static ImmutableIntArray copyOf(final IntStream stream) {
        final int[] array = stream.toArray();
        return (array.length == 0) ? ImmutableIntArray.EMPTY : new ImmutableIntArray(array);
    }
    
    public static Builder builder(final int initialCapacity) {
        Preconditions.checkArgument(initialCapacity >= 0, "Invalid initialCapacity: %s", initialCapacity);
        return new Builder(initialCapacity);
    }
    
    public static Builder builder() {
        return new Builder(10);
    }
    
    private ImmutableIntArray(final int[] array) {
        this(array, 0, array.length);
    }
    
    private ImmutableIntArray(final int[] array, final int start, final int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }
    
    public int length() {
        return this.end - this.start;
    }
    
    public boolean isEmpty() {
        return this.end == this.start;
    }
    
    public int get(final int index) {
        Preconditions.checkElementIndex(index, this.length());
        return this.array[this.start + index];
    }
    
    public int indexOf(final int target) {
        for (int i = this.start; i < this.end; ++i) {
            if (this.array[i] == target) {
                return i - this.start;
            }
        }
        return -1;
    }
    
    public int lastIndexOf(final int target) {
        for (int i = this.end - 1; i >= this.start; --i) {
            if (this.array[i] == target) {
                return i - this.start;
            }
        }
        return -1;
    }
    
    public boolean contains(final int target) {
        return this.indexOf(target) >= 0;
    }
    
    public void forEach(final IntConsumer consumer) {
        Preconditions.checkNotNull(consumer);
        for (int i = this.start; i < this.end; ++i) {
            consumer.accept(this.array[i]);
        }
    }
    
    public IntStream stream() {
        return Arrays.stream(this.array, this.start, this.end);
    }
    
    public int[] toArray() {
        return Arrays.copyOfRange(this.array, this.start, this.end);
    }
    
    public ImmutableIntArray subArray(final int startIndex, final int endIndex) {
        Preconditions.checkPositionIndexes(startIndex, endIndex, this.length());
        return (startIndex == endIndex) ? ImmutableIntArray.EMPTY : new ImmutableIntArray(this.array, this.start + startIndex, this.start + endIndex);
    }
    
    private Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(this.array, this.start, this.end, 1040);
    }
    
    public List<Integer> asList() {
        return new AsList(this);
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ImmutableIntArray)) {
            return false;
        }
        final ImmutableIntArray that = (ImmutableIntArray)object;
        if (this.length() != that.length()) {
            return false;
        }
        for (int i = 0; i < this.length(); ++i) {
            if (this.get(i) != that.get(i)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        for (int i = this.start; i < this.end; ++i) {
            hash *= 31;
            hash += Ints.hashCode(this.array[i]);
        }
        return hash;
    }
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "[]";
        }
        final StringBuilder builder = new StringBuilder(this.length() * 5);
        builder.append('[').append(this.array[this.start]);
        for (int i = this.start + 1; i < this.end; ++i) {
            builder.append(", ").append(this.array[i]);
        }
        builder.append(']');
        return builder.toString();
    }
    
    public ImmutableIntArray trimmed() {
        return this.isPartialView() ? new ImmutableIntArray(this.toArray()) : this;
    }
    
    private boolean isPartialView() {
        return this.start > 0 || this.end < this.array.length;
    }
    
    Object writeReplace() {
        return this.trimmed();
    }
    
    Object readResolve() {
        return this.isEmpty() ? ImmutableIntArray.EMPTY : this;
    }
    
    static {
        EMPTY = new ImmutableIntArray(new int[0]);
    }
    
    @CanIgnoreReturnValue
    public static final class Builder
    {
        private int[] array;
        private int count;
        
        Builder(final int initialCapacity) {
            this.count = 0;
            this.array = new int[initialCapacity];
        }
        
        public Builder add(final int value) {
            this.ensureRoomFor(1);
            this.array[this.count] = value;
            ++this.count;
            return this;
        }
        
        public Builder addAll(final int[] values) {
            this.ensureRoomFor(values.length);
            System.arraycopy(values, 0, this.array, this.count, values.length);
            this.count += values.length;
            return this;
        }
        
        public Builder addAll(final Iterable<Integer> values) {
            if (values instanceof Collection) {
                return this.addAll((Collection)values);
            }
            for (final Integer value : values) {
                this.add(value);
            }
            return this;
        }
        
        public Builder addAll(final Collection<Integer> values) {
            this.ensureRoomFor(values.size());
            for (final Integer value : values) {
                this.array[this.count++] = value;
            }
            return this;
        }
        
        public Builder addAll(final IntStream stream) {
            final Spliterator.OfInt spliterator = stream.spliterator();
            final long size = spliterator.getExactSizeIfKnown();
            if (size > 0L) {
                this.ensureRoomFor(Ints.saturatedCast(size));
            }
            spliterator.forEachRemaining(this::add);
            return this;
        }
        
        public Builder addAll(final ImmutableIntArray values) {
            this.ensureRoomFor(values.length());
            System.arraycopy(values.array, values.start, this.array, this.count, values.length());
            this.count += values.length();
            return this;
        }
        
        private void ensureRoomFor(final int numberToAdd) {
            final int newCount = this.count + numberToAdd;
            if (newCount > this.array.length) {
                this.array = Arrays.copyOf(this.array, expandedCapacity(this.array.length, newCount));
            }
        }
        
        private static int expandedCapacity(final int oldCapacity, final int minCapacity) {
            if (minCapacity < 0) {
                throw new AssertionError((Object)"cannot store more than MAX_VALUE elements");
            }
            int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
            }
            if (newCapacity < 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            return newCapacity;
        }
        
        @CheckReturnValue
        public ImmutableIntArray build() {
            return (this.count == 0) ? ImmutableIntArray.EMPTY : new ImmutableIntArray(this.array, 0, this.count, null);
        }
    }
    
    static class AsList extends AbstractList<Integer> implements RandomAccess, Serializable
    {
        private final ImmutableIntArray parent;
        
        private AsList(final ImmutableIntArray parent) {
            this.parent = parent;
        }
        
        @Override
        public int size() {
            return this.parent.length();
        }
        
        @Override
        public Integer get(final int index) {
            return this.parent.get(index);
        }
        
        @Override
        public boolean contains(@CheckForNull final Object target) {
            return this.indexOf(target) >= 0;
        }
        
        @Override
        public int indexOf(@CheckForNull final Object target) {
            return (target instanceof Integer) ? this.parent.indexOf((int)target) : -1;
        }
        
        @Override
        public int lastIndexOf(@CheckForNull final Object target) {
            return (target instanceof Integer) ? this.parent.lastIndexOf((int)target) : -1;
        }
        
        @Override
        public List<Integer> subList(final int fromIndex, final int toIndex) {
            return this.parent.subArray(fromIndex, toIndex).asList();
        }
        
        @Override
        public Spliterator<Integer> spliterator() {
            return this.parent.spliterator();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            if (object instanceof AsList) {
                final AsList that = (AsList)object;
                return this.parent.equals(that.parent);
            }
            if (!(object instanceof List)) {
                return false;
            }
            final List<?> that2 = (List<?>)object;
            if (this.size() != that2.size()) {
                return false;
            }
            int i = this.parent.start;
            for (final Object element : that2) {
                if (!(element instanceof Integer) || this.parent.array[i++] != (int)element) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return this.parent.hashCode();
        }
        
        @Override
        public String toString() {
            return this.parent.toString();
        }
    }
}
