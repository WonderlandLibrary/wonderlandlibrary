package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible("uses NavigableMap")
public class TreeRangeSet<C extends Comparable<?>> extends AbstractRangeSet<C> {
  @VisibleForTesting
  final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
  
  private transient Set<Range<C>> asRanges;
  
  private transient RangeSet<C> complement;
  
  public static <C extends Comparable<?>> TreeRangeSet<C> create() {
    return new TreeRangeSet<C>(new TreeMap<Cut<C>, Range<C>>());
  }
  
  public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet) {
    TreeRangeSet<C> result = create();
    result.addAll(rangeSet);
    return result;
  }
  
  private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
    this.rangesByLowerBound = rangesByLowerCut;
  }
  
  public Set<Range<C>> asRanges() {
    Set<Range<C>> result = this.asRanges;
    return (result == null) ? (this.asRanges = new AsRanges()) : result;
  }
  
  final class AsRanges extends ForwardingCollection<Range<C>> implements Set<Range<C>> {
    protected Collection<Range<C>> delegate() {
      return TreeRangeSet.this.rangesByLowerBound.values();
    }
    
    public int hashCode() {
      return Sets.hashCodeImpl(this);
    }
    
    public boolean equals(@Nullable Object o) {
      return Sets.equalsImpl(this, o);
    }
  }
  
  @Nullable
  public Range<C> rangeContaining(C value) {
    Preconditions.checkNotNull(value);
    Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry((Cut)Cut.belowValue((Comparable)value));
    if (floorEntry != null && ((Range)floorEntry.getValue()).contains(value))
      return floorEntry.getValue(); 
    return null;
  }
  
  public boolean encloses(Range<C> range) {
    Preconditions.checkNotNull(range);
    Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
    return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range));
  }
  
  @Nullable
  private Range<C> rangeEnclosing(Range<C> range) {
    Preconditions.checkNotNull(range);
    Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
    return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range)) ? floorEntry.getValue() : null;
  }
  
  public Range<C> span() {
    Map.Entry<Cut<C>, Range<C>> firstEntry = this.rangesByLowerBound.firstEntry();
    Map.Entry<Cut<C>, Range<C>> lastEntry = this.rangesByLowerBound.lastEntry();
    if (firstEntry == null)
      throw new NoSuchElementException(); 
    return Range.create(((Range)firstEntry.getValue()).lowerBound, ((Range)lastEntry.getValue()).upperBound);
  }
  
  public void add(Range<C> rangeToAdd) {
    Preconditions.checkNotNull(rangeToAdd);
    if (rangeToAdd.isEmpty())
      return; 
    Cut<C> lbToAdd = rangeToAdd.lowerBound;
    Cut<C> ubToAdd = rangeToAdd.upperBound;
    Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(lbToAdd);
    if (entryBelowLB != null) {
      Range<C> rangeBelowLB = entryBelowLB.getValue();
      if (rangeBelowLB.upperBound.compareTo(lbToAdd) >= 0) {
        if (rangeBelowLB.upperBound.compareTo(ubToAdd) >= 0)
          ubToAdd = rangeBelowLB.upperBound; 
        lbToAdd = rangeBelowLB.lowerBound;
      } 
    } 
    Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(ubToAdd);
    if (entryBelowUB != null) {
      Range<C> rangeBelowUB = entryBelowUB.getValue();
      if (rangeBelowUB.upperBound.compareTo(ubToAdd) >= 0)
        ubToAdd = rangeBelowUB.upperBound; 
    } 
    this.rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
    replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
  }
  
  public void remove(Range<C> rangeToRemove) {
    Preconditions.checkNotNull(rangeToRemove);
    if (rangeToRemove.isEmpty())
      return; 
    Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
    if (entryBelowLB != null) {
      Range<C> rangeBelowLB = entryBelowLB.getValue();
      if (rangeBelowLB.upperBound.compareTo(rangeToRemove.lowerBound) >= 0) {
        if (rangeToRemove.hasUpperBound() && rangeBelowLB.upperBound.compareTo(rangeToRemove.upperBound) >= 0)
          replaceRangeWithSameLowerBound(Range.create(rangeToRemove.upperBound, rangeBelowLB.upperBound)); 
        replaceRangeWithSameLowerBound(Range.create(rangeBelowLB.lowerBound, rangeToRemove.lowerBound));
      } 
    } 
    Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(rangeToRemove.upperBound);
    if (entryBelowUB != null) {
      Range<C> rangeBelowUB = entryBelowUB.getValue();
      if (rangeToRemove.hasUpperBound() && rangeBelowUB.upperBound.compareTo(rangeToRemove.upperBound) >= 0)
        replaceRangeWithSameLowerBound(Range.create(rangeToRemove.upperBound, rangeBelowUB.upperBound)); 
    } 
    this.rangesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
  }
  
  private void replaceRangeWithSameLowerBound(Range<C> range) {
    if (range.isEmpty()) {
      this.rangesByLowerBound.remove(range.lowerBound);
    } else {
      this.rangesByLowerBound.put(range.lowerBound, range);
    } 
  }
  
  public RangeSet<C> complement() {
    RangeSet<C> result = this.complement;
    return (result == null) ? (this.complement = new Complement()) : result;
  }
  
  @VisibleForTesting
  static final class RangesByUpperBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
    private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
    
    private final Range<Cut<C>> upperBoundWindow;
    
    RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
      this.rangesByLowerBound = rangesByLowerBound;
      this.upperBoundWindow = Range.all();
    }
    
    private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, Range<Cut<C>> upperBoundWindow) {
      this.rangesByLowerBound = rangesByLowerBound;
      this.upperBoundWindow = upperBoundWindow;
    }
    
    private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
      if (window.isConnected(this.upperBoundWindow))
        return new RangesByUpperBound(this.rangesByLowerBound, window.intersection(this.upperBoundWindow)); 
      return ImmutableSortedMap.of();
    }
    
    public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
      return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
      return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
      return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
    }
    
    public Comparator<? super Cut<C>> comparator() {
      return Ordering.natural();
    }
    
    public boolean containsKey(@Nullable Object key) {
      return (get(key) != null);
    }
    
    public Range<C> get(@Nullable Object key) {
      if (key instanceof Cut)
        try {
          Cut<C> cut = (Cut<C>)key;
          if (!this.upperBoundWindow.contains(cut))
            return null; 
          Map.Entry<Cut<C>, Range<C>> candidate = this.rangesByLowerBound.lowerEntry(cut);
          if (candidate != null && ((Range)candidate.getValue()).upperBound.equals(cut))
            return candidate.getValue(); 
        } catch (ClassCastException e) {
          return null;
        }  
      return null;
    }
    
    Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
      final Iterator<Range<C>> backingItr;
      if (!this.upperBoundWindow.hasLowerBound()) {
        backingItr = this.rangesByLowerBound.values().iterator();
      } else {
        Map.Entry<Cut<C>, Range<C>> lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
        if (lowerEntry == null) {
          backingItr = this.rangesByLowerBound.values().iterator();
        } else if (this.upperBoundWindow.lowerBound.isLessThan(((Range)lowerEntry.getValue()).upperBound)) {
          backingItr = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
        } else {
          backingItr = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
        } 
      } 
      return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
          protected Map.Entry<Cut<C>, Range<C>> computeNext() {
            if (!backingItr.hasNext())
              return endOfData(); 
            Range<C> range = backingItr.next();
            if (TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound))
              return endOfData(); 
            return Maps.immutableEntry(range.upperBound, range);
          }
        };
    }
    
    Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
      Collection<Range<C>> candidates;
      if (this.upperBoundWindow.hasUpperBound()) {
        candidates = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
      } else {
        candidates = this.rangesByLowerBound.descendingMap().values();
      } 
      final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator(candidates.iterator());
      if (backingItr.hasNext() && this.upperBoundWindow.upperBound.isLessThan(((Range)backingItr.peek()).upperBound))
        backingItr.next(); 
      return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
          protected Map.Entry<Cut<C>, Range<C>> computeNext() {
            if (!backingItr.hasNext())
              return endOfData(); 
            Range<C> range = backingItr.next();
            return TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound) ? Maps.<Cut<C>, Range<C>>immutableEntry(range.upperBound, range) : endOfData();
          }
        };
    }
    
    public int size() {
      if (this.upperBoundWindow.equals(Range.all()))
        return this.rangesByLowerBound.size(); 
      return Iterators.size(entryIterator());
    }
    
    public boolean isEmpty() {
      return this.upperBoundWindow.equals(Range.all()) ? this.rangesByLowerBound.isEmpty() : (!entryIterator().hasNext());
    }
  }
  
  private static final class ComplementRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
    private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
    
    private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
    
    private final Range<Cut<C>> complementLowerBoundWindow;
    
    ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound) {
      this(positiveRangesByLowerBound, Range.all());
    }
    
    private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, Range<Cut<C>> window) {
      this.positiveRangesByLowerBound = positiveRangesByLowerBound;
      this.positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound<C>(positiveRangesByLowerBound);
      this.complementLowerBoundWindow = window;
    }
    
    private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
      if (!this.complementLowerBoundWindow.isConnected(subWindow))
        return ImmutableSortedMap.of(); 
      subWindow = subWindow.intersection(this.complementLowerBoundWindow);
      return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, subWindow);
    }
    
    public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
      return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
      return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
      return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
    }
    
    public Comparator<? super Cut<C>> comparator() {
      return Ordering.natural();
    }
    
    Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
      Collection<Range<C>> positiveRanges;
      final Cut<C> firstComplementRangeLowerBound;
      if (this.complementLowerBoundWindow.hasLowerBound()) {
        positiveRanges = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), (this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values();
      } else {
        positiveRanges = this.positiveRangesByUpperBound.values();
      } 
      final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRanges.iterator());
      if (this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) && (!positiveItr.hasNext() || ((Range)positiveItr.peek()).lowerBound != Cut.belowAll())) {
        firstComplementRangeLowerBound = Cut.belowAll();
      } else if (positiveItr.hasNext()) {
        firstComplementRangeLowerBound = ((Range)positiveItr.next()).upperBound;
      } else {
        return Iterators.emptyIterator();
      } 
      return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
          Cut<C> nextComplementRangeLowerBound = firstComplementRangeLowerBound;
          
          protected Map.Entry<Cut<C>, Range<C>> computeNext() {
            Range<C> negativeRange;
            if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound) || this.nextComplementRangeLowerBound == Cut.aboveAll())
              return endOfData(); 
            if (positiveItr.hasNext()) {
              Range<C> positiveRange = positiveItr.next();
              negativeRange = Range.create(this.nextComplementRangeLowerBound, positiveRange.lowerBound);
              this.nextComplementRangeLowerBound = positiveRange.upperBound;
            } else {
              negativeRange = Range.create(this.nextComplementRangeLowerBound, (Cut)Cut.aboveAll());
              this.nextComplementRangeLowerBound = Cut.aboveAll();
            } 
            return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
          }
        };
    }
    
    Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
      Cut<C> cut, startingPoint = this.complementLowerBoundWindow.hasUpperBound() ? this.complementLowerBoundWindow.upperEndpoint() : (Cut)Cut.<Comparable>aboveAll();
      boolean inclusive = (this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED);
      final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(this.positiveRangesByUpperBound.headMap(startingPoint, inclusive).descendingMap().values().iterator());
      if (positiveItr.hasNext()) {
        cut = (((Range)positiveItr.peek()).upperBound == Cut.aboveAll()) ? ((Range)positiveItr.next()).lowerBound : this.positiveRangesByLowerBound.higherKey(((Range)positiveItr.peek()).upperBound);
      } else {
        if (!this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) || this.positiveRangesByLowerBound.containsKey(Cut.belowAll()))
          return Iterators.emptyIterator(); 
        cut = this.positiveRangesByLowerBound.higherKey((Cut)Cut.belowAll());
      } 
      final Cut<C> firstComplementRangeUpperBound = (Cut<C>)Objects.firstNonNull(cut, Cut.aboveAll());
      return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
          Cut<C> nextComplementRangeUpperBound = firstComplementRangeUpperBound;
          
          protected Map.Entry<Cut<C>, Range<C>> computeNext() {
            if (this.nextComplementRangeUpperBound == Cut.belowAll())
              return endOfData(); 
            if (positiveItr.hasNext()) {
              Range<C> positiveRange = positiveItr.next();
              Range<C> negativeRange = Range.create(positiveRange.upperBound, this.nextComplementRangeUpperBound);
              this.nextComplementRangeUpperBound = positiveRange.lowerBound;
              if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(negativeRange.lowerBound))
                return Maps.immutableEntry(negativeRange.lowerBound, negativeRange); 
            } else if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
              Range<C> negativeRange = Range.create((Cut)Cut.belowAll(), this.nextComplementRangeUpperBound);
              this.nextComplementRangeUpperBound = Cut.belowAll();
              return Maps.immutableEntry((Cut)Cut.belowAll(), negativeRange);
            } 
            return endOfData();
          }
        };
    }
    
    public int size() {
      return Iterators.size(entryIterator());
    }
    
    @Nullable
    public Range<C> get(Object key) {
      if (key instanceof Cut)
        try {
          Cut<C> cut = (Cut<C>)key;
          Map.Entry<Cut<C>, Range<C>> firstEntry = tailMap(cut, true).firstEntry();
          if (firstEntry != null && ((Cut)firstEntry.getKey()).equals(cut))
            return firstEntry.getValue(); 
        } catch (ClassCastException e) {
          return null;
        }  
      return null;
    }
    
    public boolean containsKey(Object key) {
      return (get(key) != null);
    }
  }
  
  private final class Complement extends TreeRangeSet<C> {
    Complement() {
      super(new TreeRangeSet.ComplementRangesByLowerBound<Comparable<?>>(TreeRangeSet.this.rangesByLowerBound));
    }
    
    public void add(Range<C> rangeToAdd) {
      TreeRangeSet.this.remove(rangeToAdd);
    }
    
    public void remove(Range<C> rangeToRemove) {
      TreeRangeSet.this.add(rangeToRemove);
    }
    
    public boolean contains(C value) {
      return !TreeRangeSet.this.contains((Comparable)value);
    }
    
    public RangeSet<C> complement() {
      return TreeRangeSet.this;
    }
  }
  
  private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
    private final Range<Cut<C>> lowerBoundWindow;
    
    private final Range<C> restriction;
    
    private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
    
    private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
    
    private SubRangeSetRangesByLowerBound(Range<Cut<C>> lowerBoundWindow, Range<C> restriction, NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
      this.lowerBoundWindow = (Range<Cut<C>>)Preconditions.checkNotNull(lowerBoundWindow);
      this.restriction = (Range<C>)Preconditions.checkNotNull(restriction);
      this.rangesByLowerBound = (NavigableMap<Cut<C>, Range<C>>)Preconditions.checkNotNull(rangesByLowerBound);
      this.rangesByUpperBound = new TreeRangeSet.RangesByUpperBound<C>(rangesByLowerBound);
    }
    
    private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
      if (!window.isConnected(this.lowerBoundWindow))
        return ImmutableSortedMap.of(); 
      return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow.intersection(window), this.restriction, this.rangesByLowerBound);
    }
    
    public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
      return subMap(Range.range(fromKey, BoundType.forBoolean(fromInclusive), toKey, BoundType.forBoolean(toInclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
      return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
      return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
    }
    
    public Comparator<? super Cut<C>> comparator() {
      return Ordering.natural();
    }
    
    public boolean containsKey(@Nullable Object key) {
      return (get(key) != null);
    }
    
    @Nullable
    public Range<C> get(@Nullable Object key) {
      if (key instanceof Cut)
        try {
          Cut<C> cut = (Cut<C>)key;
          if (!this.lowerBoundWindow.contains(cut) || cut.compareTo(this.restriction.lowerBound) < 0 || cut.compareTo(this.restriction.upperBound) >= 0)
            return null; 
          if (cut.equals(this.restriction.lowerBound)) {
            Range<C> candidate = Maps.<Range<C>>valueOrNull(this.rangesByLowerBound.floorEntry(cut));
            if (candidate != null && candidate.upperBound.compareTo(this.restriction.lowerBound) > 0)
              return candidate.intersection(this.restriction); 
          } else {
            Range<C> result = this.rangesByLowerBound.get(cut);
            if (result != null)
              return result.intersection(this.restriction); 
          } 
        } catch (ClassCastException e) {
          return null;
        }  
      return null;
    }
    
    Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
      final Iterator<Range<C>> completeRangeItr;
      if (this.restriction.isEmpty())
        return Iterators.emptyIterator(); 
      if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound))
        return Iterators.emptyIterator(); 
      if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
        completeRangeItr = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
      } else {
        completeRangeItr = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), (this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values().iterator();
      } 
      final Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
      return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
          protected Map.Entry<Cut<C>, Range<C>> computeNext() {
            if (!completeRangeItr.hasNext())
              return endOfData(); 
            Range<C> nextRange = completeRangeItr.next();
            if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound))
              return endOfData(); 
            nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
            return Maps.immutableEntry(nextRange.lowerBound, nextRange);
          }
        };
    }
    
    Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
      if (this.restriction.isEmpty())
        return Iterators.emptyIterator(); 
      Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
      final Iterator<Range<C>> completeRangeItr = this.rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), (upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED)).descendingMap().values().iterator();
      return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
          protected Map.Entry<Cut<C>, Range<C>> computeNext() {
            if (!completeRangeItr.hasNext())
              return endOfData(); 
            Range<C> nextRange = completeRangeItr.next();
            if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo(nextRange.upperBound) >= 0)
              return endOfData(); 
            nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
            if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(nextRange.lowerBound))
              return Maps.immutableEntry(nextRange.lowerBound, nextRange); 
            return endOfData();
          }
        };
    }
    
    public int size() {
      return Iterators.size(entryIterator());
    }
  }
  
  public RangeSet<C> subRangeSet(Range<C> view) {
    return view.equals(Range.all()) ? this : new SubRangeSet(view);
  }
  
  private final class SubRangeSet extends TreeRangeSet<C> {
    private final Range<C> restriction;
    
    SubRangeSet(Range<C> restriction) {
      super(new TreeRangeSet.SubRangeSetRangesByLowerBound<Comparable<?>>(Range.all(), restriction, TreeRangeSet.this.rangesByLowerBound, null));
      this.restriction = restriction;
    }
    
    public boolean encloses(Range<C> range) {
      if (!this.restriction.isEmpty() && this.restriction.encloses(range)) {
        Range<C> enclosing = TreeRangeSet.this.rangeEnclosing(range);
        return (enclosing != null && !enclosing.intersection(this.restriction).isEmpty());
      } 
      return false;
    }
    
    @Nullable
    public Range<C> rangeContaining(C value) {
      if (!this.restriction.contains(value))
        return null; 
      Range<C> result = TreeRangeSet.this.rangeContaining(value);
      return (result == null) ? null : result.intersection(this.restriction);
    }
    
    public void add(Range<C> rangeToAdd) {
      Preconditions.checkArgument(this.restriction.encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", new Object[] { rangeToAdd, this.restriction });
      super.add(rangeToAdd);
    }
    
    public void remove(Range<C> rangeToRemove) {
      if (rangeToRemove.isConnected(this.restriction))
        TreeRangeSet.this.remove(rangeToRemove.intersection(this.restriction)); 
    }
    
    public boolean contains(C value) {
      return (this.restriction.contains(value) && TreeRangeSet.this.contains((Comparable)value));
    }
    
    public void clear() {
      TreeRangeSet.this.remove(this.restriction);
    }
    
    public RangeSet<C> subRangeSet(Range<C> view) {
      if (view.encloses(this.restriction))
        return this; 
      if (view.isConnected(this.restriction))
        return new SubRangeSet(this.restriction.intersection(view)); 
      return (RangeSet)ImmutableRangeSet.of();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\TreeRangeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */