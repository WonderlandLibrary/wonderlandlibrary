/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.Immutable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.RegularImmutableTable;
import com.google.common.collect.Table;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class SparseImmutableTable<R, C, V>
extends RegularImmutableTable<R, C, V> {
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] iterationOrderRow;
    private final int[] iterationOrderColumn;

    SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        HashMap rowIndex = Maps.newHashMap();
        LinkedHashMap rows = Maps.newLinkedHashMap();
        for (Object row : rowSpace) {
            rowIndex.put(row, rows.size());
            rows.put(row, new LinkedHashMap());
        }
        LinkedHashMap columns = Maps.newLinkedHashMap();
        for (Object col : columnSpace) {
            columns.put(col, new LinkedHashMap());
        }
        int[] iterationOrderRow = new int[cellList.size()];
        int[] iterationOrderColumn = new int[cellList.size()];
        for (int i2 = 0; i2 < cellList.size(); ++i2) {
            Table.Cell cell = (Table.Cell)cellList.get(i2);
            Object rowKey = cell.getRowKey();
            Object columnKey = cell.getColumnKey();
            Object value = cell.getValue();
            iterationOrderRow[i2] = (Integer)rowIndex.get(rowKey);
            Map thisRow = (Map)rows.get(rowKey);
            iterationOrderColumn[i2] = thisRow.size();
            Object oldValue = thisRow.put(columnKey, value);
            if (oldValue != null) {
                throw new IllegalArgumentException("Duplicate value for row=" + rowKey + ", column=" + columnKey + ": " + value + ", " + oldValue);
            }
            ((Map)columns.get(columnKey)).put(rowKey, value);
        }
        this.iterationOrderRow = iterationOrderRow;
        this.iterationOrderColumn = iterationOrderColumn;
        ImmutableMap.Builder rowBuilder = ImmutableMap.builder();
        for (Map.Entry row : rows.entrySet()) {
            rowBuilder.put(row.getKey(), ImmutableMap.copyOf((Map)row.getValue()));
        }
        this.rowMap = rowBuilder.build();
        ImmutableMap.Builder columnBuilder = ImmutableMap.builder();
        for (Map.Entry col : columns.entrySet()) {
            columnBuilder.put(col.getKey(), ImmutableMap.copyOf((Map)col.getValue()));
        }
        this.columnMap = columnBuilder.build();
    }

    @Override
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    @Override
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    @Override
    public int size() {
        return this.iterationOrderRow.length;
    }

    @Override
    Table.Cell<R, C, V> getCell(int index) {
        int rowIndex = this.iterationOrderRow[index];
        Map.Entry rowEntry = (Map.Entry)((ImmutableCollection)((Object)this.rowMap.entrySet())).asList().get(rowIndex);
        ImmutableMap row = (ImmutableMap)rowEntry.getValue();
        int columnIndex = this.iterationOrderColumn[index];
        Map.Entry colEntry = (Map.Entry)((ImmutableCollection)((Object)row.entrySet())).asList().get(columnIndex);
        return SparseImmutableTable.cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
    }

    @Override
    V getValue(int index) {
        int rowIndex = this.iterationOrderRow[index];
        ImmutableMap row = (ImmutableMap)((ImmutableCollection)this.rowMap.values()).asList().get(rowIndex);
        int columnIndex = this.iterationOrderColumn[index];
        return (V)((ImmutableCollection)row.values()).asList().get(columnIndex);
    }
}

