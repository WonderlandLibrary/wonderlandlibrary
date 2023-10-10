// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.SortedSet;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Collections;
import javax.annotation.CheckForNull;
import com.google.common.base.Preconditions;
import java.util.function.BinaryOperator;
import com.google.common.annotations.Beta;
import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Tables
{
    private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER;
    
    private Tables() {
    }
    
    @Beta
    public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(final java.util.function.Function<? super T, ? extends R> rowFunction, final java.util.function.Function<? super T, ? extends C> columnFunction, final java.util.function.Function<? super T, ? extends V> valueFunction, final Supplier<I> tableSupplier) {
        return TableCollectors.toTable((java.util.function.Function<? super T, ?>)rowFunction, (java.util.function.Function<? super T, ?>)columnFunction, (java.util.function.Function<? super T, ?>)valueFunction, tableSupplier);
    }
    
    public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(final java.util.function.Function<? super T, ? extends R> rowFunction, final java.util.function.Function<? super T, ? extends C> columnFunction, final java.util.function.Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction, final Supplier<I> tableSupplier) {
        return TableCollectors.toTable((java.util.function.Function<? super T, ?>)rowFunction, (java.util.function.Function<? super T, ?>)columnFunction, valueFunction, mergeFunction, tableSupplier);
    }
    
    public static <R, C, V> Table.Cell<R, C, V> immutableCell(@ParametricNullness final R rowKey, @ParametricNullness final C columnKey, @ParametricNullness final V value) {
        return new ImmutableCell<R, C, V>(rowKey, columnKey, value);
    }
    
    public static <R, C, V> Table<C, R, V> transpose(final Table<R, C, V> table) {
        return (table instanceof TransposeTable) ? ((TransposeTable)table).original : new TransposeTable<C, R, V>((Table<Object, Object, Object>)table);
    }
    
    @Beta
    public static <R, C, V> Table<R, C, V> newCustomTable(final Map<R, Map<C, V>> backingMap, final com.google.common.base.Supplier<? extends Map<C, V>> factory) {
        Preconditions.checkArgument(backingMap.isEmpty());
        Preconditions.checkNotNull(factory);
        return new StandardTable<R, C, V>(backingMap, factory);
    }
    
    @Beta
    public static <R, C, V1, V2> Table<R, C, V2> transformValues(final Table<R, C, V1> fromTable, final Function<? super V1, V2> function) {
        return (Table<R, C, V2>)new TransformedTable((Table<Object, Object, Object>)fromTable, (Function<? super Object, Object>)function);
    }
    
    public static <R, C, V> Table<R, C, V> unmodifiableTable(final Table<? extends R, ? extends C, ? extends V> table) {
        return new UnmodifiableTable<R, C, V>(table);
    }
    
    @Beta
    public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(final RowSortedTable<R, ? extends C, ? extends V> table) {
        return new UnmodifiableRowSortedMap<R, C, V>(table);
    }
    
    private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
        return (Function<Map<K, V>, Map<K, V>>)Tables.UNMODIFIABLE_WRAPPER;
    }
    
    public static <R, C, V> Table<R, C, V> synchronizedTable(final Table<R, C, V> table) {
        return Synchronized.table(table, null);
    }
    
    static boolean equalsImpl(final Table<?, ?, ?> table, @CheckForNull final Object obj) {
        if (obj == table) {
            return true;
        }
        if (obj instanceof Table) {
            final Table<?, ?, ?> that = (Table<?, ?, ?>)obj;
            return table.cellSet().equals(that.cellSet());
        }
        return false;
    }
    
    static {
        UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>() {
            @Override
            public Map<Object, Object> apply(final Map<Object, Object> input) {
                return Collections.unmodifiableMap((Map<?, ?>)input);
            }
        };
    }
    
    static final class ImmutableCell<R, C, V> extends AbstractCell<R, C, V> implements Serializable
    {
        @ParametricNullness
        private final R rowKey;
        @ParametricNullness
        private final C columnKey;
        @ParametricNullness
        private final V value;
        private static final long serialVersionUID = 0L;
        
        ImmutableCell(@ParametricNullness final R rowKey, @ParametricNullness final C columnKey, @ParametricNullness final V value) {
            this.rowKey = rowKey;
            this.columnKey = columnKey;
            this.value = value;
        }
        
        @ParametricNullness
        @Override
        public R getRowKey() {
            return this.rowKey;
        }
        
        @ParametricNullness
        @Override
        public C getColumnKey() {
            return this.columnKey;
        }
        
        @ParametricNullness
        @Override
        public V getValue() {
            return this.value;
        }
    }
    
    abstract static class AbstractCell<R, C, V> implements Table.Cell<R, C, V>
    {
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Table.Cell) {
                final Table.Cell<?, ?, ?> other = (Table.Cell<?, ?, ?>)obj;
                return Objects.equal(this.getRowKey(), other.getRowKey()) && Objects.equal(this.getColumnKey(), other.getColumnKey()) && Objects.equal(this.getValue(), other.getValue());
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.getRowKey(), this.getColumnKey(), this.getValue());
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.getRowKey());
            final String value2 = String.valueOf(this.getColumnKey());
            final String value3 = String.valueOf(this.getValue());
            return new StringBuilder(4 + String.valueOf(value).length() + String.valueOf(value2).length() + String.valueOf(value3).length()).append("(").append(value).append(",").append(value2).append(")=").append(value3).toString();
        }
    }
    
    private static class TransposeTable<C, R, V> extends AbstractTable<C, R, V>
    {
        final Table<R, C, V> original;
        private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL;
        
        TransposeTable(final Table<R, C, V> original) {
            this.original = Preconditions.checkNotNull(original);
        }
        
        @Override
        public void clear() {
            this.original.clear();
        }
        
        @Override
        public Map<C, V> column(@ParametricNullness final R columnKey) {
            return this.original.row(columnKey);
        }
        
        @Override
        public Set<R> columnKeySet() {
            return this.original.rowKeySet();
        }
        
        @Override
        public Map<R, Map<C, V>> columnMap() {
            return this.original.rowMap();
        }
        
        @Override
        public boolean contains(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            return this.original.contains(columnKey, rowKey);
        }
        
        @Override
        public boolean containsColumn(@CheckForNull final Object columnKey) {
            return this.original.containsRow(columnKey);
        }
        
        @Override
        public boolean containsRow(@CheckForNull final Object rowKey) {
            return this.original.containsColumn(rowKey);
        }
        
        @Override
        public boolean containsValue(@CheckForNull final Object value) {
            return this.original.containsValue(value);
        }
        
        @CheckForNull
        @Override
        public V get(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            return this.original.get(columnKey, rowKey);
        }
        
        @CheckForNull
        @Override
        public V put(@ParametricNullness final C rowKey, @ParametricNullness final R columnKey, @ParametricNullness final V value) {
            return this.original.put(columnKey, rowKey, value);
        }
        
        @Override
        public void putAll(final Table<? extends C, ? extends R, ? extends V> table) {
            this.original.putAll(Tables.transpose(table));
        }
        
        @CheckForNull
        @Override
        public V remove(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            return this.original.remove(columnKey, rowKey);
        }
        
        @Override
        public Map<R, V> row(@ParametricNullness final C rowKey) {
            return this.original.column(rowKey);
        }
        
        @Override
        public Set<C> rowKeySet() {
            return this.original.columnKeySet();
        }
        
        @Override
        public Map<C, Map<R, V>> rowMap() {
            return this.original.columnMap();
        }
        
        @Override
        public int size() {
            return this.original.size();
        }
        
        @Override
        public Collection<V> values() {
            return this.original.values();
        }
        
        @Override
        Iterator<Table.Cell<C, R, V>> cellIterator() {
            return Iterators.transform(this.original.cellSet().iterator(), (Function<? super Table.Cell<R, C, V>, ? extends Table.Cell<C, R, V>>)TransposeTable.TRANSPOSE_CELL);
        }
        
        @Override
        Spliterator<Table.Cell<C, R, V>> cellSpliterator() {
            return CollectSpliterators.map(this.original.cellSet().spliterator(), (java.util.function.Function<? super Table.Cell<R, C, V>, ? extends Table.Cell<C, R, V>>)TransposeTable.TRANSPOSE_CELL);
        }
        
        static {
            TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>() {
                @Override
                public Table.Cell<?, ?, ?> apply(final Table.Cell<?, ?, ?> cell) {
                    return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
                }
            };
        }
    }
    
    private static class TransformedTable<R, C, V1, V2> extends AbstractTable<R, C, V2>
    {
        final Table<R, C, V1> fromTable;
        final Function<? super V1, V2> function;
        
        TransformedTable(final Table<R, C, V1> fromTable, final Function<? super V1, V2> function) {
            this.fromTable = Preconditions.checkNotNull(fromTable);
            this.function = Preconditions.checkNotNull(function);
        }
        
        @Override
        public boolean contains(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            return this.fromTable.contains(rowKey, columnKey);
        }
        
        @CheckForNull
        @Override
        public V2 get(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            return this.contains(rowKey, columnKey) ? this.function.apply((Object)NullnessCasts.uncheckedCastNullableTToT(this.fromTable.get(rowKey, columnKey))) : null;
        }
        
        @Override
        public int size() {
            return this.fromTable.size();
        }
        
        @Override
        public void clear() {
            this.fromTable.clear();
        }
        
        @CheckForNull
        @Override
        public V2 put(@ParametricNullness final R rowKey, @ParametricNullness final C columnKey, @ParametricNullness final V2 value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Table<? extends R, ? extends C, ? extends V2> table) {
            throw new UnsupportedOperationException();
        }
        
        @CheckForNull
        @Override
        public V2 remove(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            return this.contains(rowKey, columnKey) ? this.function.apply((Object)NullnessCasts.uncheckedCastNullableTToT(this.fromTable.remove(rowKey, columnKey))) : null;
        }
        
        @Override
        public Map<C, V2> row(@ParametricNullness final R rowKey) {
            return Maps.transformValues(this.fromTable.row(rowKey), this.function);
        }
        
        @Override
        public Map<R, V2> column(@ParametricNullness final C columnKey) {
            return Maps.transformValues(this.fromTable.column(columnKey), this.function);
        }
        
        Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
            return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>() {
                @Override
                public Table.Cell<R, C, V2> apply(final Table.Cell<R, C, V1> cell) {
                    return Tables.immutableCell(cell.getRowKey(), cell.getColumnKey(), TransformedTable.this.function.apply((Object)cell.getValue()));
                }
            };
        }
        
        @Override
        Iterator<Table.Cell<R, C, V2>> cellIterator() {
            return Iterators.transform(this.fromTable.cellSet().iterator(), (Function<? super Table.Cell<R, C, V1>, ? extends Table.Cell<R, C, V2>>)this.cellFunction());
        }
        
        @Override
        Spliterator<Table.Cell<R, C, V2>> cellSpliterator() {
            return CollectSpliterators.map(this.fromTable.cellSet().spliterator(), (java.util.function.Function<? super Table.Cell<R, C, V1>, ? extends Table.Cell<R, C, V2>>)this.cellFunction());
        }
        
        @Override
        public Set<R> rowKeySet() {
            return this.fromTable.rowKeySet();
        }
        
        @Override
        public Set<C> columnKeySet() {
            return this.fromTable.columnKeySet();
        }
        
        @Override
        Collection<V2> createValues() {
            return Collections2.transform(this.fromTable.values(), this.function);
        }
        
        @Override
        public Map<R, Map<C, V2>> rowMap() {
            final Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>() {
                @Override
                public Map<C, V2> apply(final Map<C, V1> row) {
                    return Maps.transformValues(row, TransformedTable.this.function);
                }
            };
            return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
        }
        
        @Override
        public Map<C, Map<R, V2>> columnMap() {
            final Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>() {
                @Override
                public Map<R, V2> apply(final Map<R, V1> column) {
                    return Maps.transformValues(column, TransformedTable.this.function);
                }
            };
            return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
        }
    }
    
    private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable
    {
        final Table<? extends R, ? extends C, ? extends V> delegate;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableTable(final Table<? extends R, ? extends C, ? extends V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected Table<R, C, V> delegate() {
            return (Table<R, C, V>)this.delegate;
        }
        
        @Override
        public Set<Table.Cell<R, C, V>> cellSet() {
            return Collections.unmodifiableSet((Set<? extends Table.Cell<R, C, V>>)super.cellSet());
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Map<R, V> column(@ParametricNullness final C columnKey) {
            return Collections.unmodifiableMap(super.column(columnKey));
        }
        
        @Override
        public Set<C> columnKeySet() {
            return Collections.unmodifiableSet(super.columnKeySet());
        }
        
        @Override
        public Map<C, Map<R, V>> columnMap() {
            final Function<Map<R, V>, Map<R, V>> wrapper = (Function<Map<R, V>, Map<R, V>>)unmodifiableWrapper();
            return Collections.unmodifiableMap((Map<? extends C, ? extends Map<R, V>>)Maps.transformValues(super.columnMap(), (Function<? super Map<Object, Object>, ? extends V>)wrapper));
        }
        
        @CheckForNull
        @Override
        public V put(@ParametricNullness final R rowKey, @ParametricNullness final C columnKey, @ParametricNullness final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Table<? extends R, ? extends C, ? extends V> table) {
            throw new UnsupportedOperationException();
        }
        
        @CheckForNull
        @Override
        public V remove(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Map<C, V> row(@ParametricNullness final R rowKey) {
            return Collections.unmodifiableMap(super.row(rowKey));
        }
        
        @Override
        public Set<R> rowKeySet() {
            return Collections.unmodifiableSet(super.rowKeySet());
        }
        
        @Override
        public Map<R, Map<C, V>> rowMap() {
            final Function<Map<C, V>, Map<C, V>> wrapper = (Function<Map<C, V>, Map<C, V>>)unmodifiableWrapper();
            return Collections.unmodifiableMap((Map<? extends R, ? extends Map<C, V>>)Maps.transformValues(super.rowMap(), (Function<? super Map<Object, Object>, ? extends V>)wrapper));
        }
        
        @Override
        public Collection<V> values() {
            return Collections.unmodifiableCollection(super.values());
        }
    }
    
    static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V>
    {
        private static final long serialVersionUID = 0L;
        
        public UnmodifiableRowSortedMap(final RowSortedTable<R, ? extends C, ? extends V> delegate) {
            super(delegate);
        }
        
        @Override
        protected RowSortedTable<R, C, V> delegate() {
            return (RowSortedTable<R, C, V>)(RowSortedTable)super.delegate();
        }
        
        @Override
        public SortedMap<R, Map<C, V>> rowMap() {
            final Function<Map<C, V>, Map<C, V>> wrapper = (Function<Map<C, V>, Map<C, V>>)unmodifiableWrapper();
            return Collections.unmodifiableSortedMap((SortedMap<R, ? extends Map<C, V>>)Maps.transformValues((SortedMap<K, Map<C, V>>)this.delegate().rowMap(), (Function<? super Map<C, V>, ? extends V>)wrapper));
        }
        
        @Override
        public SortedSet<R> rowKeySet() {
            return Collections.unmodifiableSortedSet(this.delegate().rowKeySet());
        }
    }
}
