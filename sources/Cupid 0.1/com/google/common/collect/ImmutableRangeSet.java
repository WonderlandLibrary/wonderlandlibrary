package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public final class ImmutableRangeSet<C extends Comparable> extends AbstractRangeSet<C> implements Serializable {
  private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(ImmutableList.of());
  
  private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(ImmutableList.of((Range)Range.all()));
  
  private final transient ImmutableList<Range<C>> ranges;
  
  private transient ImmutableRangeSet<C> complement;
  
  public static <C extends Comparable> ImmutableRangeSet<C> of() {
    return (ImmutableRangeSet)EMPTY;
  }
  
  static <C extends Comparable> ImmutableRangeSet<C> all() {
    return (ImmutableRangeSet)ALL;
  }
  
  public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range) {
    Preconditions.checkNotNull(range);
    if (range.isEmpty())
      return of(); 
    if (range.equals(Range.all()))
      return all(); 
    return new ImmutableRangeSet<C>(ImmutableList.of(range));
  }
  
  public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet) {
    Preconditions.checkNotNull(rangeSet);
    if (rangeSet.isEmpty())
      return of(); 
    if (rangeSet.encloses((Range)Range.all()))
      return all(); 
    if (rangeSet instanceof ImmutableRangeSet) {
      ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet<C>)rangeSet;
      if (!immutableRangeSet.isPartialView())
        return immutableRangeSet; 
    } 
    return new ImmutableRangeSet<C>(ImmutableList.copyOf(rangeSet.asRanges()));
  }
  
  ImmutableRangeSet(ImmutableList<Range<C>> ranges) {
    this.ranges = ranges;
  }
  
  private ImmutableRangeSet(ImmutableList<Range<C>> ranges, ImmutableRangeSet<C> complement) {
    this.ranges = ranges;
    this.complement = complement;
  }
  
  public boolean encloses(Range<C> otherRange) {
    int index = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), otherRange.lowerBound, Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
    return (index != -1 && ((Range<C>)this.ranges.get(index)).encloses(otherRange));
  }
  
  public Range<C> rangeContaining(C value) {
    int index = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), Cut.belowValue(value), Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
    if (index != -1) {
      Range<C> range = this.ranges.get(index);
      return range.contains(value) ? range : null;
    } 
    return null;
  }
  
  public Range<C> span() {
    if (this.ranges.isEmpty())
      throw new NoSuchElementException(); 
    return (Range)Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
  }
  
  public boolean isEmpty() {
    return this.ranges.isEmpty();
  }
  
  public void add(Range<C> range) {
    throw new UnsupportedOperationException();
  }
  
  public void addAll(RangeSet<C> other) {
    throw new UnsupportedOperationException();
  }
  
  public void remove(Range<C> range) {
    throw new UnsupportedOperationException();
  }
  
  public void removeAll(RangeSet<C> other) {
    throw new UnsupportedOperationException();
  }
  
  public ImmutableSet<Range<C>> asRanges() {
    if (this.ranges.isEmpty())
      return ImmutableSet.of(); 
    return (ImmutableSet)new RegularImmutableSortedSet<Range<?>>(this.ranges, Range.RANGE_LEX_ORDERING);
  }
  
  private final class ComplementRanges extends ImmutableList<Range<C>> {
    private final boolean positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
    
    private final boolean positiveBoundedAbove = ((Range)Iterables.<Range>getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
    
    private final int size;
    
    ComplementRanges() {
      int size = ImmutableRangeSet.this.ranges.size() - 1;
      if (this.positiveBoundedBelow)
        size++; 
      if (this.positiveBoundedAbove)
        size++; 
      this.size = size;
    }
    
    public int size() {
      return this.size;
    }
    
    public Range<C> get(int index) {
      Cut<C> lowerBound, upperBound;
      Preconditions.checkElementIndex(index, this.size);
      if (this.positiveBoundedBelow) {
        lowerBound = (index == 0) ? Cut.<C>belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound;
      } else {
        lowerBound = ((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
      } 
      if (this.positiveBoundedAbove && index == this.size - 1) {
        upperBound = Cut.aboveAll();
      } else {
        upperBound = ((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
      } 
      return (Range)Range.create(lowerBound, upperBound);
    }
    
    boolean isPartialView() {
      return true;
    }
  }
  
  public ImmutableRangeSet<C> complement() {
    ImmutableRangeSet<C> result = this.complement;
    if (result != null)
      return result; 
    if (this.ranges.isEmpty())
      return this.complement = all(); 
    if (this.ranges.size() == 1 && ((Range)this.ranges.get(0)).equals(Range.all()))
      return this.complement = of(); 
    ImmutableList<Range<C>> complementRanges = new ComplementRanges();
    result = this.complement = new ImmutableRangeSet(complementRanges, this);
    return result;
  }
  
  private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
    final int fromIndex, toIndex;
    if (this.ranges.isEmpty() || range.isEmpty())
      return ImmutableList.of(); 
    if (range.encloses(span()))
      return this.ranges; 
    if (range.hasLowerBound()) {
      fromIndex = SortedLists.binarySearch(this.ranges, (Function)Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    } else {
      fromIndex = 0;
    } 
    if (range.hasUpperBound()) {
      toIndex = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    } else {
      toIndex = this.ranges.size();
    } 
    final int length = toIndex - fromIndex;
    if (length == 0)
      return ImmutableList.of(); 
    return new ImmutableList<Range<C>>() {
        public int size() {
          return length;
        }
        
        public Range<C> get(int index) {
          Preconditions.checkElementIndex(index, length);
          if (index == 0 || index == length - 1)
            return ((Range<C>)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range); 
          return ImmutableRangeSet.this.ranges.get(index + fromIndex);
        }
        
        boolean isPartialView() {
          return true;
        }
      };
  }
  
  public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
    if (!isEmpty()) {
      Range<C> span = span();
      if (range.encloses(span))
        return this; 
      if (range.isConnected(span))
        return new ImmutableRangeSet(intersectRanges(range)); 
    } 
    return of();
  }
  
  public ImmutableSortedSet<C> asSet(DiscreteDomain<C> domain) {
    Preconditions.checkNotNull(domain);
    if (isEmpty())
      return ImmutableSortedSet.of(); 
    Range<C> span = span().canonical(domain);
    if (!span.hasLowerBound())
      throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below"); 
    if (!span.hasUpperBound())
      try {
        domain.maxValue();
      } catch (NoSuchElementException e) {
        throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
      }  
    return new AsSet(domain);
  }
  
  private final class AsSet extends ImmutableSortedSet<C> {
    private final DiscreteDomain<C> domain;
    
    private transient Integer size;
    
    AsSet(DiscreteDomain<C> domain) {
      super(Ordering.natural());
      this.domain = domain;
    }
    
    public int size() {
      Integer result = this.size;
      if (result == null) {
        long total = 0L;
        for (Range<C> range : (Iterable<Range<C>>)ImmutableRangeSet.this.ranges) {
          total += ContiguousSet.<C>create(range, this.domain).size();
          if (total >= 2147483647L)
            break; 
        } 
        result = this.size = Integer.valueOf(Ints.saturatedCast(total));
      } 
      return result.intValue();
    }
    
    public UnmodifiableIterator<C> iterator() {
      return new AbstractIterator<C>() {
          final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();
          
          Iterator<C> elemItr = Iterators.emptyIterator();
          
          protected C computeNext() {
            while (!this.elemItr.hasNext()) {
              if (this.rangeItr.hasNext()) {
                this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).iterator();
                continue;
              } 
              return endOfData();
            } 
            return this.elemItr.next();
          }
        };
    }
    
    @GwtIncompatible("NavigableSet")
    public UnmodifiableIterator<C> descendingIterator() {
      return new AbstractIterator<C>() {
          final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
          
          Iterator<C> elemItr = Iterators.emptyIterator();
          
          protected C computeNext() {
            while (!this.elemItr.hasNext()) {
              if (this.rangeItr.hasNext()) {
                this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).descendingIterator();
                continue;
              } 
              return endOfData();
            } 
            return this.elemItr.next();
          }
        };
    }
    
    ImmutableSortedSet<C> subSet(Range<C> range) {
      return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
    }
    
    ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive) {
      return subSet((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
    }
    
    ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
      if (!fromInclusive && !toInclusive && Range.compareOrThrow((Comparable)fromElement, (Comparable)toElement) == 0)
        return ImmutableSortedSet.of(); 
      return subSet((Range)Range.range((Comparable<?>)fromElement, BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, BoundType.forBoolean(toInclusive)));
    }
    
    ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive) {
      return subSet((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
    }
    
    public boolean contains(@Nullable Object o) {
      if (o == null)
        return false; 
      try {
        Comparable comparable = (Comparable)o;
        return ImmutableRangeSet.this.contains(comparable);
      } catch (ClassCastException e) {
        return false;
      } 
    }
    
    int indexOf(Object target) {
      if (contains(target)) {
        Comparable comparable = (Comparable)target;
        long total = 0L;
        for (Range<C> range : (Iterable<Range<C>>)ImmutableRangeSet.this.ranges) {
          if (range.contains((C)comparable))
            return Ints.saturatedCast(total + ContiguousSet.<C>create(range, this.domain).indexOf(comparable)); 
          total += ContiguousSet.<C>create(range, this.domain).size();
        } 
        throw new AssertionError("impossible");
      } 
      return -1;
    }
    
    boolean isPartialView() {
      return ImmutableRangeSet.this.ranges.isPartialView();
    }
    
    public String toString() {
      return ImmutableRangeSet.this.ranges.toString();
    }
    
    Object writeReplace() {
      return new ImmutableRangeSet.AsSetSerializedForm<C>(ImmutableRangeSet.this.ranges, this.domain);
    }
  }
  
  private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
    private final ImmutableList<Range<C>> ranges;
    
    private final DiscreteDomain<C> domain;
    
    AsSetSerializedForm(ImmutableList<Range<C>> ranges, DiscreteDomain<C> domain) {
      this.ranges = ranges;
      this.domain = domain;
    }
    
    Object readResolve() {
      return (new ImmutableRangeSet<C>(this.ranges)).asSet(this.domain);
    }
  }
  
  boolean isPartialView() {
    return this.ranges.isPartialView();
  }
  
  public static <C extends Comparable<?>> Builder<C> builder() {
    return new Builder<C>();
  }
  
  public static class Builder<C extends Comparable<?>> {
    private final RangeSet<C> rangeSet = TreeRangeSet.create();
    
    public Builder<C> add(Range<C> range) {
      if (range.isEmpty())
        throw new IllegalArgumentException("range must not be empty, but was " + range); 
      if (!this.rangeSet.complement().encloses(range)) {
        for (Range<C> currentRange : this.rangeSet.asRanges()) {
          Preconditions.checkArgument((!currentRange.isConnected(range) || currentRange.intersection(range).isEmpty()), "Ranges may not overlap, but received %s and %s", new Object[] { currentRange, range });
        } 
        throw new AssertionError("should have thrown an IAE above");
      } 
      this.rangeSet.add(range);
      return this;
    }
    
    public Builder<C> addAll(RangeSet<C> ranges) {
      for (Range<C> range : ranges.asRanges())
        add(range); 
      return this;
    }
    
    public ImmutableRangeSet<C> build() {
      return (ImmutableRangeSet)ImmutableRangeSet.copyOf(this.rangeSet);
    }
  }
  
  private static final class SerializedForm<C extends Comparable> implements Serializable {
    private final ImmutableList<Range<C>> ranges;
    
    SerializedForm(ImmutableList<Range<C>> ranges) {
      this.ranges = ranges;
    }
    
    Object readResolve() {
      if (this.ranges.isEmpty())
        return ImmutableRangeSet.of(); 
      if (this.ranges.equals(ImmutableList.of(Range.all())))
        return ImmutableRangeSet.all(); 
      return new ImmutableRangeSet<C>(this.ranges);
    }
  }
  
  Object writeReplace() {
    return new SerializedForm<C>(this.ranges);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableRangeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */