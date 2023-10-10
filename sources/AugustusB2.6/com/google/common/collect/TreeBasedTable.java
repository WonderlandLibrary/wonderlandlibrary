// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import java.io.Serializable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Set;
import javax.annotation.CheckForNull;
import com.google.common.base.Function;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.Objects;
import com.google.common.base.Supplier;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true)
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V>
{
    private final Comparator<? super C> columnComparator;
    private static final long serialVersionUID = 0L;
    
    public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
        return new TreeBasedTable<R, C, V>(Ordering.natural(), Ordering.natural());
    }
    
    public static <R, C, V> TreeBasedTable<R, C, V> create(final Comparator<? super R> rowComparator, final Comparator<? super C> columnComparator) {
        Preconditions.checkNotNull(rowComparator);
        Preconditions.checkNotNull(columnComparator);
        return new TreeBasedTable<R, C, V>(rowComparator, columnComparator);
    }
    
    public static <R, C, V> TreeBasedTable<R, C, V> create(final TreeBasedTable<R, C, ? extends V> table) {
        final TreeBasedTable<R, C, V> result = new TreeBasedTable<R, C, V>(table.rowComparator(), table.columnComparator());
        result.putAll((Table)table);
        return result;
    }
    
    TreeBasedTable(final Comparator<? super R> rowComparator, final Comparator<? super C> columnComparator) {
        super(new TreeMap(rowComparator), new Factory(columnComparator));
        this.columnComparator = columnComparator;
    }
    
    @Deprecated
    public Comparator<? super R> rowComparator() {
        return Objects.requireNonNull(this.rowKeySet().comparator());
    }
    
    @Deprecated
    public Comparator<? super C> columnComparator() {
        return this.columnComparator;
    }
    
    @Override
    public SortedMap<C, V> row(final R rowKey) {
        return new TreeRow(rowKey);
    }
    
    @Override
    public SortedSet<R> rowKeySet() {
        return super.rowKeySet();
    }
    
    @Override
    public SortedMap<R, Map<C, V>> rowMap() {
        return super.rowMap();
    }
    
    @Override
    Iterator<C> createColumnKeyIterator() {
        final Comparator<? super C> comparator = this.columnComparator();
        final Iterator<C> merged = (Iterator<C>)Iterators.mergeSorted(Iterables.transform((Iterable<Map<C, V>>)this.backingMap.values(), (Function<? super Map<C, V>, ? extends Iterator<?>>)new Function<Map<C, V>, Iterator<C>>(this) {
            @Override
            public Iterator<C> apply(final Map<C, V> input) {
                return input.keySet().iterator();
            }
        }), (Comparator<? super Object>)comparator);
        return new AbstractIterator<C>(this) {
            @CheckForNull
            C lastValue;
            
            @CheckForNull
            @Override
            protected C computeNext() {
                while (merged.hasNext()) {
                    final C next = merged.next();
                    final boolean duplicate = this.lastValue != null && comparator.compare(next, this.lastValue) == 0;
                    if (!duplicate) {
                        return this.lastValue = next;
                    }
                }
                this.lastValue = null;
                return this.endOfData();
            }
        };
    }
    
    private static class Factory<C, V> implements Supplier<TreeMap<C, V>>, Serializable
    {
        final Comparator<? super C> comparator;
        private static final long serialVersionUID = 0L;
        
        Factory(final Comparator<? super C> comparator) {
            this.comparator = comparator;
        }
        
        @Override
        public TreeMap<C, V> get() {
            return new TreeMap<C, V>(this.comparator);
        }
    }
    
    private class TreeRow extends Row implements SortedMap<C, V>
    {
        @CheckForNull
        final C lowerBound;
        @CheckForNull
        final C upperBound;
        @CheckForNull
        transient SortedMap<C, V> wholeRow;
        
        TreeRow(final TreeBasedTable treeBasedTable, final R rowKey) {
            this(rowKey, null, null);
        }
        
        TreeRow(@CheckForNull final R rowKey, @CheckForNull final C lowerBound, final C upperBound) {
            (StandardTable<R, C, V>)TreeBasedTable.this.super(rowKey);
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            Preconditions.checkArgument(lowerBound == null || upperBound == null || this.compare(lowerBound, upperBound) <= 0);
        }
        
        @Override
        public SortedSet<C> keySet() {
            return new Maps.SortedKeySet<C, Object>(this);
        }
        
        @Override
        public Comparator<? super C> comparator() {
            return TreeBasedTable.this.columnComparator();
        }
        
        int compare(final Object a, final Object b) {
            final Comparator<Object> cmp = (Comparator<Object>)this.comparator();
            return cmp.compare(a, b);
        }
        
        boolean rangeContains(@CheckForNull final Object o) {
            return o != null && (this.lowerBound == null || this.compare(this.lowerBound, o) <= 0) && (this.upperBound == null || this.compare(this.upperBound, o) > 0);
        }
        
        @Override
        public SortedMap<C, V> subMap(final C fromKey, final C toKey) {
            Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(fromKey)) && this.rangeContains(Preconditions.checkNotNull(toKey)));
            return new TreeRow((R)this.rowKey, fromKey, toKey);
        }
        
        @Override
        public SortedMap<C, V> headMap(final C toKey) {
            Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(toKey)));
            return new TreeRow((R)this.rowKey, this.lowerBound, toKey);
        }
        
        @Override
        public SortedMap<C, V> tailMap(final C fromKey) {
            Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(fromKey)));
            return new TreeRow((R)this.rowKey, fromKey, this.upperBound);
        }
        
        @Override
        public C firstKey() {
            this.updateBackingRowMapField();
            if (this.backingRowMap == null) {
                throw new NoSuchElementException();
            }
            return ((SortedMap)this.backingRowMap).firstKey();
        }
        
        @Override
        public C lastKey() {
            this.updateBackingRowMapField();
            if (this.backingRowMap == null) {
                throw new NoSuchElementException();
            }
            return ((SortedMap)this.backingRowMap).lastKey();
        }
        
        void updateWholeRowField() {
            if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey))) {
                this.wholeRow = (SortedMap<C, V>)(SortedMap)TreeBasedTable.this.backingMap.get(this.rowKey);
            }
        }
        
        @CheckForNull
        @Override
        SortedMap<C, V> computeBackingRowMap() {
            this.updateWholeRowField();
            SortedMap<C, V> map = this.wholeRow;
            if (map != null) {
                if (this.lowerBound != null) {
                    map = map.tailMap(this.lowerBound);
                }
                if (this.upperBound != null) {
                    map = map.headMap(this.upperBound);
                }
                return map;
            }
            return null;
        }
        
        @Override
        void maintainEmptyInvariant() {
            this.updateWholeRowField();
            if (this.wholeRow != null && this.wholeRow.isEmpty()) {
                TreeBasedTable.this.backingMap.remove(this.rowKey);
                this.wholeRow = null;
                this.backingRowMap = null;
            }
        }
        
        @Override
        public boolean containsKey(@CheckForNull final Object key) {
            return this.rangeContains(key) && super.containsKey(key);
        }
        
        @CheckForNull
        @Override
        public V put(final C key, final V value) {
            Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(key)));
            return super.put(key, value);
        }
    }
}
