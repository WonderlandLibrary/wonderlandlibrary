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
import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;
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
public final class ImmutableDoubleArray implements Serializable
{
    private static final ImmutableDoubleArray EMPTY;
    private final double[] array;
    private final transient int start;
    private final int end;
    
    public static ImmutableDoubleArray of() {
        return ImmutableDoubleArray.EMPTY;
    }
    
    public static ImmutableDoubleArray of(final double e0) {
        return new ImmutableDoubleArray(new double[] { e0 });
    }
    
    public static ImmutableDoubleArray of(final double e0, final double e1) {
        return new ImmutableDoubleArray(new double[] { e0, e1 });
    }
    
    public static ImmutableDoubleArray of(final double e0, final double e1, final double e2) {
        return new ImmutableDoubleArray(new double[] { e0, e1, e2 });
    }
    
    public static ImmutableDoubleArray of(final double e0, final double e1, final double e2, final double e3) {
        return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3 });
    }
    
    public static ImmutableDoubleArray of(final double e0, final double e1, final double e2, final double e3, final double e4) {
        return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3, e4 });
    }
    
    public static ImmutableDoubleArray of(final double e0, final double e1, final double e2, final double e3, final double e4, final double e5) {
        return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3, e4, e5 });
    }
    
    public static ImmutableDoubleArray of(final double first, final double... rest) {
        Preconditions.checkArgument(rest.length <= 2147483646, (Object)"the total number of elements must fit in an int");
        final double[] array = new double[rest.length + 1];
        array[0] = first;
        System.arraycopy(rest, 0, array, 1, rest.length);
        return new ImmutableDoubleArray(array);
    }
    
    public static ImmutableDoubleArray copyOf(final double[] values) {
        return (values.length == 0) ? ImmutableDoubleArray.EMPTY : new ImmutableDoubleArray(Arrays.copyOf(values, values.length));
    }
    
    public static ImmutableDoubleArray copyOf(final Collection<Double> values) {
        return values.isEmpty() ? ImmutableDoubleArray.EMPTY : new ImmutableDoubleArray(Doubles.toArray(values));
    }
    
    public static ImmutableDoubleArray copyOf(final Iterable<Double> values) {
        if (values instanceof Collection) {
            return copyOf((Collection)values);
        }
        return builder().addAll(values).build();
    }
    
    public static ImmutableDoubleArray copyOf(final DoubleStream stream) {
        final double[] array = stream.toArray();
        return (array.length == 0) ? ImmutableDoubleArray.EMPTY : new ImmutableDoubleArray(array);
    }
    
    public static Builder builder(final int initialCapacity) {
        Preconditions.checkArgument(initialCapacity >= 0, "Invalid initialCapacity: %s", initialCapacity);
        return new Builder(initialCapacity);
    }
    
    public static Builder builder() {
        return new Builder(10);
    }
    
    private ImmutableDoubleArray(final double[] array) {
        this(array, 0, array.length);
    }
    
    private ImmutableDoubleArray(final double[] array, final int start, final int end) {
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
    
    public double get(final int index) {
        Preconditions.checkElementIndex(index, this.length());
        return this.array[this.start + index];
    }
    
    public int indexOf(final double target) {
        for (int i = this.start; i < this.end; ++i) {
            if (areEqual(this.array[i], target)) {
                return i - this.start;
            }
        }
        return -1;
    }
    
    public int lastIndexOf(final double target) {
        for (int i = this.end - 1; i >= this.start; --i) {
            if (areEqual(this.array[i], target)) {
                return i - this.start;
            }
        }
        return -1;
    }
    
    public boolean contains(final double target) {
        return this.indexOf(target) >= 0;
    }
    
    public void forEach(final DoubleConsumer consumer) {
        Preconditions.checkNotNull(consumer);
        for (int i = this.start; i < this.end; ++i) {
            consumer.accept(this.array[i]);
        }
    }
    
    public DoubleStream stream() {
        return Arrays.stream(this.array, this.start, this.end);
    }
    
    public double[] toArray() {
        return Arrays.copyOfRange(this.array, this.start, this.end);
    }
    
    public ImmutableDoubleArray subArray(final int startIndex, final int endIndex) {
        Preconditions.checkPositionIndexes(startIndex, endIndex, this.length());
        return (startIndex == endIndex) ? ImmutableDoubleArray.EMPTY : new ImmutableDoubleArray(this.array, this.start + startIndex, this.start + endIndex);
    }
    
    private Spliterator.OfDouble spliterator() {
        return Spliterators.spliterator(this.array, this.start, this.end, 1040);
    }
    
    public List<Double> asList() {
        return new AsList(this);
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ImmutableDoubleArray)) {
            return false;
        }
        final ImmutableDoubleArray that = (ImmutableDoubleArray)object;
        if (this.length() != that.length()) {
            return false;
        }
        for (int i = 0; i < this.length(); ++i) {
            if (!areEqual(this.get(i), that.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean areEqual(final double a, final double b) {
        return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        for (int i = this.start; i < this.end; ++i) {
            hash *= 31;
            hash += Doubles.hashCode(this.array[i]);
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
    
    public ImmutableDoubleArray trimmed() {
        return this.isPartialView() ? new ImmutableDoubleArray(this.toArray()) : this;
    }
    
    private boolean isPartialView() {
        return this.start > 0 || this.end < this.array.length;
    }
    
    Object writeReplace() {
        return this.trimmed();
    }
    
    Object readResolve() {
        return this.isEmpty() ? ImmutableDoubleArray.EMPTY : this;
    }
    
    static {
        EMPTY = new ImmutableDoubleArray(new double[0]);
    }
    
    @CanIgnoreReturnValue
    public static final class Builder
    {
        private double[] array;
        private int count;
        
        Builder(final int initialCapacity) {
            this.count = 0;
            this.array = new double[initialCapacity];
        }
        
        public Builder add(final double value) {
            this.ensureRoomFor(1);
            this.array[this.count] = value;
            ++this.count;
            return this;
        }
        
        public Builder addAll(final double[] values) {
            this.ensureRoomFor(values.length);
            System.arraycopy(values, 0, this.array, this.count, values.length);
            this.count += values.length;
            return this;
        }
        
        public Builder addAll(final Iterable<Double> values) {
            if (values instanceof Collection) {
                return this.addAll((Collection)values);
            }
            for (final Double value : values) {
                this.add(value);
            }
            return this;
        }
        
        public Builder addAll(final Collection<Double> values) {
            this.ensureRoomFor(values.size());
            for (final Double value : values) {
                this.array[this.count++] = value;
            }
            return this;
        }
        
        public Builder addAll(final DoubleStream stream) {
            final Spliterator.OfDouble spliterator = stream.spliterator();
            final long size = spliterator.getExactSizeIfKnown();
            if (size > 0L) {
                this.ensureRoomFor(Ints.saturatedCast(size));
            }
            spliterator.forEachRemaining(this::add);
            return this;
        }
        
        public Builder addAll(final ImmutableDoubleArray values) {
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
        public ImmutableDoubleArray build() {
            return (this.count == 0) ? ImmutableDoubleArray.EMPTY : new ImmutableDoubleArray(this.array, 0, this.count, null);
        }
    }
    
    static class AsList extends AbstractList<Double> implements RandomAccess, Serializable
    {
        private final ImmutableDoubleArray parent;
        
        private AsList(final ImmutableDoubleArray parent) {
            this.parent = parent;
        }
        
        @Override
        public int size() {
            return this.parent.length();
        }
        
        @Override
        public Double get(final int index) {
            return this.parent.get(index);
        }
        
        @Override
        public boolean contains(@CheckForNull final Object target) {
            return this.indexOf(target) >= 0;
        }
        
        @Override
        public int indexOf(@CheckForNull final Object target) {
            return (target instanceof Double) ? this.parent.indexOf((double)target) : -1;
        }
        
        @Override
        public int lastIndexOf(@CheckForNull final Object target) {
            return (target instanceof Double) ? this.parent.lastIndexOf((double)target) : -1;
        }
        
        @Override
        public List<Double> subList(final int fromIndex, final int toIndex) {
            return this.parent.subArray(fromIndex, toIndex).asList();
        }
        
        @Override
        public Spliterator<Double> spliterator() {
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
                if (!(element instanceof Double) || !areEqual(this.parent.array[i++], (double)element)) {
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
