/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.DenseImmutableTable;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.collect.SparseImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
abstract class RegularImmutableTable<R, C, V>
extends ImmutableTable<R, C, V> {
    RegularImmutableTable() {
    }

    abstract Table.Cell<R, C, V> getCell(int var1);

    @Override
    final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return this.isEmpty() ? ImmutableSet.of() : new CellSet();
    }

    abstract V getValue(int var1);

    @Override
    final ImmutableCollection<V> createValues() {
        return this.isEmpty() ? ImmutableList.of() : new Values();
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, final @Nullable Comparator<? super R> rowComparator, final @Nullable Comparator<? super C> columnComparator) {
        Preconditions.checkNotNull(cells);
        if (rowComparator != null || columnComparator != null) {
            Comparator comparator = new Comparator<Table.Cell<R, C, V>>(){

                @Override
                public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2) {
                    int rowCompare;
                    int n2 = rowCompare = rowComparator == null ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
                    if (rowCompare != 0) {
                        return rowCompare;
                    }
                    return columnComparator == null ? 0 : columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
                }
            };
            Collections.sort(cells, comparator);
        }
        return RegularImmutableTable.forCellsInternal(cells, rowComparator, columnComparator);
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
        return RegularImmutableTable.forCellsInternal(cells, null, null);
    }

    /*
     * WARNING - void declaration
     */
    private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator) {
        void var7_12;
        ImmutableSet.Builder rowSpaceBuilder = ImmutableSet.builder();
        ImmutableSet.Builder columnSpaceBuilder = ImmutableSet.builder();
        ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
        for (Table.Cell cell : cellList) {
            rowSpaceBuilder.add(cell.getRowKey());
            columnSpaceBuilder.add(cell.getColumnKey());
        }
        ImmutableSet rowSpace = rowSpaceBuilder.build();
        if (rowComparator != null) {
            ArrayList arrayList = Lists.newArrayList(rowSpace);
            Collections.sort(arrayList, rowComparator);
            rowSpace = ImmutableSet.copyOf(arrayList);
        }
        ImmutableCollection immutableCollection = columnSpaceBuilder.build();
        if (columnComparator != null) {
            ArrayList columnList = Lists.newArrayList(immutableCollection);
            Collections.sort(columnList, columnComparator);
            ImmutableSet immutableSet = ImmutableSet.copyOf(columnList);
        }
        return (long)cellList.size() > (long)rowSpace.size() * (long)var7_12.size() / 2L ? new DenseImmutableTable<R, C, V>(cellList, rowSpace, var7_12) : new SparseImmutableTable<R, C, V>(cellList, rowSpace, var7_12);
    }

    private final class Values
    extends ImmutableList<V> {
        private Values() {
        }

        @Override
        public int size() {
            return RegularImmutableTable.this.size();
        }

        @Override
        public V get(int index) {
            return RegularImmutableTable.this.getValue(index);
        }

        @Override
        boolean isPartialView() {
            return true;
        }
    }

    private final class CellSet
    extends ImmutableSet<Table.Cell<R, C, V>> {
        private CellSet() {
        }

        @Override
        public int size() {
            return RegularImmutableTable.this.size();
        }

        @Override
        public UnmodifiableIterator<Table.Cell<R, C, V>> iterator() {
            return this.asList().iterator();
        }

        @Override
        ImmutableList<Table.Cell<R, C, V>> createAsList() {
            return new ImmutableAsList<Table.Cell<R, C, V>>(){

                @Override
                public Table.Cell<R, C, V> get(int index) {
                    return RegularImmutableTable.this.getCell(index);
                }

                @Override
                ImmutableCollection<Table.Cell<R, C, V>> delegateCollection() {
                    return CellSet.this;
                }
            };
        }

        @Override
        public boolean contains(@Nullable Object object) {
            if (object instanceof Table.Cell) {
                Table.Cell cell = (Table.Cell)object;
                Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
                return value != null && value.equals(cell.getValue());
            }
            return false;
        }

        @Override
        boolean isPartialView() {
            return false;
        }
    }
}

