package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
  final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
    return isEmpty() ? ImmutableSet.<Table.Cell<R, C, V>>of() : new CellSet();
  }
  
  private final class CellSet extends ImmutableSet<Table.Cell<R, C, V>> {
    private CellSet() {}
    
    public int size() {
      return RegularImmutableTable.this.size();
    }
    
    public UnmodifiableIterator<Table.Cell<R, C, V>> iterator() {
      return asList().iterator();
    }
    
    ImmutableList<Table.Cell<R, C, V>> createAsList() {
      return new ImmutableAsList<Table.Cell<R, C, V>>() {
          public Table.Cell<R, C, V> get(int index) {
            return RegularImmutableTable.this.getCell(index);
          }
          
          ImmutableCollection<Table.Cell<R, C, V>> delegateCollection() {
            return RegularImmutableTable.CellSet.this;
          }
        };
    }
    
    public boolean contains(@Nullable Object object) {
      if (object instanceof Table.Cell) {
        Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)object;
        Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
        return (value != null && value.equals(cell.getValue()));
      } 
      return false;
    }
    
    boolean isPartialView() {
      return false;
    }
  }
  
  final ImmutableCollection<V> createValues() {
    return isEmpty() ? ImmutableList.<V>of() : new Values();
  }
  
  private final class Values extends ImmutableList<V> {
    private Values() {}
    
    public int size() {
      return RegularImmutableTable.this.size();
    }
    
    public V get(int index) {
      return (V)RegularImmutableTable.this.getValue(index);
    }
    
    boolean isPartialView() {
      return true;
    }
  }
  
  static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator) {
    Preconditions.checkNotNull(cells);
    if (rowComparator != null || columnComparator != null) {
      Comparator<Table.Cell<R, C, V>> comparator = new Comparator<Table.Cell<R, C, V>>() {
          public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2) {
            int rowCompare = (rowComparator == null) ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
            if (rowCompare != 0)
              return rowCompare; 
            return (columnComparator == null) ? 0 : columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
          }
        };
      Collections.sort(cells, comparator);
    } 
    return forCellsInternal(cells, rowComparator, columnComparator);
  }
  
  static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
    return forCellsInternal(cells, (Comparator<? super R>)null, (Comparator<? super C>)null);
  }
  
  private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator) {
    ImmutableSet.Builder<R> rowSpaceBuilder = ImmutableSet.builder();
    ImmutableSet.Builder<C> columnSpaceBuilder = ImmutableSet.builder();
    ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
    for (Table.Cell<R, C, V> cell : cellList) {
      rowSpaceBuilder.add(cell.getRowKey());
      columnSpaceBuilder.add(cell.getColumnKey());
    } 
    ImmutableSet<R> rowSpace = rowSpaceBuilder.build();
    if (rowComparator != null) {
      List<R> rowList = Lists.newArrayList(rowSpace);
      Collections.sort(rowList, rowComparator);
      rowSpace = ImmutableSet.copyOf(rowList);
    } 
    ImmutableSet<C> columnSpace = columnSpaceBuilder.build();
    if (columnComparator != null) {
      List<C> columnList = Lists.newArrayList(columnSpace);
      Collections.sort(columnList, columnComparator);
      columnSpace = ImmutableSet.copyOf(columnList);
    } 
    return (cellList.size() > rowSpace.size() * columnSpace.size() / 2L) ? new DenseImmutableTable<R, C, V>(cellList, rowSpace, columnSpace) : new SparseImmutableTable<R, C, V>(cellList, rowSpace, columnSpace);
  }
  
  abstract Table.Cell<R, C, V> getCell(int paramInt);
  
  abstract V getValue(int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\RegularImmutableTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */