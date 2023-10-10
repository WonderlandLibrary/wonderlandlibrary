// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use ImmutableTable, HashBasedTable, or another implementation")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Table<R, C, V>
{
    boolean contains(@CheckForNull @CompatibleWith("R") final Object p0, @CheckForNull @CompatibleWith("C") final Object p1);
    
    boolean containsRow(@CheckForNull @CompatibleWith("R") final Object p0);
    
    boolean containsColumn(@CheckForNull @CompatibleWith("C") final Object p0);
    
    boolean containsValue(@CheckForNull @CompatibleWith("V") final Object p0);
    
    @CheckForNull
    V get(@CheckForNull @CompatibleWith("R") final Object p0, @CheckForNull @CompatibleWith("C") final Object p1);
    
    boolean isEmpty();
    
    int size();
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
    
    void clear();
    
    @CheckForNull
    @CanIgnoreReturnValue
    V put(@ParametricNullness final R p0, @ParametricNullness final C p1, @ParametricNullness final V p2);
    
    void putAll(final Table<? extends R, ? extends C, ? extends V> p0);
    
    @CheckForNull
    @CanIgnoreReturnValue
    V remove(@CheckForNull @CompatibleWith("R") final Object p0, @CheckForNull @CompatibleWith("C") final Object p1);
    
    Map<C, V> row(@ParametricNullness final R p0);
    
    Map<R, V> column(@ParametricNullness final C p0);
    
    Set<Cell<R, C, V>> cellSet();
    
    Set<R> rowKeySet();
    
    Set<C> columnKeySet();
    
    Collection<V> values();
    
    Map<R, Map<C, V>> rowMap();
    
    Map<C, Map<R, V>> columnMap();
    
    public interface Cell<R, C, V>
    {
        @ParametricNullness
        R getRowKey();
        
        @ParametricNullness
        C getColumnKey();
        
        @ParametricNullness
        V getValue();
        
        boolean equals(@CheckForNull final Object p0);
        
        int hashCode();
    }
}
