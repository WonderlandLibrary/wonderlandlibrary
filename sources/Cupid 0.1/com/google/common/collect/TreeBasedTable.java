package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
@Beta
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V> {
  private final Comparator<? super C> columnComparator;
  
  private static final long serialVersionUID = 0L;
  
  private static class Factory<C, V> implements Supplier<TreeMap<C, V>>, Serializable {
    final Comparator<? super C> comparator;
    
    private static final long serialVersionUID = 0L;
    
    Factory(Comparator<? super C> comparator) {
      this.comparator = comparator;
    }
    
    public TreeMap<C, V> get() {
      return new TreeMap<C, V>(this.comparator);
    }
  }
  
  public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
    return new TreeBasedTable<R, C, V>(Ordering.natural(), Ordering.natural());
  }
  
  public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
    Preconditions.checkNotNull(rowComparator);
    Preconditions.checkNotNull(columnComparator);
    return new TreeBasedTable<R, C, V>(rowComparator, columnComparator);
  }
  
  public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> table) {
    TreeBasedTable<R, C, V> result = new TreeBasedTable<R, C, V>(table.rowComparator(), table.columnComparator());
    result.putAll(table);
    return result;
  }
  
  TreeBasedTable(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
    super(new TreeMap<R, Map<C, V>>(rowComparator), (Supplier)new Factory<C, Object>(columnComparator));
    this.columnComparator = columnComparator;
  }
  
  public Comparator<? super R> rowComparator() {
    return rowKeySet().comparator();
  }
  
  public Comparator<? super C> columnComparator() {
    return this.columnComparator;
  }
  
  public SortedMap<C, V> row(R rowKey) {
    return new TreeRow(rowKey);
  }
  
  private class TreeRow extends StandardTable<R, C, V>.Row implements SortedMap<C, V> {
    @Nullable
    final C lowerBound;
    
    @Nullable
    final C upperBound;
    
    transient SortedMap<C, V> wholeRow;
    
    TreeRow(R rowKey) {
      this(rowKey, null, null);
    }
    
    TreeRow(@Nullable R rowKey, @Nullable C lowerBound, C upperBound) {
      super(rowKey);
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
      Preconditions.checkArgument((lowerBound == null || upperBound == null || compare(lowerBound, upperBound) <= 0));
    }
    
    public SortedSet<C> keySet() {
      return new Maps.SortedKeySet<C, Object>(this);
    }
    
    public Comparator<? super C> comparator() {
      return TreeBasedTable.this.columnComparator();
    }
    
    int compare(Object a, Object b) {
      Comparator<Object> cmp = (Comparator)comparator();
      return cmp.compare(a, b);
    }
    
    boolean rangeContains(@Nullable Object o) {
      return (o != null && (this.lowerBound == null || compare(this.lowerBound, o) <= 0) && (this.upperBound == null || compare(this.upperBound, o) > 0));
    }
    
    public SortedMap<C, V> subMap(C fromKey, C toKey) {
      Preconditions.checkArgument((rangeContains(Preconditions.checkNotNull(fromKey)) && rangeContains(Preconditions.checkNotNull(toKey))));
      return new TreeRow(this.rowKey, fromKey, toKey);
    }
    
    public SortedMap<C, V> headMap(C toKey) {
      Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(toKey)));
      return new TreeRow(this.rowKey, this.lowerBound, toKey);
    }
    
    public SortedMap<C, V> tailMap(C fromKey) {
      Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(fromKey)));
      return new TreeRow(this.rowKey, fromKey, this.upperBound);
    }
    
    public C firstKey() {
      SortedMap<C, V> backing = backingRowMap();
      if (backing == null)
        throw new NoSuchElementException(); 
      return backingRowMap().firstKey();
    }
    
    public C lastKey() {
      SortedMap<C, V> backing = backingRowMap();
      if (backing == null)
        throw new NoSuchElementException(); 
      return backingRowMap().lastKey();
    }
    
    SortedMap<C, V> wholeRow() {
      if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey)))
        this.wholeRow = (SortedMap<C, V>)TreeBasedTable.this.backingMap.get(this.rowKey); 
      return this.wholeRow;
    }
    
    SortedMap<C, V> backingRowMap() {
      return (SortedMap<C, V>)super.backingRowMap();
    }
    
    SortedMap<C, V> computeBackingRowMap() {
      SortedMap<C, V> map = wholeRow();
      if (map != null) {
        if (this.lowerBound != null)
          map = map.tailMap(this.lowerBound); 
        if (this.upperBound != null)
          map = map.headMap(this.upperBound); 
        return map;
      } 
      return null;
    }
    
    void maintainEmptyInvariant() {
      if (wholeRow() != null && this.wholeRow.isEmpty()) {
        TreeBasedTable.this.backingMap.remove(this.rowKey);
        this.wholeRow = null;
        this.backingRowMap = null;
      } 
    }
    
    public boolean containsKey(Object key) {
      return (rangeContains(key) && super.containsKey(key));
    }
    
    public V put(C key, V value) {
      Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(key)));
      return super.put(key, value);
    }
  }
  
  public SortedSet<R> rowKeySet() {
    return super.rowKeySet();
  }
  
  public SortedMap<R, Map<C, V>> rowMap() {
    return super.rowMap();
  }
  
  Iterator<C> createColumnKeyIterator() {
    final Comparator<? super C> comparator = columnComparator();
    final Iterator<C> merged = Iterators.mergeSorted(Iterables.transform(this.backingMap.values(), new Function<Map<C, V>, Iterator<C>>() {
            public Iterator<C> apply(Map<C, V> input) {
              return input.keySet().iterator();
            }
          }), comparator);
    return new AbstractIterator<C>() {
        C lastValue;
        
        protected C computeNext() {
          while (merged.hasNext()) {
            C next = merged.next();
            boolean duplicate = (this.lastValue != null && comparator.compare(next, this.lastValue) == 0);
            if (!duplicate) {
              this.lastValue = next;
              return this.lastValue;
            } 
          } 
          this.lastValue = null;
          return endOfData();
        }
      };
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\TreeBasedTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */