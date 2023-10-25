package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;

@Beta
@GwtCompatible(emulated = true)
public abstract class ContiguousSet<C extends Comparable> extends ImmutableSortedSet<C> {
  final DiscreteDomain<C> domain;
  
  public static <C extends Comparable> ContiguousSet<C> create(Range<C> range, DiscreteDomain<C> domain) {
    Preconditions.checkNotNull(range);
    Preconditions.checkNotNull(domain);
    Range<C> effectiveRange = range;
    try {
      if (!range.hasLowerBound())
        effectiveRange = effectiveRange.intersection((Range)Range.atLeast((Comparable<?>)domain.minValue())); 
      if (!range.hasUpperBound())
        effectiveRange = effectiveRange.intersection((Range)Range.atMost((Comparable<?>)domain.maxValue())); 
    } catch (NoSuchElementException e) {
      throw new IllegalArgumentException(e);
    } 
    boolean empty = (effectiveRange.isEmpty() || Range.compareOrThrow((Comparable)range.lowerBound.leastValueAbove(domain), (Comparable)range.upperBound.greatestValueBelow(domain)) > 0);
    return empty ? new EmptyContiguousSet<C>(domain) : new RegularContiguousSet<C>(effectiveRange, domain);
  }
  
  ContiguousSet(DiscreteDomain<C> domain) {
    super(Ordering.natural());
    this.domain = domain;
  }
  
  public ContiguousSet<C> headSet(C toElement) {
    return headSetImpl((C)Preconditions.checkNotNull(toElement), false);
  }
  
  @GwtIncompatible("NavigableSet")
  public ContiguousSet<C> headSet(C toElement, boolean inclusive) {
    return headSetImpl((C)Preconditions.checkNotNull(toElement), inclusive);
  }
  
  public ContiguousSet<C> subSet(C fromElement, C toElement) {
    Preconditions.checkNotNull(fromElement);
    Preconditions.checkNotNull(toElement);
    Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
    return subSetImpl(fromElement, true, toElement, false);
  }
  
  @GwtIncompatible("NavigableSet")
  public ContiguousSet<C> subSet(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
    Preconditions.checkNotNull(fromElement);
    Preconditions.checkNotNull(toElement);
    Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
    return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
  }
  
  public ContiguousSet<C> tailSet(C fromElement) {
    return tailSetImpl((C)Preconditions.checkNotNull(fromElement), true);
  }
  
  @GwtIncompatible("NavigableSet")
  public ContiguousSet<C> tailSet(C fromElement, boolean inclusive) {
    return tailSetImpl((C)Preconditions.checkNotNull(fromElement), inclusive);
  }
  
  public String toString() {
    return range().toString();
  }
  
  @Deprecated
  public static <E> ImmutableSortedSet.Builder<E> builder() {
    throw new UnsupportedOperationException();
  }
  
  abstract ContiguousSet<C> headSetImpl(C paramC, boolean paramBoolean);
  
  abstract ContiguousSet<C> subSetImpl(C paramC1, boolean paramBoolean1, C paramC2, boolean paramBoolean2);
  
  abstract ContiguousSet<C> tailSetImpl(C paramC, boolean paramBoolean);
  
  public abstract ContiguousSet<C> intersection(ContiguousSet<C> paramContiguousSet);
  
  public abstract Range<C> range();
  
  public abstract Range<C> range(BoundType paramBoundType1, BoundType paramBoundType2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ContiguousSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */