package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible("NavigableMap")
public final class TreeRangeMap<K extends Comparable, V> implements RangeMap<K, V> {
  private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;
  
  public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
    return new TreeRangeMap<K, V>();
  }
  
  private TreeRangeMap() {
    this.entriesByLowerBound = Maps.newTreeMap();
  }
  
  private static final class RangeMapEntry<K extends Comparable, V> extends AbstractMapEntry<Range<K>, V> {
    private final Range<K> range;
    
    private final V value;
    
    RangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
      this(Range.create(lowerBound, upperBound), value);
    }
    
    RangeMapEntry(Range<K> range, V value) {
      this.range = range;
      this.value = value;
    }
    
    public Range<K> getKey() {
      return this.range;
    }
    
    public V getValue() {
      return this.value;
    }
    
    public boolean contains(K value) {
      return this.range.contains(value);
    }
    
    Cut<K> getLowerBound() {
      return this.range.lowerBound;
    }
    
    Cut<K> getUpperBound() {
      return this.range.upperBound;
    }
  }
  
  @Nullable
  public V get(K key) {
    Map.Entry<Range<K>, V> entry = getEntry(key);
    return (entry == null) ? null : entry.getValue();
  }
  
  @Nullable
  public Map.Entry<Range<K>, V> getEntry(K key) {
    Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(key));
    if (mapEntry != null && ((RangeMapEntry)mapEntry.getValue()).contains(key))
      return mapEntry.getValue(); 
    return null;
  }
  
  public void put(Range<K> range, V value) {
    if (!range.isEmpty()) {
      Preconditions.checkNotNull(value);
      remove(range);
      this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry<K, V>(range, value));
    } 
  }
  
  public void putAll(RangeMap<K, V> rangeMap) {
    for (Map.Entry<Range<K>, V> entry : (Iterable<Map.Entry<Range<K>, V>>)rangeMap.asMapOfRanges().entrySet())
      put(entry.getKey(), entry.getValue()); 
  }
  
  public void clear() {
    this.entriesByLowerBound.clear();
  }
  
  public Range<K> span() {
    Map.Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = this.entriesByLowerBound.firstEntry();
    Map.Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = this.entriesByLowerBound.lastEntry();
    if (firstEntry == null)
      throw new NoSuchElementException(); 
    return Range.create((((RangeMapEntry)firstEntry.getValue()).getKey()).lowerBound, (((RangeMapEntry)lastEntry.getValue()).getKey()).upperBound);
  }
  
  private void putRangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
    this.entriesByLowerBound.put(lowerBound, new RangeMapEntry<K, V>(lowerBound, upperBound, value));
  }
  
  public void remove(Range<K> rangeToRemove) {
    if (rangeToRemove.isEmpty())
      return; 
    Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
    if (mapEntryBelowToTruncate != null) {
      RangeMapEntry<K, V> rangeMapEntry = mapEntryBelowToTruncate.getValue();
      if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
        if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
          putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(), (V)((RangeMapEntry)mapEntryBelowToTruncate.getValue()).getValue()); 
        putRangeMapEntry(rangeMapEntry.getLowerBound(), rangeToRemove.lowerBound, (V)((RangeMapEntry)mapEntryBelowToTruncate.getValue()).getValue());
      } 
    } 
    Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
    if (mapEntryAboveToTruncate != null) {
      RangeMapEntry<K, V> rangeMapEntry = mapEntryAboveToTruncate.getValue();
      if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
        putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(), (V)((RangeMapEntry)mapEntryAboveToTruncate.getValue()).getValue());
        this.entriesByLowerBound.remove(rangeToRemove.lowerBound);
      } 
    } 
    this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
  }
  
  public Map<Range<K>, V> asMapOfRanges() {
    return new AsMapOfRanges();
  }
  
  private final class AsMapOfRanges extends AbstractMap<Range<K>, V> {
    private AsMapOfRanges() {}
    
    public boolean containsKey(@Nullable Object key) {
      return (get(key) != null);
    }
    
    public V get(@Nullable Object key) {
      if (key instanceof Range) {
        Range<?> range = (Range)key;
        TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
        if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range))
          return rangeMapEntry.getValue(); 
      } 
      return null;
    }
    
    public Set<Map.Entry<Range<K>, V>> entrySet() {
      return new AbstractSet<Map.Entry<Range<K>, V>>() {
          public Iterator<Map.Entry<Range<K>, V>> iterator() {
            return TreeRangeMap.this.entriesByLowerBound.values().iterator();
          }
          
          public int size() {
            return TreeRangeMap.this.entriesByLowerBound.size();
          }
        };
    }
  }
  
  public RangeMap<K, V> subRangeMap(Range<K> subRange) {
    if (subRange.equals(Range.all()))
      return this; 
    return new SubRangeMap(subRange);
  }
  
  private RangeMap<K, V> emptySubRangeMap() {
    return EMPTY_SUB_RANGE_MAP;
  }
  
  private static final RangeMap EMPTY_SUB_RANGE_MAP = new RangeMap<Comparable, Object>() {
      @Nullable
      public Object get(Comparable key) {
        return null;
      }
      
      @Nullable
      public Map.Entry<Range, Object> getEntry(Comparable key) {
        return null;
      }
      
      public Range span() {
        throw new NoSuchElementException();
      }
      
      public void put(Range range, Object value) {
        Preconditions.checkNotNull(range);
        throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
      }
      
      public void putAll(RangeMap rangeMap) {
        if (!rangeMap.asMapOfRanges().isEmpty())
          throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap"); 
      }
      
      public void clear() {}
      
      public void remove(Range range) {
        Preconditions.checkNotNull(range);
      }
      
      public Map<Range, Object> asMapOfRanges() {
        return Collections.emptyMap();
      }
      
      public RangeMap subRangeMap(Range range) {
        Preconditions.checkNotNull(range);
        return this;
      }
    };
  
  private class SubRangeMap implements RangeMap<K, V> {
    private final Range<K> subRange;
    
    SubRangeMap(Range<K> subRange) {
      this.subRange = subRange;
    }
    
    @Nullable
    public V get(K key) {
      return this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
    }
    
    @Nullable
    public Map.Entry<Range<K>, V> getEntry(K key) {
      if (this.subRange.contains(key)) {
        Map.Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
        if (entry != null)
          return Maps.immutableEntry(((Range<K>)entry.getKey()).intersection(this.subRange), entry.getValue()); 
      } 
      return null;
    }
    
    public Range<K> span() {
      Cut<K> lowerBound, upperBound;
      Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lowerEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
      if (lowerEntry != null && ((TreeRangeMap.RangeMapEntry)lowerEntry.getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
        lowerBound = this.subRange.lowerBound;
      } else {
        lowerBound = (Cut<K>)TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
        if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0)
          throw new NoSuchElementException(); 
      } 
      Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> upperEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
      if (upperEntry == null)
        throw new NoSuchElementException(); 
      if (((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
        upperBound = this.subRange.upperBound;
      } else {
        upperBound = ((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound();
      } 
      return Range.create(lowerBound, upperBound);
    }
    
    public void put(Range<K> range, V value) {
      Preconditions.checkArgument(this.subRange.encloses(range), "Cannot put range %s into a subRangeMap(%s)", new Object[] { range, this.subRange });
      TreeRangeMap.this.put(range, value);
    }
    
    public void putAll(RangeMap<K, V> rangeMap) {
      if (rangeMap.asMapOfRanges().isEmpty())
        return; 
      Range<K> span = rangeMap.span();
      Preconditions.checkArgument(this.subRange.encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", new Object[] { span, this.subRange });
      TreeRangeMap.this.putAll(rangeMap);
    }
    
    public void clear() {
      TreeRangeMap.this.remove(this.subRange);
    }
    
    public void remove(Range<K> range) {
      if (range.isConnected(this.subRange))
        TreeRangeMap.this.remove(range.intersection(this.subRange)); 
    }
    
    public RangeMap<K, V> subRangeMap(Range<K> range) {
      if (!range.isConnected(this.subRange))
        return TreeRangeMap.this.emptySubRangeMap(); 
      return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
    }
    
    public Map<Range<K>, V> asMapOfRanges() {
      return new SubRangeMapAsMap();
    }
    
    public boolean equals(@Nullable Object o) {
      if (o instanceof RangeMap) {
        RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
        return asMapOfRanges().equals(rangeMap.asMapOfRanges());
      } 
      return false;
    }
    
    public int hashCode() {
      return asMapOfRanges().hashCode();
    }
    
    public String toString() {
      return asMapOfRanges().toString();
    }
    
    class SubRangeMapAsMap extends AbstractMap<Range<K>, V> {
      public boolean containsKey(Object key) {
        return (get(key) != null);
      }
      
      public V get(Object key) {
        try {
          if (key instanceof Range) {
            Range<K> r = (Range<K>)key;
            if (!TreeRangeMap.SubRangeMap.this.subRange.encloses(r) || r.isEmpty())
              return null; 
            TreeRangeMap.RangeMapEntry<K, V> candidate = null;
            if (r.lowerBound.compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) == 0) {
              Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry = TreeRangeMap.this.entriesByLowerBound.floorEntry(r.lowerBound);
              if (entry != null)
                candidate = entry.getValue(); 
            } else {
              candidate = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(r.lowerBound);
            } 
            if (candidate != null && candidate.getKey().isConnected(TreeRangeMap.SubRangeMap.this.subRange) && candidate.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange).equals(r))
              return candidate.getValue(); 
          } 
        } catch (ClassCastException e) {
          return null;
        } 
        return null;
      }
      
      public V remove(Object key) {
        V value = get(key);
        if (value != null) {
          Range<K> range = (Range<K>)key;
          TreeRangeMap.this.remove(range);
          return value;
        } 
        return null;
      }
      
      public void clear() {
        TreeRangeMap.SubRangeMap.this.clear();
      }
      
      private boolean removeEntryIf(Predicate<? super Map.Entry<Range<K>, V>> predicate) {
        List<Range<K>> toRemove = Lists.newArrayList();
        for (Map.Entry<Range<K>, V> entry : entrySet()) {
          if (predicate.apply(entry))
            toRemove.add(entry.getKey()); 
        } 
        for (Range<K> range : toRemove)
          TreeRangeMap.this.remove(range); 
        return !toRemove.isEmpty();
      }
      
      public Set<Range<K>> keySet() {
        return (Set)new Maps.KeySet<Range<Range<K>>, V>(this) {
            public boolean remove(@Nullable Object o) {
              return (TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.remove(o) != null);
            }
            
            public boolean retainAll(Collection<?> c) {
              return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.keyFunction()));
            }
          };
      }
      
      public Set<Map.Entry<Range<K>, V>> entrySet() {
        return (Set)new Maps.EntrySet<Range<Range<K>>, V>() {
            Map<Range<K>, V> map() {
              return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this;
            }
            
            public Iterator<Map.Entry<Range<K>, V>> iterator() {
              if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty())
                return Iterators.emptyIterator(); 
              Cut<K> cutToStart = (Cut<K>)Objects.firstNonNull(TreeRangeMap.this.entriesByLowerBound.floorKey(TreeRangeMap.SubRangeMap.this.subRange.lowerBound), TreeRangeMap.SubRangeMap.this.subRange.lowerBound);
              final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
              return new AbstractIterator<Map.Entry<Range<K>, V>>() {
                  protected Map.Entry<Range<K>, V> computeNext() {
                    while (backingItr.hasNext()) {
                      TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
                      if (entry.getLowerBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.upperBound) >= 0)
                        break; 
                      if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) > 0)
                        return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue()); 
                    } 
                    return endOfData();
                  }
                };
            }
            
            public boolean retainAll(Collection<?> c) {
              return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(c)));
            }
            
            public int size() {
              return Iterators.size(iterator());
            }
            
            public boolean isEmpty() {
              return !iterator().hasNext();
            }
          };
      }
      
      public Collection<V> values() {
        return new Maps.Values<Range<Range<K>>, V>(this) {
            public boolean removeAll(Collection<?> c) {
              return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
            }
            
            public boolean retainAll(Collection<?> c) {
              return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction()));
            }
          };
      }
    }
  }
  
  public boolean equals(@Nullable Object o) {
    if (o instanceof RangeMap) {
      RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
      return asMapOfRanges().equals(rangeMap.asMapOfRanges());
    } 
    return false;
  }
  
  public int hashCode() {
    return asMapOfRanges().hashCode();
  }
  
  public String toString() {
    return this.entriesByLowerBound.values().toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\TreeRangeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */