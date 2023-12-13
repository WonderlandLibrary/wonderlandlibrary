// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.Map;
import me.gong.mcleaks.util.google.common.base.MoreObjects;
import java.util.Spliterator;
import java.util.Iterator;
import java.util.function.BinaryOperator;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.stream.Collector;
import java.util.function.Function;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible
public abstract class ImmutableTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable
{
    @Beta
    public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(final Function<? super T, ? extends R> rowFunction, final Function<? super T, ? extends C> columnFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(rowFunction);
        Preconditions.checkNotNull(columnFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(() -> new Builder(), (builder, t) -> builder.put(rowFunction.apply((Object)t), columnFunction.apply((Object)t), valueFunction.apply((Object)t)), (b1, b2) -> b1.combine(b2), b -> b.build(), new Collector.Characteristics[0]);
    }
    
    public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(final Function<? super T, ? extends R> rowFunction, final Function<? super T, ? extends C> columnFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        Preconditions.checkNotNull(rowFunction);
        Preconditions.checkNotNull(columnFunction);
        Preconditions.checkNotNull(valueFunction);
        Preconditions.checkNotNull(mergeFunction);
        return Collector.of(() -> new CollectorState(), (state, input) -> state.put(rowFunction.apply((Object)input), columnFunction.apply((Object)input), (V)valueFunction.apply((Object)input), mergeFunction), (s1, s2) -> s1.combine(s2, mergeFunction), state -> state.toTable(), new Collector.Characteristics[0]);
    }
    
    public static <R, C, V> ImmutableTable<R, C, V> of() {
        return (ImmutableTable<R, C, V>)SparseImmutableTable.EMPTY;
    }
    
    public static <R, C, V> ImmutableTable<R, C, V> of(final R rowKey, final C columnKey, final V value) {
        return new SingletonImmutableTable<R, C, V>(rowKey, columnKey, value);
    }
    
    public static <R, C, V> ImmutableTable<R, C, V> copyOf(final Table<? extends R, ? extends C, ? extends V> table) {
        if (table instanceof ImmutableTable) {
            final ImmutableTable<R, C, V> parameterizedTable = (ImmutableTable<R, C, V>)(ImmutableTable)table;
            return parameterizedTable;
        }
        return copyOf((Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>>)table.cellSet());
    }
    
    private static <R, C, V> ImmutableTable<R, C, V> copyOf(final Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>> cells) {
        final Builder<R, C, V> builder = builder();
        for (final Table.Cell<? extends R, ? extends C, ? extends V> cell : cells) {
            builder.put(cell);
        }
        return builder.build();
    }
    
    public static <R, C, V> Builder<R, C, V> builder() {
        return new Builder<R, C, V>();
    }
    
    static <R, C, V> Table.Cell<R, C, V> cellOf(final R rowKey, final C columnKey, final V value) {
        return Tables.immutableCell((R)Preconditions.checkNotNull((R)rowKey), (C)Preconditions.checkNotNull((C)columnKey), (V)Preconditions.checkNotNull((V)value));
    }
    
    ImmutableTable() {
    }
    
    @Override
    public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
        return (ImmutableSet<Table.Cell<R, C, V>>)(ImmutableSet)super.cellSet();
    }
    
    @Override
    abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();
    
    @Override
    final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    final Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableCollection<V> values() {
        return (ImmutableCollection<V>)(ImmutableCollection)super.values();
    }
    
    @Override
    abstract ImmutableCollection<V> createValues();
    
    @Override
    final Iterator<V> valuesIterator() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableMap<R, V> column(final C columnKey) {
        Preconditions.checkNotNull(columnKey);
        return MoreObjects.firstNonNull(this.columnMap().get(columnKey), ImmutableMap.of());
    }
    
    @Override
    public ImmutableSet<C> columnKeySet() {
        return this.columnMap().keySet();
    }
    
    @Override
    public abstract ImmutableMap<C, Map<R, V>> columnMap();
    
    @Override
    public ImmutableMap<C, V> row(final R rowKey) {
        Preconditions.checkNotNull(rowKey);
        return MoreObjects.firstNonNull(this.rowMap().get(rowKey), ImmutableMap.of());
    }
    
    @Override
    public ImmutableSet<R> rowKeySet() {
        return this.rowMap().keySet();
    }
    
    @Override
    public abstract ImmutableMap<R, Map<C, V>> rowMap();
    
    @Override
    public boolean contains(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        return this.get(rowKey, columnKey) != null;
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return this.values().contains(value);
    }
    
    @Deprecated
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public final V put(final R rowKey, final C columnKey, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public final void putAll(final Table<? extends R, ? extends C, ? extends V> table) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public final V remove(final Object rowKey, final Object columnKey) {
        throw new UnsupportedOperationException();
    }
    
    abstract SerializedForm createSerializedForm();
    
    final Object writeReplace() {
        return this.createSerializedForm();
    }
    
    private static final class CollectorState<R, C, V>
    {
        final List<MutableCell<R, C, V>> insertionOrder;
        final Table<R, C, MutableCell<R, C, V>> table;
        
        private CollectorState() {
            this.insertionOrder = new ArrayList<MutableCell<R, C, V>>();
            this.table = (Table<R, C, MutableCell<R, C, V>>)HashBasedTable.create();
        }
        
        void put(final R row, final C column, final V value, final BinaryOperator<V> merger) {
            final MutableCell<R, C, V> oldCell = this.table.get(row, column);
            if (oldCell == null) {
                final MutableCell<R, C, V> cell = new MutableCell<R, C, V>(row, column, value);
                this.insertionOrder.add(cell);
                this.table.put(row, column, cell);
            }
            else {
                oldCell.merge(value, merger);
            }
        }
        
        CollectorState<R, C, V> combine(final CollectorState<R, C, V> other, final BinaryOperator<V> merger) {
            for (final MutableCell<R, C, V> cell : other.insertionOrder) {
                this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue(), merger);
            }
            return this;
        }
        
        ImmutableTable<R, C, V> toTable() {
            return (ImmutableTable<R, C, V>)copyOf((Iterable<? extends Table.Cell<?, ?, ?>>)this.insertionOrder);
        }
    }
    
