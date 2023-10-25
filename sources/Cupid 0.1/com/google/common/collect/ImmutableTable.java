package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ImmutableTable<R, C, V> extends AbstractTable<R, C, V> {
  private static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable<Object, Object, Object>(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
  
  public static <R, C, V> ImmutableTable<R, C, V> of() {
    return (ImmutableTable)EMPTY;
  }
  
  public static <R, C, V> ImmutableTable<R, C, V> of(R rowKey, C columnKey, V value) {
    return new SingletonImmutableTable<R, C, V>(rowKey, columnKey, value);
  }
  
  public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
    Table.Cell<? extends R, ? extends C, ? extends V> onlyCell;
    if (table instanceof ImmutableTable) {
      ImmutableTable<R, C, V> parameterizedTable = (ImmutableTable)table;
      return parameterizedTable;
    } 
    int size = table.size();
    switch (size) {
      case 0:
        return of();
      case 1:
        onlyCell = Iterables.<Table.Cell<? extends R, ? extends C, ? extends V>>getOnlyElement(table.cellSet());
        return of(onlyCell.getRowKey(), onlyCell.getColumnKey(), onlyCell.getValue());
    } 
    ImmutableSet.Builder<Table.Cell<R, C, V>> cellSetBuilder = ImmutableSet.builder();
    for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet())
      cellSetBuilder.add(cellOf(cell.getRowKey(), cell.getColumnKey(), cell.getValue())); 
    return RegularImmutableTable.forCells(cellSetBuilder.build());
  }
  
  public static <R, C, V> Builder<R, C, V> builder() {
    return new Builder<R, C, V>();
  }
  
  static <R, C, V> Table.Cell<R, C, V> cellOf(R rowKey, C columnKey, V value) {
    return Tables.immutableCell((R)Preconditions.checkNotNull(rowKey), (C)Preconditions.checkNotNull(columnKey), (V)Preconditions.checkNotNull(value));
  }
  
  public static final class Builder<R, C, V> {
    private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
    
    private Comparator<? super R> rowComparator;
    
    private Comparator<? super C> columnComparator;
    
    public Builder<R, C, V> orderRowsBy(Comparator<? super R> rowComparator) {
      this.rowComparator = (Comparator<? super R>)Preconditions.checkNotNull(rowComparator);
      return this;
    }
    
    public Builder<R, C, V> orderColumnsBy(Comparator<? super C> columnComparator) {
      this.columnComparator = (Comparator<? super C>)Preconditions.checkNotNull(columnComparator);
      return this;
    }
    
    public Builder<R, C, V> put(R rowKey, C columnKey, V value) {
      this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
      return this;
    }
    
    public Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> cell) {
      if (cell instanceof Tables.ImmutableCell) {
        Preconditions.checkNotNull(cell.getRowKey());
        Preconditions.checkNotNull(cell.getColumnKey());
        Preconditions.checkNotNull(cell.getValue());
        Table.Cell<? extends R, ? extends C, ? extends V> cell1 = cell;
        this.cells.add(cell1);
      } else {
        put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
      } 
      return this;
    }
    
    public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
      for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet())
        put(cell); 
      return this;
    }
    
    public ImmutableTable<R, C, V> build() {
      int size = this.cells.size();
      switch (size) {
        case 0:
          return ImmutableTable.of();
        case 1:
          return new SingletonImmutableTable<R, C, V>(Iterables.<Table.Cell<R, C, V>>getOnlyElement(this.cells));
      } 
      return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
    }
  }
  
  public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
    return (ImmutableSet<Table.Cell<R, C, V>>)super.cellSet();
  }
  
  final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
    throw new AssertionError("should never be called");
  }
  
  public ImmutableCollection<V> values() {
    return (ImmutableCollection<V>)super.values();
  }
  
  final Iterator<V> valuesIterator() {
    throw new AssertionError("should never be called");
  }
  
  public ImmutableMap<R, V> column(C columnKey) {
    Preconditions.checkNotNull(columnKey);
    return (ImmutableMap<R, V>)Objects.firstNonNull(columnMap().get(columnKey), ImmutableMap.of());
  }
  
  public ImmutableSet<C> columnKeySet() {
    return columnMap().keySet();
  }
  
  public ImmutableMap<C, V> row(R rowKey) {
    Preconditions.checkNotNull(rowKey);
    return (ImmutableMap<C, V>)Objects.firstNonNull(rowMap().get(rowKey), ImmutableMap.of());
  }
  
  public ImmutableSet<R> rowKeySet() {
    return rowMap().keySet();
  }
  
  public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
    return (get(rowKey, columnKey) != null);
  }
  
  public boolean containsValue(@Nullable Object value) {
    return values().contains(value);
  }
  
  @Deprecated
  public final void clear() {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final V put(R rowKey, C columnKey, V value) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final V remove(Object rowKey, Object columnKey) {
    throw new UnsupportedOperationException();
  }
  
  abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();
  
  abstract ImmutableCollection<V> createValues();
  
  public abstract ImmutableMap<C, Map<R, V>> columnMap();
  
  public abstract ImmutableMap<R, Map<C, V>> rowMap();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */