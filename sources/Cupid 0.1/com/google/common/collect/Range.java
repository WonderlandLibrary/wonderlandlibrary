package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public final class Range<C extends Comparable> implements Predicate<C>, Serializable {
  private static final Function<Range, Cut> LOWER_BOUND_FN = new Function<Range, Cut>() {
      public Cut apply(Range range) {
        return range.lowerBound;
      }
    };
  
  static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
    return (Function)LOWER_BOUND_FN;
  }
  
  private static final Function<Range, Cut> UPPER_BOUND_FN = new Function<Range, Cut>() {
      public Cut apply(Range range) {
        return range.upperBound;
      }
    };
  
  static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
    return (Function)UPPER_BOUND_FN;
  }
  
  static final Ordering<Range<?>> RANGE_LEX_ORDERING = new Ordering<Range<?>>() {
      public int compare(Range<?> left, Range<?> right) {
        return ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
      }
    };
  
  static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
    return (Range)new Range<Comparable>(lowerBound, upperBound);
  }
  
  public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
    return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
  }
  
  public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
    return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
  }
  
  public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
    return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
  }
  
  public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
    return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
  }
  
  public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
    Preconditions.checkNotNull(lowerType);
    Preconditions.checkNotNull(upperType);
    Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? (Cut)Cut.<Comparable>aboveValue((Comparable)lower) : (Cut)Cut.<Comparable>belowValue((Comparable)lower);
    Cut<C> upperBound = (upperType == BoundType.OPEN) ? (Cut)Cut.<Comparable>belowValue((Comparable)upper) : (Cut)Cut.<Comparable>aboveValue((Comparable)upper);
    return create(lowerBound, upperBound);
  }
  
  public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
    return create((Cut)Cut.belowAll(), (Cut)Cut.belowValue((Comparable)endpoint));
  }
  
  public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
    return create((Cut)Cut.belowAll(), (Cut)Cut.aboveValue((Comparable)endpoint));
  }
  
  public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
    switch (boundType) {
      case OPEN:
        return lessThan(endpoint);
      case CLOSED:
        return atMost(endpoint);
    } 
    throw new AssertionError();
  }
  
  public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
    return create((Cut)Cut.aboveValue((Comparable)endpoint), (Cut)Cut.aboveAll());
  }
  
  public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
    return create((Cut)Cut.belowValue((Comparable)endpoint), (Cut)Cut.aboveAll());
  }
  
  public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
    switch (boundType) {
      case OPEN:
        return greaterThan(endpoint);
      case CLOSED:
        return atLeast(endpoint);
    } 
    throw new AssertionError();
  }
  
  private static final Range<Comparable> ALL = new Range((Cut)Cut.belowAll(), (Cut)Cut.aboveAll());
  
  final Cut<C> lowerBound;
  
  final Cut<C> upperBound;
  
  private static final long serialVersionUID = 0L;
  
  public static <C extends Comparable<?>> Range<C> all() {
    return (Range)ALL;
  }
  
  public static <C extends Comparable<?>> Range<C> singleton(C value) {
    return closed(value, value);
  }
  
  public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
    Preconditions.checkNotNull(values);
    if (values instanceof ContiguousSet)
      return ((ContiguousSet)values).range(); 
    Iterator<C> valueIterator = values.iterator();
    Comparable comparable1 = (Comparable)Preconditions.checkNotNull(valueIterator.next());
    Comparable comparable2 = comparable1;
    while (valueIterator.hasNext()) {
      Comparable comparable = (Comparable)Preconditions.checkNotNull(valueIterator.next());
      comparable1 = (Comparable)Ordering.<Comparable>natural().min(comparable1, comparable);
      comparable2 = (Comparable)Ordering.<Comparable>natural().max(comparable2, comparable);
    } 
    return closed((C)comparable1, (C)comparable2);
  }
  
  private Range(Cut<C> lowerBound, Cut<C> upperBound) {
    if (lowerBound.compareTo(upperBound) > 0 || lowerBound == Cut.aboveAll() || upperBound == Cut.belowAll())
      throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound)); 
    this.lowerBound = (Cut<C>)Preconditions.checkNotNull(lowerBound);
    this.upperBound = (Cut<C>)Preconditions.checkNotNull(upperBound);
  }
  
  public boolean hasLowerBound() {
    return (this.lowerBound != Cut.belowAll());
  }
  
  public C lowerEndpoint() {
    return this.lowerBound.endpoint();
  }
  
  public BoundType lowerBoundType() {
    return this.lowerBound.typeAsLowerBound();
  }
  
  public boolean hasUpperBound() {
    return (this.upperBound != Cut.aboveAll());
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
  
  public boolean contains(C value) {
    Preconditions.checkNotNull(value);
    return (this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value));
  }
  
  @Deprecated
  public boolean apply(C input) {
    return contains(input);
  }
  
  public boolean containsAll(Iterable<? extends C> values) {
    if (Iterables.isEmpty(values))
      return true; 
    if (values instanceof SortedSet) {
      SortedSet<? extends C> set = cast(values);
      Comparator<?> comparator = set.comparator();
      if (Ordering.<Comparable>natural().equals(comparator) || comparator == null)
        return (contains(set.first()) && contains(set.last())); 
    } 
    for (Comparable comparable : values) {
      if (!contains((C)comparable))
        return false; 
    } 
    return true;
  }
  
  public boolean encloses(Range<C> other) {
    return (this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound.compareTo(other.upperBound) >= 0);
  }
  
  public boolean isConnected(Range<C> other) {
    return (this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound.compareTo(this.upperBound) <= 0);
  }
  
  public Range<C> intersection(Range<C> connectedRange) {
    int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
    int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
    if (lowerCmp >= 0 && upperCmp <= 0)
      return this; 
    if (lowerCmp <= 0 && upperCmp >= 0)
      return connectedRange; 
    Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
    Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
    return (Range)create(newLower, newUpper);
  }
  
  public Range<C> span(Range<C> other) {
    int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
    int upperCmp = this.upperBound.compareTo(other.upperBound);
    if (lowerCmp <= 0 && upperCmp >= 0)
      return this; 
    if (lowerCmp >= 0 && upperCmp <= 0)
      return other; 
    Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
    Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
    return (Range)create(newLower, newUpper);
  }
  
  public Range<C> canonical(DiscreteDomain<C> domain) {
    Preconditions.checkNotNull(domain);
    Cut<C> lower = this.lowerBound.canonical(domain);
    Cut<C> upper = this.upperBound.canonical(domain);
    return (lower == this.lowerBound && upper == this.upperBound) ? this : (Range)create(lower, upper);
  }
  
  public boolean equals(@Nullable Object object) {
    if (object instanceof Range) {
      Range<?> other = (Range)object;
      return (this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound));
    } 
    return false;
  }
  
  public int hashCode() {
    return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
  }
  
  public String toString() {
    return toString(this.lowerBound, this.upperBound);
  }
  
  private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
    StringBuilder sb = new StringBuilder(16);
    lowerBound.describeAsLowerBound(sb);
    sb.append('‥');
    upperBound.describeAsUpperBound(sb);
    return sb.toString();
  }
  
  private static <T> SortedSet<T> cast(Iterable<T> iterable) {
    return (SortedSet<T>)iterable;
  }
  
  Object readResolve() {
    if (equals(ALL))
      return all(); 
    return this;
  }
  
  static int compareOrThrow(Comparable<Comparable> left, Comparable right) {
    return left.compareTo(right);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Range.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */