// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true)
final class GeneralRange<T> implements Serializable
{
    private final Comparator<? super T> comparator;
    private final boolean hasLowerBound;
    @CheckForNull
    private final T lowerEndpoint;
    private final BoundType lowerBoundType;
    private final boolean hasUpperBound;
    @CheckForNull
    private final T upperEndpoint;
    private final BoundType upperBoundType;
    @CheckForNull
    private transient GeneralRange<T> reverse;
    
    static <T extends Comparable> GeneralRange<T> from(final Range<T> range) {
        final T lowerEndpoint = (T)(range.hasLowerBound() ? range.lowerEndpoint() : null);
        final BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
        final T upperEndpoint = (T)(range.hasUpperBound() ? range.upperEndpoint() : null);
        final BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
        return new GeneralRange<T>(Ordering.natural(), range.hasLowerBound(), lowerEndpoint, lowerBoundType, range.hasUpperBound(), upperEndpoint, upperBoundType);
    }
    
    static <T> GeneralRange<T> all(final Comparator<? super T> comparator) {
        return new GeneralRange<T>(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
    }
    
    static <T> GeneralRange<T> downTo(final Comparator<? super T> comparator, @ParametricNullness final T endpoint, final BoundType boundType) {
        return new GeneralRange<T>(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
    }
    
    static <T> GeneralRange<T> upTo(final Comparator<? super T> comparator, @ParametricNullness final T endpoint, final BoundType boundType) {
        return new GeneralRange<T>(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
    }
    
    static <T> GeneralRange<T> range(final Comparator<? super T> comparator, @ParametricNullness final T lower, final BoundType lowerType, @ParametricNullness final T upper, final BoundType upperType) {
        return new GeneralRange<T>(comparator, true, lower, lowerType, true, upper, upperType);
    }
    
    private GeneralRange(final Comparator<? super T> comparator, final boolean hasLowerBound, @CheckForNull final T lowerEndpoint, final BoundType lowerBoundType, final boolean hasUpperBound, @CheckForNull final T upperEndpoint, final BoundType upperBoundType) {
        this.comparator = Preconditions.checkNotNull(comparator);
        this.hasLowerBound = hasLowerBound;
        this.hasUpperBound = hasUpperBound;
        this.lowerEndpoint = lowerEndpoint;
        this.lowerBoundType = Preconditions.checkNotNull(lowerBoundType);
        this.upperEndpoint = upperEndpoint;
        this.upperBoundType = Preconditions.checkNotNull(upperBoundType);
        if (hasLowerBound) {
            comparator.compare((Object)NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint), (Object)NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint));
        }
        if (hasUpperBound) {
            comparator.compare((Object)NullnessCasts.uncheckedCastNullableTToT(upperEndpoint), (Object)NullnessCasts.uncheckedCastNullableTToT(upperEndpoint));
        }
        if (hasLowerBound && hasUpperBound) {
            final int cmp = comparator.compare((Object)NullnessCasts.uncheckedCastNullableTToT(lowerEndpoint), (Object)NullnessCasts.uncheckedCastNullableTToT(upperEndpoint));
            Preconditions.checkArgument(cmp <= 0, "lowerEndpoint (%s) > upperEndpoint (%s)", lowerEndpoint, upperEndpoint);
            if (cmp == 0) {
                Preconditions.checkArgument(lowerBoundType != BoundType.OPEN | upperBoundType != BoundType.OPEN);
            }
        }
    }
    
    Comparator<? super T> comparator() {
        return this.comparator;
    }
    
    boolean hasLowerBound() {
        return this.hasLowerBound;
    }
    
    boolean hasUpperBound() {
        return this.hasUpperBound;
    }
    
    boolean isEmpty() {
        return (this.hasUpperBound() && this.tooLow(NullnessCasts.uncheckedCastNullableTToT(this.getUpperEndpoint()))) || (this.hasLowerBound() && this.tooHigh(NullnessCasts.uncheckedCastNullableTToT(this.getLowerEndpoint())));
    }
    
    boolean tooLow(@ParametricNullness final T t) {
        if (!this.hasLowerBound()) {
            return false;
        }
        final T lbound = NullnessCasts.uncheckedCastNullableTToT(this.getLowerEndpoint());
        final int cmp = this.comparator.compare((Object)t, (Object)lbound);
        return cmp < 0 | (cmp == 0 & this.getLowerBoundType() == BoundType.OPEN);
    }
    
    boolean tooHigh(@ParametricNullness final T t) {
        if (!this.hasUpperBound()) {
            return false;
        }
        final T ubound = NullnessCasts.uncheckedCastNullableTToT(this.getUpperEndpoint());
        final int cmp = this.comparator.compare((Object)t, (Object)ubound);
        return cmp > 0 | (cmp == 0 & this.getUpperBoundType() == BoundType.OPEN);
    }
    
    boolean contains(@ParametricNullness final T t) {
        return !this.tooLow(t) && !this.tooHigh(t);
    }
    
    GeneralRange<T> intersect(final GeneralRange<T> other) {
        Preconditions.checkNotNull(other);
        Preconditions.checkArgument(this.comparator.equals(other.comparator));
        boolean hasLowBound = this.hasLowerBound;
        T lowEnd = this.getLowerEndpoint();
        BoundType lowType = this.getLowerBoundType();
        if (!this.hasLowerBound()) {
            hasLowBound = other.hasLowerBound;
            lowEnd = other.getLowerEndpoint();
            lowType = other.getLowerBoundType();
        }
        else if (other.hasLowerBound()) {
            final int cmp = this.comparator.compare(this.getLowerEndpoint(), (Object)other.getLowerEndpoint());
            if (cmp < 0 || (cmp == 0 && other.getLowerBoundType() == BoundType.OPEN)) {
                lowEnd = other.getLowerEndpoint();
                lowType = other.getLowerBoundType();
            }
        }
        boolean hasUpBound = this.hasUpperBound;
        T upEnd = this.getUpperEndpoint();
        BoundType upType = this.getUpperBoundType();
        if (!this.hasUpperBound()) {
            hasUpBound = other.hasUpperBound;
            upEnd = other.getUpperEndpoint();
            upType = other.getUpperBoundType();
        }
        else if (other.hasUpperBound()) {
            final int cmp2 = this.comparator.compare(this.getUpperEndpoint(), (Object)other.getUpperEndpoint());
            if (cmp2 > 0 || (cmp2 == 0 && other.getUpperBoundType() == BoundType.OPEN)) {
                upEnd = other.getUpperEndpoint();
                upType = other.getUpperBoundType();
            }
        }
        if (hasLowBound && hasUpBound) {
            final int cmp2 = this.comparator.compare((Object)lowEnd, (Object)upEnd);
            if (cmp2 > 0 || (cmp2 == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {
                lowEnd = upEnd;
                lowType = BoundType.OPEN;
                upType = BoundType.CLOSED;
            }
        }
        return new GeneralRange<T>(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
    }
    
    @Override
    public boolean equals(@CheckForNull final Object obj) {
        if (obj instanceof GeneralRange) {
            final GeneralRange<?> r = (GeneralRange<?>)obj;
            return this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && this.getLowerBoundType().equals(r.getLowerBoundType()) && this.getUpperBoundType().equals(r.getUpperBoundType()) && Objects.equal(this.getLowerEndpoint(), r.getLowerEndpoint()) && Objects.equal(this.getUpperEndpoint(), r.getUpperEndpoint());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.comparator, this.getLowerEndpoint(), this.getLowerBoundType(), this.getUpperEndpoint(), this.getUpperBoundType());
    }
    
    GeneralRange<T> reverse() {
        GeneralRange<T> result = this.reverse;
        if (result == null) {
            result = new GeneralRange<T>(Ordering.from(this.comparator).reverse(), this.hasUpperBound, this.getUpperEndpoint(), this.getUpperBoundType(), this.hasLowerBound, this.getLowerEndpoint(), this.getLowerBoundType());
            result.reverse = this;
            return this.reverse = result;
        }
        return result;
    }
    
    @Override
    public String toString() {
        final String value = String.valueOf(this.comparator);
        final char c = (this.lowerBoundType == BoundType.CLOSED) ? '[' : '(';
        final String value2 = String.valueOf(this.hasLowerBound ? this.lowerEndpoint : "-\u221e");
        final String value3 = String.valueOf(this.hasUpperBound ? this.upperEndpoint : "\u221e");
        return new StringBuilder(4 + String.valueOf(value).length() + String.valueOf(value2).length() + String.valueOf(value3).length()).append(value).append(":").append(c).append(value2).append(',').append(value3).append((this.upperBoundType == BoundType.CLOSED) ? ']' : ')').toString();
    }
    
    @CheckForNull
    T getLowerEndpoint() {
        return this.lowerEndpoint;
    }
    
    BoundType getLowerBoundType() {
        return this.lowerBoundType;
    }
    
    @CheckForNull
    T getUpperEndpoint() {
        return this.upperEndpoint;
    }
    
    BoundType getUpperBoundType() {
        return this.upperBoundType;
    }
}
