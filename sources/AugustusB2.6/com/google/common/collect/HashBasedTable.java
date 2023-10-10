// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.Serializable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Set;
import java.util.Collection;
import com.google.common.base.Supplier;
import java.util.Map;
import java.util.LinkedHashMap;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true)
public class HashBasedTable<R, C, V> extends StandardTable<R, C, V>
{
    private static final long serialVersionUID = 0L;
    
    public static <R, C, V> HashBasedTable<R, C, V> create() {
        return new HashBasedTable<R, C, V>(new LinkedHashMap<R, Map<C, V>>(), new Factory<C, V>(0));
    }
    
    public static <R, C, V> HashBasedTable<R, C, V> create(final int expectedRows, final int expectedCellsPerRow) {
        CollectPreconditions.checkNonnegative(expectedCellsPerRow, "expectedCellsPerRow");
        final Map<R, Map<C, V>> backingMap = (Map<R, Map<C, V>>)Maps.newLinkedHashMapWithExpectedSize(expectedRows);
        return new HashBasedTable<R, C, V>(backingMap, new Factory<C, V>(expectedCellsPerRow));
    }
    
    public static <R, C, V> HashBasedTable<R, C, V> create(final Table<? extends R, ? extends C, ? extends V> table) {
        final HashBasedTable<R, C, V> result = create();
        result.putAll(table);
        return result;
    }
    
    HashBasedTable(final Map<R, Map<C, V>> backingMap, final Factory<C, V> factory) {
        super(backingMap, factory);
    }
    
    private static class Factory<C, V> implements Supplier<Map<C, V>>, Serializable
    {
        final int expectedSize;
        private static final long serialVersionUID = 0L;
        
        Factory(final int expectedSize) {
            this.expectedSize = expectedSize;
        }
        
        @Override
        public Map<C, V> get() {
            return (Map<C, V>)Maps.newLinkedHashMapWithExpectedSize(this.expectedSize);
        }
    }
}
