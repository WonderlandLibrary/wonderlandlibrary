package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface Table<R, C, V> {
  boolean contains(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  boolean containsRow(@Nullable Object paramObject);
  
  boolean containsColumn(@Nullable Object paramObject);
  
  boolean containsValue(@Nullable Object paramObject);
  
  V get(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  boolean isEmpty();
  
  int size();
  
  boolean equals(@Nullable Object paramObject);
  
  int hashCode();
  
  void clear();
  
  V put(R paramR, C paramC, V paramV);
  
  void putAll(Table<? extends R, ? extends C, ? extends V> paramTable);
  
  V remove(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  Map<C, V> row(R paramR);
  
  Map<R, V> column(C paramC);
  
  Set<Cell<R, C, V>> cellSet();
  
  Set<R> rowKeySet();
  
  Set<C> columnKeySet();
  
  Collection<V> values();
  
  Map<R, Map<C, V>> rowMap();
  
  Map<C, Map<R, V>> columnMap();
  
  public static interface Cell<R, C, V> {
    R getRowKey();
    
    C getColumnKey();
    
    V getValue();
    
    boolean equals(@Nullable Object param1Object);
    
    int hashCode();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Table.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */