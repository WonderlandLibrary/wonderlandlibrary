/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.concurrent.Immutable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.RegularImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class DenseImmutableTable<R, C, V>
extends RegularImmutableTable<R, C, V> {
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] rowCounts;
    private final int[] columnCounts;
    private final V[][] values;
    private final int[] iterationOrderRow;
    private final int[] iterationOrderColumn;

    private static <E> ImmutableMap<E, Integer> makeIndex(ImmutableSet<E> set) {
        ImmutableMap.Builder indexBuilder = ImmutableMap.builder();
        int i2 = 0;
        for (Object key : set) {
            indexBuilder.put(key, i2);
            ++i2;
        }
        return indexBuilder.build();
    }

    DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        Object[][] array = new Object[rowSpace.size()][columnSpace.size()];
        this.values = array;
        this.rowKeyToIndex = DenseImmutableTable.makeIndex(rowSpace);
        this.columnKeyToIndex = DenseImmutableTable.makeIndex(columnSpace);
        this.rowCounts = new int[this.rowKeyToIndex.size()];
        this.columnCounts = new int[this.columnKeyToIndex.size()];
        int[] iterationOrderRow = new int[cellList.size()];
        int[] iterationOrderColumn = new int[cellList.size()];
        for (int i2 = 0; i2 < cellList.size(); ++i2) {
            int columnIndex;
            Table.Cell cell = (Table.Cell)cellList.get(i2);
            Object rowKey = cell.getRowKey();
            Object columnKey = cell.getColumnKey();
            int rowIndex = this.rowKeyToIndex.get(rowKey);
            V existingValue = this.values[rowIndex][columnIndex = this.columnKeyToIndex.get(columnKey).intValue()];
            Preconditions.checkArgument(existingValue == null, "duplicate key: (%s, %s)", rowKey, columnKey);
            this.values[rowIndex][columnIndex] = cell.getValue();
            int n2 = rowIndex;
            this.rowCounts[n2] = this.rowCounts[n2] + 1;
            int n3 = columnIndex;
            this.columnCounts[n3] = this.columnCounts[n3] + 1;
            iterationOrderRow[i2] = rowIndex;
            iterationOrderColumn[i2] = columnIndex;
        }
        this.iterationOrderRow = iterationOrderRow;
        this.iterationOrderColumn = iterationOrderColumn;
        this.rowMap = new RowMap();
        this.columnMap = new ColumnMap();
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
    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
        Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        return rowIndex == null || columnIndex == null ? null : (V)this.values[rowIndex][columnIndex];
    }

    @Override
    public int size() {
        return this.iterationOrderRow.length;
    }

    @Override
    Table.Cell<R, C, V> getCell(int index) {
        int rowIndex = this.iterationOrderRow[index];
        int columnIndex = this.iterationOrderColumn[index];
        Object rowKey = ((ImmutableCollection)((Object)this.rowKeySet())).asList().get(rowIndex);
        Object columnKey = ((ImmutableCollection)((Object)this.columnKeySet())).asList().get(columnIndex);
        V value = this.values[rowIndex][columnIndex];
        return DenseImmutableTable.cellOf(rowKey, columnKey, value);
    }

    @Override
    V getValue(int index) {
        return this.values[this.iterationOrderRow[index]][this.iterationOrderColumn[index]];
    }

    private final class ColumnMap
    extends ImmutableArrayMap<C, Map<R, V>> {
        private ColumnMap() {
            super(DenseImmutableTable.this.columnCounts.length);
        }

        @Override
        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        @Override
        Map<R, V> getValue(int keyIndex) {
            return new Column(keyIndex);
        }

        @Override
        boolean isPartialView() {
            return false;
        }
    }

    private final class RowMap
    extends ImmutableArrayMap<R, Map<C, V>> {
        private RowMap() {
            super(DenseImmutableTable.this.rowCounts.length);
        }

        @Override
        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        @Override
        Map<C, V> getValue(int keyIndex) {
            return new Row(keyIndex);
        }

        @Override
        boolean isPartialView() {
            return false;
        }
    }

    private final class Column
    extends ImmutableArrayMap<R, V> {
        private final int columnIndex;

        Column(int columnIndex) {
            super(DenseImmutableTable.this.columnCounts[columnIndex]);
            this.columnIndex = columnIndex;
        }

        @Override
        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        @Override
        V getValue(int keyIndex) {
            return DenseImmutableTable.this.values[keyIndex][this.columnIndex];
        }

        @Override
        boolean isPartialView() {
            return true;
        }
    }

    private final class Row
    extends ImmutableArrayMap<C, V> {
        private final int rowIndex;

        Row(int rowIndex) {
            super(DenseImmutableTable.this.rowCounts[rowIndex]);
            this.rowIndex = rowIndex;
        }

        @Override
        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        @Override
        V getValue(int keyIndex) {
            return DenseImmutableTable.this.values[this.rowIndex][keyIndex];
        }

        @Override
        boolean isPartialView() {
            return true;
        }
    }

    private static abstract class ImmutableArrayMap<K, V>
    extends ImmutableMap<K, V> {
        private final int size;

        ImmutableArrayMap(int size) {
            this.size = size;
        }

        abstract ImmutableMap<K, Integer> keyToIndex();

        private boolean isFull() {
            return this.size == this.keyToIndex().size();
        }

        K getKey(int index) {
            return (K)((ImmutableCollection)((Object)this.keyToIndex().keySet())).asList().get(index);
        }

        @Nullable
        abstract V getValue(int var1);

        @Override
        ImmutableSet<K> createKeySet() {
            return this.isFull() ? this.keyToIndex().keySet() : super.createKeySet();
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public V get(@Nullable Object key) {
            Integer keyIndex = this.keyToIndex().get(key);
            return keyIndex == null ? null : (V)this.getValue(keyIndex);
        }

        @Override
        ImmutableSet<Map.Entry<K, V>> createEntrySet() {
            return new ImmutableMapEntrySet<K, V>(){

                @Override
                ImmutableMap<K, V> map() {
                    return ImmutableArrayMap.this;
                }

                @Override
                public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                    return new AbstractIterator<Map.Entry<K, V>>(){
                        private int index = -1;
                        private final int maxIndex;
                        {
                            this.maxIndex = ImmutableArrayMap.this.keyToIndex().size();
                        }

                        @Override
                        protected Map.Entry<K, V> computeNext() {
                            ++this.index;
                            while (this.index < this.maxIndex) {
                                Object value = ImmutableArrayMap.this.getValue(this.index);
                                if (value != null) {
                                    return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), value);
                                }
                                ++this.index;
                            }
                            return (Map.Entry)this.endOfData();
                        }
                    };
                }
            };
        }
    }
}