    private static final class MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V>
    {
        private final R row;
        private final C column;
        private V value;
        
        MutableCell(final R row, final C column, final V value) {
            this.row = Preconditions.checkNotNull(row);
            this.column = Preconditions.checkNotNull(column);
            this.value = Preconditions.checkNotNull(value);
        }
        
        @Override
        public R getRowKey() {
            return this.row;
        }
        
        @Override
        public C getColumnKey() {
            return this.column;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        void merge(final V value, final BinaryOperator<V> mergeFunction) {
            Preconditions.checkNotNull(value);
            this.value = Preconditions.checkNotNull(mergeFunction.apply(this.value, value));
        }
    }
    
    public static final class Builder<R, C, V>
    {
        private final List<Table.Cell<R, C, V>> cells;
        private Comparator<? super R> rowComparator;
        private Comparator<? super C> columnComparator;
        
        public Builder() {
            this.cells = (List<Table.Cell<R, C, V>>)Lists.newArrayList();
        }
        
        @CanIgnoreReturnValue
        public Builder<R, C, V> orderRowsBy(final Comparator<? super R> rowComparator) {
            this.rowComparator = Preconditions.checkNotNull(rowComparator);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<R, C, V> orderColumnsBy(final Comparator<? super C> columnComparator) {
            this.columnComparator = Preconditions.checkNotNull(columnComparator);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<R, C, V> put(final R rowKey, final C columnKey, final V value) {
            this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<R, C, V> put(final Table.Cell<? extends R, ? extends C, ? extends V> cell) {
            if (cell instanceof Tables.ImmutableCell) {
                Preconditions.checkNotNull(cell.getRowKey());
                Preconditions.checkNotNull(cell.getColumnKey());
                Preconditions.checkNotNull(cell.getValue());
                final Table.Cell<R, C, V> immutableCell = (Table.Cell<R, C, V>)cell;
                this.cells.add(immutableCell);
            }
            else {
                this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<R, C, V> putAll(final Table<? extends R, ? extends C, ? extends V> table) {
            for (final Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
                this.put(cell);
            }
            return this;
        }
        
        Builder<R, C, V> combine(final Builder<R, C, V> other) {
            this.cells.addAll(other.cells);
            return this;
        }
        
        public ImmutableTable<R, C, V> build() {
            final int size = this.cells.size();
            switch (size) {
                case 0: {
                    return ImmutableTable.of();
                }
                case 1: {
                    return new SingletonImmutableTable<R, C, V>(Iterables.getOnlyElement(this.cells));
                }
                default: {
                    return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
                }
            }
        }
    }
    
    static final class SerializedForm implements Serializable
    {
        private final Object[] rowKeys;
        private final Object[] columnKeys;
        private final Object[] cellValues;
        private final int[] cellRowIndices;
        private final int[] cellColumnIndices;
        private static final long serialVersionUID = 0L;
        
        private SerializedForm(final Object[] rowKeys, final Object[] columnKeys, final Object[] cellValues, final int[] cellRowIndices, final int[] cellColumnIndices) {
            this.rowKeys = rowKeys;
            this.columnKeys = columnKeys;
            this.cellValues = cellValues;
            this.cellRowIndices = cellRowIndices;
            this.cellColumnIndices = cellColumnIndices;
        }
        
        static SerializedForm create(final ImmutableTable<?, ?, ?> table, final int[] cellRowIndices, final int[] cellColumnIndices) {
            return new SerializedForm(table.rowKeySet().toArray(), table.columnKeySet().toArray(), table.values().toArray(), cellRowIndices, cellColumnIndices);
        }
        
        Object readResolve() {
            if (this.cellValues.length == 0) {
                return ImmutableTable.of();
            }
            if (this.cellValues.length == 1) {
                return ImmutableTable.of(this.rowKeys[0], this.columnKeys[0], this.cellValues[0]);
            }
            final ImmutableList.Builder<Table.Cell<Object, Object, Object>> cellListBuilder = new ImmutableList.Builder<Table.Cell<Object, Object, Object>>(this.cellValues.length);
            for (int i = 0; i < this.cellValues.length; ++i) {
                cellListBuilder.add(ImmutableTable.cellOf(this.rowKeys[this.cellRowIndices[i]], this.columnKeys[this.cellColumnIndices[i]], this.cellValues[i]));
            }
            return RegularImmutableTable.forOrderedComponents(cellListBuilder.build(), (ImmutableSet<Object>)ImmutableSet.copyOf(this.rowKeys), (ImmutableSet<Object>)ImmutableSet.copyOf(this.columnKeys));
        }
    }
}
