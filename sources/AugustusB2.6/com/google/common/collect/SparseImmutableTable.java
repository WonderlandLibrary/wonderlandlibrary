// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.Immutable;

@Immutable(containerOf = { "R", "C", "V" })
@ElementTypesAreNonnullByDefault
@GwtCompatible
final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V>
{
    static final ImmutableTable<Object, Object, Object> EMPTY;
    private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
    private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
    private final int[] cellRowIndices;
    private final int[] cellColumnInRowIndices;
    
    SparseImmutableTable(final ImmutableList<Table.Cell<R, C, V>> cellList, final ImmutableSet<R> rowSpace, final ImmutableSet<C> columnSpace) {
        final Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
        final Map<R, Map<C, V>> rows = (Map<R, Map<C, V>>)Maps.newLinkedHashMap();
        for (final R row : rowSpace) {
            rows.put(row, new LinkedHashMap<C, V>());
        }
        final Map<C, Map<R, V>> columns = (Map<C, Map<R, V>>)Maps.newLinkedHashMap();
        for (final C col : columnSpace) {
            columns.put(col, new LinkedHashMap<R, V>());
        }
        final int[] cellRowIndices = new int[cellList.size()];
        final int[] cellColumnInRowIndices = new int[cellList.size()];
        for (int i = 0; i < cellList.size(); ++i) {
            final Table.Cell<R, C, V> cell = cellList.get(i);
            final R rowKey = cell.getRowKey();
            final C columnKey = cell.getColumnKey();
            final V value = cell.getValue();
            cellRowIndices[i] = Objects.requireNonNull(rowIndex.get(rowKey));
            final Map<C, V> thisRow = Objects.requireNonNull(rows.get(rowKey));
            cellColumnInRowIndices[i] = thisRow.size();
            final V oldValue = thisRow.put(columnKey, value);
            this.checkNoDuplicate(rowKey, columnKey, oldValue, value);
            Objects.requireNonNull(columns.get(columnKey)).put(rowKey, value);
        }
        this.cellRowIndices = cellRowIndices;
        this.cellColumnInRowIndices = cellColumnInRowIndices;
        final ImmutableMap.Builder<R, ImmutableMap<C, V>> rowBuilder = new ImmutableMap.Builder<R, ImmutableMap<C, V>>(rows.size());
        for (final Map.Entry<R, Map<C, V>> row2 : rows.entrySet()) {
            rowBuilder.put(row2.getKey(), ImmutableMap.copyOf((Map<? extends C, ? extends V>)row2.getValue()));
        }
        this.rowMap = rowBuilder.build();
        final ImmutableMap.Builder<C, ImmutableMap<R, V>> columnBuilder = new ImmutableMap.Builder<C, ImmutableMap<R, V>>(columns.size());
        for (final Map.Entry<C, Map<R, V>> col2 : columns.entrySet()) {
            columnBuilder.put(col2.getKey(), ImmutableMap.copyOf((Map<? extends R, ? extends V>)col2.getValue()));
        }
        this.columnMap = columnBuilder.build();
    }
    
    @Override
    public ImmutableMap<C, Map<R, V>> columnMap() {
        final ImmutableMap<C, ImmutableMap<R, V>> columnMap = this.columnMap;
        return ImmutableMap.copyOf((Map<? extends C, ? extends Map<R, V>>)columnMap);
    }
    
    @Override
    public ImmutableMap<R, Map<C, V>> rowMap() {
        final ImmutableMap<R, ImmutableMap<C, V>> rowMap = this.rowMap;
        return ImmutableMap.copyOf((Map<? extends R, ? extends Map<C, V>>)rowMap);
    }
    
    @Override
    public int size() {
        return this.cellRowIndices.length;
    }
    
    @Override
    Table.Cell<R, C, V> getCell(final int index) {
        final int rowIndex = this.cellRowIndices[index];
        final Map.Entry<R, ImmutableMap<C, V>> rowEntry = this.rowMap.entrySet().asList().get(rowIndex);
        final ImmutableMap<C, V> row = rowEntry.getValue();
        final int columnIndex = this.cellColumnInRowIndices[index];
        final Map.Entry<C, V> colEntry = row.entrySet().asList().get(columnIndex);
        return ImmutableTable.cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
    }
    
    @Override
    V getValue(final int index) {
        final int rowIndex = this.cellRowIndices[index];
        final ImmutableMap<C, V> row = this.rowMap.values().asList().get(rowIndex);
        final int columnIndex = this.cellColumnInRowIndices[index];
        return row.values().asList().get(columnIndex);
    }
    
    @Override
    SerializedForm createSerializedForm() {
        final Map<C, Integer> columnKeyToIndex = Maps.indexMap(this.columnKeySet());
        final int[] cellColumnIndices = new int[this.cellSet().size()];
        int i = 0;
        for (final Table.Cell<R, C, V> cell : this.cellSet()) {
            cellColumnIndices[i++] = Objects.requireNonNull(columnKeyToIndex.get(cell.getColumnKey()));
        }
        return SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
    }
    
    static {
        EMPTY = new SparseImmutableTable<Object, Object, Object>(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
    }
}
