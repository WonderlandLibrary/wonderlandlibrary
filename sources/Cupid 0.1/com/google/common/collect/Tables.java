package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public final class Tables {
  public static <R, C, V> Table.Cell<R, C, V> immutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
    return new ImmutableCell<R, C, V>(rowKey, columnKey, value);
  }
  
  static final class ImmutableCell<R, C, V> extends AbstractCell<R, C, V> implements Serializable {
    private final R rowKey;
    
    private final C columnKey;
    
    private final V value;
    
    private static final long serialVersionUID = 0L;
    
    ImmutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
      this.rowKey = rowKey;
      this.columnKey = columnKey;
      this.value = value;
    }
    
    public R getRowKey() {
      return this.rowKey;
    }
    
    public C getColumnKey() {
      return this.columnKey;
    }
    
    public V getValue() {
      return this.value;
    }
  }
  
  static abstract class AbstractCell<R, C, V> implements Table.Cell<R, C, V> {
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (obj instanceof Table.Cell) {
        Table.Cell<?, ?, ?> other = (Table.Cell<?, ?, ?>)obj;
        return (Objects.equal(getRowKey(), other.getRowKey()) && Objects.equal(getColumnKey(), other.getColumnKey()) && Objects.equal(getValue(), other.getValue()));
      } 
      return false;
    }
    
    public int hashCode() {
      return Objects.hashCode(new Object[] { getRowKey(), getColumnKey(), getValue() });
    }
    
    public String toString() {
      return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
    }
  }
  
  public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
    return (table instanceof TransposeTable) ? ((TransposeTable)table).original : new TransposeTable<C, R, V>(table);
  }
  
  private static class TransposeTable<C, R, V> extends AbstractTable<C, R, V> {
    final Table<R, C, V> original;
    
    TransposeTable(Table<R, C, V> original) {
      this.original = (Table<R, C, V>)Preconditions.checkNotNull(original);
    }
    
    public void clear() {
      this.original.clear();
    }
    
    public Map<C, V> column(R columnKey) {
      return this.original.row(columnKey);
    }
    
    public Set<R> columnKeySet() {
      return this.original.rowKeySet();
    }
    
    public Map<R, Map<C, V>> columnMap() {
      return this.original.rowMap();
    }
    
    public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
      return this.original.contains(columnKey, rowKey);
    }
    
    public boolean containsColumn(@Nullable Object columnKey) {
      return this.original.containsRow(columnKey);
    }
    
    public boolean containsRow(@Nullable Object rowKey) {
      return this.original.containsColumn(rowKey);
    }
    
    public boolean containsValue(@Nullable Object value) {
      return this.original.containsValue(value);
    }
    
    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
      return this.original.get(columnKey, rowKey);
    }
    
    public V put(C rowKey, R columnKey, V value) {
      return this.original.put(columnKey, rowKey, value);
    }
    
    public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
      this.original.putAll(Tables.transpose(table));
    }
    
    public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
      return this.original.remove(columnKey, rowKey);
    }
    
    public Map<R, V> row(C rowKey) {
      return this.original.column(rowKey);
    }
    
    public Set<C> rowKeySet() {
      return this.original.columnKeySet();
    }
    
    public Map<C, Map<R, V>> rowMap() {
      return this.original.columnMap();
    }
    
    public int size() {
      return this.original.size();
    }
    
    public Collection<V> values() {
      return this.original.values();
    }
    
    private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>() {
        public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> cell) {
          return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
        }
      };
    
    Iterator<Table.Cell<C, R, V>> cellIterator() {
      return (Iterator)Iterators.transform(this.original.cellSet().iterator(), TRANSPOSE_CELL);
    }
  }
  
  @Beta
  public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
    Preconditions.checkArgument(backingMap.isEmpty());
    Preconditions.checkNotNull(factory);
    return new StandardTable<R, C, V>(backingMap, factory);
  }
  
  @Beta
  public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
    return new TransformedTable<R, C, V1, V2>(fromTable, function);
  }
  
  private static class TransformedTable<R, C, V1, V2> extends AbstractTable<R, C, V2> {
    final Table<R, C, V1> fromTable;
    
    final Function<? super V1, V2> function;
    
    TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
      this.fromTable = (Table<R, C, V1>)Preconditions.checkNotNull(fromTable);
      this.function = (Function<? super V1, V2>)Preconditions.checkNotNull(function);
    }
    
    public boolean contains(Object rowKey, Object columnKey) {
      return this.fromTable.contains(rowKey, columnKey);
    }
    
    public V2 get(Object rowKey, Object columnKey) {
      return contains(rowKey, columnKey) ? (V2)this.function.apply(this.fromTable.get(rowKey, columnKey)) : null;
    }
    
    public int size() {
      return this.fromTable.size();
    }
    
    public void clear() {
      this.fromTable.clear();
    }
    
    public V2 put(R rowKey, C columnKey, V2 value) {
      throw new UnsupportedOperationException();
    }
    
    public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
      throw new UnsupportedOperationException();
    }
    
    public V2 remove(Object rowKey, Object columnKey) {
      return contains(rowKey, columnKey) ? (V2)this.function.apply(this.fromTable.remove(rowKey, columnKey)) : null;
    }
    
    public Map<C, V2> row(R rowKey) {
      return Maps.transformValues(this.fromTable.row(rowKey), this.function);
    }
    
    public Map<R, V2> column(C columnKey) {
      return Maps.transformValues(this.fromTable.column(columnKey), this.function);
    }
    
    Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
      return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>() {
          public Table.Cell<R, C, V2> apply(Table.Cell<R, C, V1> cell) {
            return Tables.immutableCell(cell.getRowKey(), cell.getColumnKey(), (V2)Tables.TransformedTable.this.function.apply(cell.getValue()));
          }
        };
    }
    
    Iterator<Table.Cell<R, C, V2>> cellIterator() {
      return Iterators.transform(this.fromTable.cellSet().iterator(), cellFunction());
    }
    
    public Set<R> rowKeySet() {
      return this.fromTable.rowKeySet();
    }
    
    public Set<C> columnKeySet() {
      return this.fromTable.columnKeySet();
    }
    
    Collection<V2> createValues() {
      return Collections2.transform(this.fromTable.values(), this.function);
    }
    
    public Map<R, Map<C, V2>> rowMap() {
      Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>() {
          public Map<C, V2> apply(Map<C, V1> row) {
            return Maps.transformValues(row, Tables.TransformedTable.this.function);
          }
        };
      return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
    }
    
    public Map<C, Map<R, V2>> columnMap() {
      Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>() {
          public Map<R, V2> apply(Map<R, V1> column) {
            return Maps.transformValues(column, Tables.TransformedTable.this.function);
          }
        };
      return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
    }
  }
  
  public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
    return new UnmodifiableTable<R, C, V>(table);
  }
  
  private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable {
    final Table<? extends R, ? extends C, ? extends V> delegate;
    
    private static final long serialVersionUID = 0L;
    
    UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> delegate) {
      this.delegate = (Table<? extends R, ? extends C, ? extends V>)Preconditions.checkNotNull(delegate);
    }
    
    protected Table<R, C, V> delegate() {
      return (Table)this.delegate;
    }
    
    public Set<Table.Cell<R, C, V>> cellSet() {
      return Collections.unmodifiableSet(super.cellSet());
    }
    
    public void clear() {
      throw new UnsupportedOperationException();
    }
    
    public Map<R, V> column(@Nullable C columnKey) {
      return Collections.unmodifiableMap(super.column(columnKey));
    }
    
    public Set<C> columnKeySet() {
      return Collections.unmodifiableSet(super.columnKeySet());
    }
    
    public Map<C, Map<R, V>> columnMap() {
      Function<Map<R, V>, Map<R, V>> wrapper = Tables.unmodifiableWrapper();
      return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), wrapper));
    }
    
    public V put(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
      throw new UnsupportedOperationException();
    }
    
    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
      throw new UnsupportedOperationException();
    }
    
    public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
      throw new UnsupportedOperationException();
    }
    
    public Map<C, V> row(@Nullable R rowKey) {
      return Collections.unmodifiableMap(super.row(rowKey));
    }
    
    public Set<R> rowKeySet() {
      return Collections.unmodifiableSet(super.rowKeySet());
    }
    
    public Map<R, Map<C, V>> rowMap() {
      Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
      return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), wrapper));
    }
    
    public Collection<V> values() {
      return Collections.unmodifiableCollection(super.values());
    }
  }
  
  @Beta
  public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> table) {
    return new UnmodifiableRowSortedMap<R, C, V>(table);
  }
  
  static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
    private static final long serialVersionUID = 0L;
    
    public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> delegate) {
      super(delegate);
    }
    
    protected RowSortedTable<R, C, V> delegate() {
      return (RowSortedTable<R, C, V>)super.delegate();
    }
    
    public SortedMap<R, Map<C, V>> rowMap() {
      Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
      return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), wrapper));
    }
    
    public SortedSet<R> rowKeySet() {
      return Collections.unmodifiableSortedSet(delegate().rowKeySet());
    }
  }
  
  private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
    return (Function)UNMODIFIABLE_WRAPPER;
  }
  
  private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>() {
      public Map<Object, Object> apply(Map<Object, Object> input) {
        return Collections.unmodifiableMap(input);
      }
    };
  
  static boolean equalsImpl(Table<?, ?, ?> table, @Nullable Object obj) {
    if (obj == table)
      return true; 
    if (obj instanceof Table) {
      Table<?, ?, ?> that = (Table<?, ?, ?>)obj;
      return table.cellSet().equals(that.cellSet());
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Tables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */