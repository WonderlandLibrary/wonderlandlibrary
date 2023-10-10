// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import java.util.Objects;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
final class RegularContiguousSet<C extends Comparable> extends ContiguousSet<C>
{
    private final Range<C> range;
    private static final long serialVersionUID = 0L;
    
    RegularContiguousSet(final Range<C> range, final DiscreteDomain<C> domain) {
        super(domain);
        this.range = range;
    }
    
    private ContiguousSet<C> intersectionInCurrentDomain(final Range<C> other) {
        return this.range.isConnected(other) ? ContiguousSet.create(this.range.intersection(other), this.domain) : new EmptyContiguousSet<C>(this.domain);
    }
    
    @Override
    ContiguousSet<C> headSetImpl(final C toElement, final boolean inclusive) {
        return this.intersectionInCurrentDomain(Range.upTo(toElement, BoundType.forBoolean(inclusive)));
    }
    
    @Override
    ContiguousSet<C> subSetImpl(final C fromElement, final boolean fromInclusive, final C toElement, final boolean toInclusive) {
        if (fromElement.compareTo(toElement) == 0 && !fromInclusive && !toInclusive) {
            return new EmptyContiguousSet<C>(this.domain);
        }
        return this.intersectionInCurrentDomain(Range.range(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
    }
    
    @Override
    ContiguousSet<C> tailSetImpl(final C fromElement, final boolean inclusive) {
        return this.intersectionInCurrentDomain(Range.downTo(fromElement, BoundType.forBoolean(inclusive)));
    }
    
    @GwtIncompatible
    @Override
    int indexOf(@CheckForNull final Object target) {
        return this.contains(target) ? ((int)this.domain.distance(this.first(), Objects.requireNonNull(target))) : -1;
    }
    
    @Override
    public UnmodifiableIterator<C> iterator() {
        return new AbstractSequentialIterator<C>(this.first()) {
            final C last = RegularContiguousSet.this.last();
            
            @CheckForNull
            @Override
            protected C computeNext(final C previous) {
                return (C)(equalsOrThrow(previous, this.last) ? null : RegularContiguousSet.this.domain.next((C)previous));
            }
        };
    }
    
    @GwtIncompatible
    @Override
    public UnmodifiableIterator<C> descendingIterator() {
        return new AbstractSequentialIterator<C>(this.last()) {
            final C first = RegularContiguousSet.this.first();
            
            @CheckForNull
            @Override
            protected C computeNext(final C previous) {
                return (C)(equalsOrThrow(previous, this.first) ? null : RegularContiguousSet.this.domain.previous((C)previous));
            }
        };
    }
    
    private static boolean equalsOrThrow(final Comparable<?> left, @CheckForNull final Comparable<?> right) {
        return right != null && Range.compareOrThrow(left, right) == 0;
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public C first() {
        return Objects.requireNonNull(this.range.lowerBound.leastValueAbove(this.domain));
    }
    
    @Override
    public C last() {
        return Objects.requireNonNull(this.range.upperBound.greatestValueBelow(this.domain));
    }
    
    @Override
    ImmutableList<C> createAsList() {
        if (this.domain.supportsFastOffset) {
            return new ImmutableAsList<C>() {
                @Override
                ImmutableSortedSet<C> delegateCollection() {
                    return (ImmutableSortedSet<C>)RegularContiguousSet.this;
                }
                
                @Override
                public C get(final int i) {
                    Preconditions.checkElementIndex(i, this.size());
                    return (C)RegularContiguousSet.this.domain.offset(RegularContiguousSet.this.first(), i);
                }
            };
        }
        return super.createAsList();
    }
    
    @Override
    public int size() {
        final long distance = this.domain.distance(this.first(), this.last());
        return (distance >= 2147483647L) ? Integer.MAX_VALUE : ((int)distance + 1);
    }
    
    @Override
    public boolean contains(@CheckForNull final Object object) {
        if (object == null) {
            return false;
        }
        try {
            return this.range.contains((C)object);
        }
        catch (ClassCastException e) {
            return false;
        }
    }
    
    @Override
    public boolean containsAll(final Collection<?> targets) {
        return Collections2.containsAllImpl(this, targets);
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public ContiguousSet<C> intersection(final ContiguousSet<C> other) {
        Preconditions.checkNotNull(other);
        Preconditions.checkArgument(this.domain.equals(other.domain));
        if (other.isEmpty()) {
            return other;
        }
        final C lowerEndpoint = Ordering.natural().max(this.first(), other.first());
        final C upperEndpoint = Ordering.natural().min(this.last(), other.last());
        return (lowerEndpoint.compareTo(upperEndpoint) <= 0) ? ContiguousSet.create((Range<C>)Range.closed(lowerEndpoint, (C)upperEndpoint), this.domain) : new EmptyContiguousSet<C>(this.domain);
    }
    
    @Override
    public Range<C> range() {
        return this.range(BoundType.CLOSED, BoundType.CLOSED);
    }
    
    @Override
    public Range<C> range(final BoundType lowerBoundType, final BoundType upperBoundType) {
        return Range.create(this.range.lowerBound.withLowerBoundType(lowerBoundType, this.domain), this.range.upperBound.withUpperBoundType(upperBoundType, this.domain));
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof RegularContiguousSet) {
            final RegularContiguousSet<?> that = (RegularContiguousSet<?>)object;
            if (this.domain.equals(that.domain)) {
                return this.first().equals(that.first()) && this.last().equals(that.last());
            }
        }
        return super.equals(object);
    }
    
    @Override
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }
    
    @GwtIncompatible
    @Override
    Object writeReplace() {
        return new SerializedForm((Range)this.range, (DiscreteDomain)this.domain);
    }
    
    @GwtIncompatible
    private static final class SerializedForm<C extends Comparable> implements Serializable
    {
        final Range<C> range;
        final DiscreteDomain<C> domain;
        
        private SerializedForm(final Range<C> range, final DiscreteDomain<C> domain) {
            this.range = range;
            this.domain = domain;
        }
        
        private Object readResolve() {
            return new RegularContiguousSet((Range<Comparable>)this.range, (DiscreteDomain<Comparable>)this.domain);
        }
    }
}
