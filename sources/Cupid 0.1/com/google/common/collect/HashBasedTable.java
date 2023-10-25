package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
public class HashBasedTable<R, C, V> extends StandardTable<R, C, V> {
  private static final long serialVersionUID = 0L;
  
  private static class Factory<C, V> implements Supplier<Map<C, V>>, Serializable {
    final int expectedSize;
    
    private static final long serialVersionUID = 0L;
    
    Factory(int expectedSize) {
      this.expectedSize = expectedSize;
    }
    
    public Map<C, V> get() {
      return Maps.newHashMapWithExpectedSize(this.expectedSize);
    }
  }
  
  public static <R, C, V> HashBasedTable<R, C, V> create() {
    return new HashBasedTable<R, C, V>(new HashMap<R, Map<C, V>>(), new Factory<C, V>(0));
  }
  
  public static <R, C, V> HashBasedTable<R, C, V> create(int expectedRows, int expectedCellsPerRow) {
    CollectPreconditions.checkNonnegative(expectedCellsPerRow, "expectedCellsPerRow");
    Map<R, Map<C, V>> backingMap = Maps.newHashMapWithExpectedSize(expectedRows);
    return new HashBasedTable<R, C, V>(backingMap, new Factory<C, V>(expectedCellsPerRow));
  }
  
  public static <R, C, V> HashBasedTable<R, C, V> create(Table<? extends R, ? extends C, ? extends V> table) {
    HashBasedTable<R, C, V> result = create();
    result.putAll(table);
    return result;
  }
  
  HashBasedTable(Map<R, Map<C, V>> backingMap, Factory<C, V> factory) {
    super(backingMap, factory);
  }
  
  public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
    return super.contains(rowKey, columnKey);
  }
  
  public boolean containsColumn(@Nullable Object columnKey) {
    return super.containsColumn(columnKey);
  }
  
  public boolean containsRow(@Nullable Object rowKey) {
    return super.containsRow(rowKey);
  }
  
  public boolean containsValue(@Nullable Object value) {
    return super.containsValue(value);
  }
  
  public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
    return super.get(rowKey, columnKey);
  }
  
  public boolean equals(@Nullable Object obj) {
    return super.equals(obj);
  }
  
  public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
    return super.remove(rowKey, columnKey);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\HashBasedTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */