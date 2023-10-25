package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
class StandardRowSortedTable<R, C, V> extends StandardTable<R, C, V> implements RowSortedTable<R, C, V> {
  private static final long serialVersionUID = 0L;
  
  StandardRowSortedTable(SortedMap<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
    super(backingMap, factory);
  }
  
  private SortedMap<R, Map<C, V>> sortedBackingMap() {
    return (SortedMap<R, Map<C, V>>)this.backingMap;
  }
  
  public SortedSet<R> rowKeySet() {
    return (SortedSet<R>)rowMap().keySet();
  }
  
  public SortedMap<R, Map<C, V>> rowMap() {
    return (SortedMap<R, Map<C, V>>)super.rowMap();
  }
  
  SortedMap<R, Map<C, V>> createRowMap() {
    return new RowSortedMap();
  }
  
  private class RowSortedMap extends StandardTable<R, C, V>.RowMap implements SortedMap<R, Map<C, V>> {
    private RowSortedMap() {}
    
    public SortedSet<R> keySet() {
      return (SortedSet<R>)super.keySet();
    }
    
    SortedSet<R> createKeySet() {
      return new Maps.SortedKeySet<R, Object>(this);
    }
    
    public Comparator<? super R> comparator() {
      return StandardRowSortedTable.this.sortedBackingMap().comparator();
    }
    
    public R firstKey() {
      return (R)StandardRowSortedTable.this.sortedBackingMap().firstKey();
    }
    
    public R lastKey() {
      return (R)StandardRowSortedTable.this.sortedBackingMap().lastKey();
    }
    
    public SortedMap<R, Map<C, V>> headMap(R toKey) {
      Preconditions.checkNotNull(toKey);
      return (new StandardRowSortedTable<R, C, V>(StandardRowSortedTable.this.sortedBackingMap().headMap(toKey), StandardRowSortedTable.this.factory)).rowMap();
    }
    
    public SortedMap<R, Map<C, V>> subMap(R fromKey, R toKey) {
      Preconditions.checkNotNull(fromKey);
      Preconditions.checkNotNull(toKey);
      return (new StandardRowSortedTable<R, C, V>(StandardRowSortedTable.this.sortedBackingMap().subMap(fromKey, toKey), StandardRowSortedTable.this.factory)).rowMap();
    }
    
    public SortedMap<R, Map<C, V>> tailMap(R fromKey) {
      Preconditions.checkNotNull(fromKey);
      return (new StandardRowSortedTable<R, C, V>(StandardRowSortedTable.this.sortedBackingMap().tailMap(fromKey), StandardRowSortedTable.this.factory)).rowMap();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\StandardRowSortedTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */