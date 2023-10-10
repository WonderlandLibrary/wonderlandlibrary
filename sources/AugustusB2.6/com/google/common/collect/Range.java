// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import com.google.common.base.Predicate;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Range<C extends Comparable> extends RangeGwtSerializationDependencies implements Predicate<C>, Serializable
{
    private static final Range<Comparable> ALL;
    final Cut<C> lowerBound;
    final Cut<C> upperBound;
    private static final long serialVersionUID = 0L;
    
    static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
        return (Function<Range<C>, Cut<C>>)LowerBoundFn.INSTANCE;
    }
    
    static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
        return (Function<Range<C>, Cut<C>>)UpperBoundFn.INSTANCE;
    }
    
    static <C extends Comparable<?>> Ordering<Range<C>> rangeLexOrdering() {
        return (Ordering<Range<C>>)RangeLexOrdering.INSTANCE;
    }
    
    static <C extends Comparable<?>> Range<C> create(final Cut<C> lowerBound, final Cut<C> upperBound) {
        return new Range<C>(lowerBound, upperBound);
    }
    
    public static <C extends Comparable<?>> Range<C> open(final C lower, final C upper) {
        return create((Cut<C>)Cut.aboveValue((C)lower), (Cut<C>)Cut.belowValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> closed(final C lower, final C upper) {
        return create((Cut<C>)Cut.belowValue((C)lower), (Cut<C>)Cut.aboveValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> closedOpen(final C lower, final C upper) {
        return create((Cut<C>)Cut.belowValue((C)lower), (Cut<C>)Cut.belowValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> openClosed(final C lower, final C upper) {
        return create((Cut<C>)Cut.aboveValue((C)lower), (Cut<C>)Cut.aboveValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> range(final C lower, final BoundType lowerType, final C upper, final BoundType upperType) {
        Preconditions.checkNotNull(lowerType);
        Preconditions.checkNotNull(upperType);
        final Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? Cut.aboveValue(lower) : Cut.belowValue(lower);
        final Cut<C> upperBound = (upperType == BoundType.OPEN) ? Cut.belowValue(upper) : Cut.aboveValue(upper);
        return create(lowerBound, upperBound);
    }
    
    public static <C extends Comparable<?>> Range<C> lessThan(final C endpoint) {
        return create(Cut.belowAll(), (Cut<C>)Cut.belowValue((C)endpoint));
    }
    
    public static <C extends Comparable<?>> Range<C> atMost(final C endpoint) {
        return create(Cut.belowAll(), (Cut<C>)Cut.aboveValue((C)endpoint));
    }
    
    public static <C extends Comparable<?>> Range<C> upTo(final C endpoint, final BoundType boundType) {
        switch (boundType) {
            case OPEN: {
                return lessThan(endpoint);
            }
            case CLOSED: {
                return atMost(endpoint);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    public static <C extends Comparable<?>> Range<C> greaterThan(final C endpoint) {
        return create((Cut<C>)Cut.aboveValue((C)endpoint), Cut.aboveAll());
    }
    
    public static <C extends Comparable<?>> Range<C> atLeast(final C endpoint) {
        return create((Cut<C>)Cut.belowValue((C)endpoint), Cut.aboveAll());
    }
    
    public static <C extends Comparable<?>> Range<C> downTo(final C endpoint, final BoundType boundType) {
        switch (boundType) {
            case OPEN: {
                return greaterThan(endpoint);
            }
            case CLOSED: {
                return atLeast(endpoint);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    public static <C extends Comparable<?>> Range<C> all() {
        return (Range<C>)Range.ALL;
    }
    
    public static <C extends Comparable<?>> Range<C> singleton(final C value) {
        return closed(value, value);
    }
    
    public static <C extends Comparable<?>> Range<C> encloseAll(final Iterable<C> values) {
        Preconditions.checkNotNull(values);
        if (values instanceof SortedSet) {
            final SortedSet<C> set = (SortedSet<C>)(SortedSet)values;
            final Comparator<?> comparator = set.comparator();
            if (Ordering.natural().equals(comparator) || comparator == null) {
                return closed(set.first(), set.last());
            }
        }
        final Iterator<C> valueIterator = values.iterator();
        C max;
        C min = max = Preconditions.checkNotNull(valueIterator.next());
        while (valueIterator.hasNext()) {
            final C value = Preconditions.checkNotNull(valueIterator.next());
            min = Ordering.natural().min(min, value);
            max = Ordering.natural().max(max, value);
        }
        return closed(min, max);
    }
    
    private Range(final Cut<C> lowerBound, final Cut<C> upperBound) {
        this.lowerBound = Preconditions.checkNotNull(lowerBound);
        this.upperBound = Preconditions.checkNotNull(upperBound);
        if (lowerBound.compareTo(upperBound) > 0 || lowerBound == Cut.aboveAll() || upperBound == Cut.belowAll()) {
            final String original = "Invalid range: ";
            final String value = String.valueOf(toString(lowerBound, upperBound));
            throw new IllegalArgumentException((value.length() != 0) ? original.concat(value) : new String(original));
        }
    }
    
    public boolean hasLowerBound() {
        return this.lowerBound != Cut.belowAll();
    }
    
    public C lowerEndpoint() {
        return this.lowerBound.endpoint();
    }
    
    public BoundType lowerBoundType() {
        return this.lowerBound.typeAsLowerBound();
    }
    
    public boolean hasUpperBound() {
        return this.upperBound != Cut.aboveAll();
    }
    
    public C upperEndpoint() {
        return this.upperBound.endpoint();
    }
    
    public BoundType upperBoundType() {
        return this.upperBound.typeAsUpperBound();
    }
    
    public boolean isEmpty() {
        return this.lowerBound.equals(this.upperBound);
    }
    
    public boolean contains(final C value) {
        Preconditions.checkNotNull(value);
        return this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value);
    }
    
    @Deprecated
    @Override
    public boolean apply(final C input) {
        return this.contains(input);
    }
    
    public boolean containsAll(final Iterable<? extends C> values) {
        if (Iterables.isEmpty(values)) {
            return true;
        }
        if (values instanceof SortedSet) {
            final SortedSet<? extends C> set = (SortedSet<? extends C>)(SortedSet)values;
            final Comparator<?> comparator = set.comparator();
            if (Ordering.natural().equals(comparator) || comparator == null) {
                return this.contains((C)set.first()) && this.contains((C)set.last());
            }
        }
        for (final C value : values) {
            if (!this.contains(value)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean encloses(final Range<C> other) {
        return this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound.compareTo(other.upperBound) >= 0;
    }
    
    public boolean isConnected(final Range<C> other) {
        return this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound.compareTo(this.upperBound) <= 0;
    }
    
    public Range<C> intersection(final Range<C> connectedRange) {
        final int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
        final int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return this;
        }
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return connectedRange;
        }
        final Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
        final Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
        return create(newLower, newUpper);
    }
    
    public Range<C> gap(final Range<C> otherRange) {
        if (this.lowerBound.compareTo(otherRange.upperBound) < 0 && otherRange.lowerBound.compareTo(this.upperBound) < 0) {
            final String value = String.valueOf(this);
            final String value2 = String.valueOf(otherRange);
            throw new IllegalArgumentException(new StringBuilder(39 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Ranges have a nonempty intersection: ").append(value).append(", ").append(value2).toString());
        }
        final boolean isThisFirst = this.lowerBound.compareTo(otherRange.lowerBound) < 0;
        final Range<C> firstRange = isThisFirst ? this : otherRange;
        final Range<C> secondRange = isThisFirst ? otherRange : this;
        return create(firstRange.upperBound, secondRange.lowerBound);
    }
    
    public Range<C> span(final Range<C> other) {
        final int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
        final int upperCmp = this.upperBound.compareTo(other.upperBound);
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return this;
        }
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return other;
        }
        final Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
        final Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
        return create(newLower, newUpper);
    }
    
    public Range<C> canonical(final DiscreteDomain<C> domain) {
        Preconditions.checkNotNull(domain);
        final Cut<C> lower = this.lowerBound.canonical(domain);
        final Cut<C> upper = this.upperBound.canonical(domain);
        return (lower == this.lowerBound && upper == this.upperBound) ? this : create(lower, upper);
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object instanceof Range) {
            final Range<?> other = (Range<?>)object;
            return this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
    }
    
    @Override
    public String toString() {
        return toString(this.lowerBound, this.upperBound);
    }
    
    private static String toString(final Cut<?> lowerBound, final Cut<?> upperBound) {
        final StringBuilder sb = new StringBuilder(16);
        lowerBound.describeAsLowerBound(sb);
        sb.append("..");
        upperBound.describeAsUpperBound(sb);
        return sb.toString();
    }
    
    Object readResolve() {
        if (this.equals(Range.ALL)) {
            return all();
        }
        return this;
    }
    
    static int compareOrThrow(final Comparable left, final Comparable right) {
        return left.compareTo(right);
    }
    
    static {
        ALL = new Range<Comparable>((Cut<Comparable>)Cut.belowAll(), (Cut<Comparable>)Cut.aboveAll());
    }
    
    static class LowerBoundFn implements Function<Range, Cut>
    {
        static final LowerBoundFn INSTANCE;
        
        @Override
        public Cut apply(final Range range) {
            return range.lowerBound;
        }
        
        static {
            INSTANCE = new LowerBoundFn();
        }
    }
    
    static class UpperBoundFn implements Function<Range, Cut>
    {
        static final UpperBoundFn INSTANCE;
        
        @Override
        public Cut apply(final Range range) {
            return range.upperBound;
        }
        
        static {
            INSTANCE = new UpperBoundFn();
        }
    }
    
    private static class RangeLexOrdering extends Ordering<Range<?>> implements Serializable
    {
        static final Ordering<Range<?>> INSTANCE;
        private static final long serialVersionUID = 0L;
        
        @Override
        public int compare(final Range<?> left, final Range<?> right) {
            return ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
        }
        
        static {
            INSTANCE = new RangeLexOrdering();
        }
    }
}
