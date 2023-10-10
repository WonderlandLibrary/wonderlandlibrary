// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import com.google.common.base.Preconditions;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class TableCollectors
{
    static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(final Function<? super T, ? extends R> rowFunction, final Function<? super T, ? extends C> columnFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(rowFunction, (Object)"rowFunction");
        Preconditions.checkNotNull(columnFunction, (Object)"columnFunction");
        Preconditions.checkNotNull(valueFunction, (Object)"valueFunction");
        return Collector.of(ImmutableTable.Builder::new, (builder, t) -> builder.put(rowFunction.apply((Object)t), columnFunction.apply((Object)t), valueFunction.apply((Object)t)), ImmutableTable.Builder::combine, ImmutableTable.Builder::build, new Collector.Characteristics[0]);
    }
    
    static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(final Function<? super T, ? extends R> rowFunction, final Function<? super T, ? extends C> columnFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        Preconditions.checkNotNull(rowFunction, (Object)"rowFunction");
        Preconditions.checkNotNull(columnFunction, (Object)"columnFunction");
        Preconditions.checkNotNull(valueFunction, (Object)"valueFunction");
        Preconditions.checkNotNull(mergeFunction, (Object)"mergeFunction");
        return Collector.of(() -> new ImmutableTableCollectorState(), (state, input) -> state.put(rowFunction.apply((Object)input), columnFunction.apply((Object)input), (V)valueFunction.apply((Object)input), mergeFunction), (s1, s2) -> s1.combine(s2, mergeFunction), state -> state.toTable(), new Collector.Characteristics[0]);
    }
    
    static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(final Function<? super T, ? extends R> rowFunction, final Function<? super T, ? extends C> columnFunction, final Function<? super T, ? extends V> valueFunction, final Supplier<I> tableSupplier) {
        final Object o;
        final String s;
        final String s2;
        return toTable((Function<? super T, ?>)rowFunction, (Function<? super T, ?>)columnFunction, (Function<? super T, ?>)valueFunction, (v1, v2) -> {
            // new(java.lang.IllegalStateException.class)
            String.valueOf(v1);
            String.valueOf(v2);
            new IllegalStateException(new StringBuilder(24 + String.valueOf(s).length() + String.valueOf(s2).length()).append("Conflicting values ").append(s).append(" and ").append(s2).toString());
            throw o;
        }, tableSupplier);
    }
    
    static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(final Function<? super T, ? extends R> rowFunction, final Function<? super T, ? extends C> columnFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction, final Supplier<I> tableSupplier) {
        Preconditions.checkNotNull(rowFunction);
        Preconditions.checkNotNull(columnFunction);
        Preconditions.checkNotNull(valueFunction);
        Preconditions.checkNotNull(mergeFunction);
        Preconditions.checkNotNull(tableSupplier);
        final Iterator<Table.Cell<Object, Object, Object>> iterator;
        Table.Cell<Object, Object, Object> cell2;
        return (Collector<T, ?, I>)Collector.of((Supplier<?>)tableSupplier, (table, input) -> mergeTables(table, rowFunction.apply((Object)input), columnFunction.apply((Object)input), (V)valueFunction.apply((Object)input), mergeFunction), (table1, table2) -> {
            table2.cellSet().iterator();
            while (iterator.hasNext()) {
                cell2 = iterator.next();
                mergeTables(table1, cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue(), (BinaryOperator<Object>)mergeFunction);
            }
            return table1;
        }, new Collector.Characteristics[0]);
    }
    
    private static <R, C, V> void mergeTables(final Table<R, C, V> table, @ParametricNullness final R row, @ParametricNullness final C column, @ParametricNullness final V value, final BinaryOperator<V> mergeFunction) {
        Preconditions.checkNotNull(value);
        final V oldValue = table.get(row, column);
        if (oldValue == null) {
            table.put(row, column, value);
        }
        else {
            final V newValue = mergeFunction.apply(oldValue, value);
            if (newValue == null) {
                table.remove(row, column);
            }
            else {
                table.put(row, column, newValue);
            }
        }
    }
    
    private TableCollectors() {
    }
    
    private static final class ImmutableTableCollectorState<R, C, V>
    {
        final List<MutableCell<R, C, V>> insertionOrder;
        final Table<R, C, MutableCell<R, C, V>> table;
        
        private ImmutableTableCollectorState() {
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
        
        ImmutableTableCollectorState<R, C, V> combine(final ImmutableTableCollectorState<R, C, V> other, final BinaryOperator<V> merger) {
            for (final MutableCell<R, C, V> cell : other.insertionOrder) {
                this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue(), merger);
            }
            return this;
        }
        
        ImmutableTable<R, C, V> toTable() {
            return ImmutableTable.copyOf((Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>>)this.insertionOrder);
        }
    }
    
    private static final class MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V>
    {
        private final R row;
        private final C column;
        private V value;
        
        MutableCell(final R row, final C column, final V value) {
            this.row = Preconditions.checkNotNull(row, (Object)"row");
            this.column = Preconditions.checkNotNull(column, (Object)"column");
            this.value = Preconditions.checkNotNull(value, (Object)"value");
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
            Preconditions.checkNotNull(value, (Object)"value");
            this.value = Preconditions.checkNotNull(mergeFunction.apply(this.value, value), (Object)"mergeFunction.apply");
        }
    }
}
