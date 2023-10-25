package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public abstract class ForwardingTable<R, C, V> extends ForwardingObject implements Table<R, C, V> {
  public Set<Table.Cell<R, C, V>> cellSet() {
    return delegate().cellSet();
  }
  
  public void clear() {
    delegate().clear();
  }
  
  public Map<R, V> column(C columnKey) {
    return delegate().column(columnKey);
  }
  
  public Set<C> columnKeySet() {
    return delegate().columnKeySet();
  }
  
  public Map<C, Map<R, V>> columnMap() {
    return delegate().columnMap();
  }
  
  public boolean contains(Object rowKey, Object columnKey) {
    return delegate().contains(rowKey, columnKey);
  }
  
  public boolean containsColumn(Object columnKey) {
    return delegate().containsColumn(columnKey);
  }
  
  public boolean containsRow(Object rowKey) {
    return delegate().containsRow(rowKey);
  }
  
  public boolean containsValue(Object value) {
    return delegate().containsValue(value);
  }
  
  public V get(Object rowKey, Object columnKey) {
    return delegate().get(rowKey, columnKey);
  }
  
  public boolean isEmpty() {
    return delegate().isEmpty();
  }
  
  public V put(R rowKey, C columnKey, V value) {
    return delegate().put(rowKey, columnKey, value);
  }
  
  public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
    delegate().putAll(table);
  }
  
  public V remove(Object rowKey, Object columnKey) {
    return delegate().remove(rowKey, columnKey);
  }
  
  public Map<C, V> row(R rowKey) {
    return delegate().row(rowKey);
  }
  
  public Set<R> rowKeySet() {
    return delegate().rowKeySet();
  }
  
  public Map<R, Map<C, V>> rowMap() {
    return delegate().rowMap();
  }
  
  public int size() {
    return delegate().size();
  }
  
  public Collection<V> values() {
    return delegate().values();
  }
  
  public boolean equals(Object obj) {
    return (obj == this || delegate().equals(obj));
  }
  
  public int hashCode() {
    return delegate().hashCode();
  }
  
  protected abstract Table<R, C, V> delegate();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ForwardingTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */