// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Collections;
import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import java.util.List;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V>
{
    abstract Table.Cell<R, C, V> getCell(final int p0);
    
    @Override
    final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return this.isEmpty() ? ImmutableSet.of() : new CellSet();
    }
    
    abstract V getValue(final int p0);
    
    @Override
    final ImmutableCollection<V> createValues() {
        return (ImmutableCollection<V>)(this.isEmpty() ? ImmutableList.of() : new Values());
    }
    
    static <R, C, V> RegularImmutableTable<R, C, V> forCells(final List<Table.Cell<R, C, V>> cells, @CheckForNull final Comparator<? super R> rowComparator, @CheckForNull final Comparator<? super C> columnComparator) {
        Preconditions.checkNotNull(cells);
        if (rowComparator != null || columnComparator != null) {
            final Comparator<Table.Cell<R, C, V>> comparator = new Comparator<Table.Cell<R, C, V>>() {
                @Override
                public int compare(final Table.Cell<R, C, V> cell1, final Table.Cell<R, C, V> cell2) {
                    final int rowCompare = (rowComparator == null) ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
                    if (rowCompare != 0) {
                        return rowCompare;
                    }
                    return (columnComparator == null) ? 0 : columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
                }
            };
            Collections.sort(cells, comparator);
        }
        return forCellsInternal(cells, rowComparator, columnComparator);
    }
    
    static <R, C, V> RegularImmutableTable<R, C, V> forCells(final Iterable<Table.Cell<R, C, V>> cells) {
        return forCellsInternal(cells, null, null);
    }
    
    private static <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(final Iterable<Table.Cell<R, C, V>> cells, @CheckForNull final Comparator<? super R> rowComparator, @CheckForNull final Comparator<? super C> columnComparator) {
        final Set<R> rowSpaceBuilder = new LinkedHashSet<R>();
        final Set<C> columnSpaceBuilder = new LinkedHashSet<C>();
        final ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf((Iterable<? extends Table.Cell<R, C, V>>)cells);
        for (final Table.Cell<R, C, V> cell : cells) {
            rowSpaceBuilder.add(cell.getRowKey());
            columnSpaceBuilder.add(cell.getColumnKey());
        }
        final ImmutableSet<R> rowSpace = (rowComparator == null) ? ImmutableSet.copyOf((Collection<? extends R>)rowSpaceBuilder) : ImmutableSet.copyOf((Collection<? extends R>)ImmutableList.sortedCopyOf((Comparator<? super Object>)rowComparator, (Iterable<?>)rowSpaceBuilder));
        final ImmutableSet<C> columnSpace = (columnComparator == null) ? ImmutableSet.copyOf((Collection<? extends C>)columnSpaceBuilder) : ImmutableSet.copyOf((Collection<? extends C>)ImmutableList.sortedCopyOf((Comparator<? super Object>)columnComparator, (Iterable<?>)columnSpaceBuilder));
        return forOrderedComponents(cellList, rowSpace, columnSpace);
    }
    
    static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(final ImmutableList<Table.Cell<R, C, V>> cellList, final ImmutableSet<R> rowSpace, final ImmutableSet<C> columnSpace) {
        return (RegularImmutableTable<R, C, V>)((cellList.size() > rowSpace.size() * (long)columnSpace.size() / 2L) ? new DenseImmutableTable<Object, Object, Object>((ImmutableList<Table.Cell<?, ?, ?>>)cellList, rowSpace, columnSpace) : new SparseImmutableTable<Object, Object, Object>((ImmutableList<Table.Cell<?, ?, ?>>)cellList, rowSpace, columnSpace));
    }
    
    final void checkNoDuplicate(final R rowKey, final C columnKey, @CheckForNull final V existingValue, final V newValue) {
        Preconditions.checkArgument(existingValue == null, "Duplicate key: (row=%s, column=%s), values: [%s, %s].", rowKey, columnKey, newValue, existingValue);
    }
    
    private final class CellSet extends IndexedImmutableSet<Table.Cell<R, C, V>>
    {
        @Override
        public int size() {
            return RegularImmutableTable.this.size();
        }
        
        @Override
        Table.Cell<R, C, V> get(final int index) {
            return RegularImmutableTable.this.getCell(index);
        }
        
        @Override
        public boolean contains(@CheckForNull final Object object) {
            if (object instanceof Table.Cell) {
                final Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)object;
                final Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
                return value != null && value.equals(cell.getValue());
            }
            return false;
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
    }
    
    private final class Values extends ImmutableList<V>
    {
        @Override
        public int size() {
            return RegularImmutableTable.this.size();
        }
        
        @Override
        public V get(final int index) {
            return RegularImmutableTable.this.getValue(index);
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
}
