// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Map;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingTable<R, C, V> extends ForwardingObject implements Table<R, C, V>
{
    protected ForwardingTable() {
    }
    
    @Override
    protected abstract Table<R, C, V> delegate();
    
    @Override
    public Set<Cell<R, C, V>> cellSet() {
        return this.delegate().cellSet();
    }
    
    @Override
    public void clear() {
        this.delegate().clear();
    }
    
    @Override
    public Map<R, V> column(@ParametricNullness final C columnKey) {
        return this.delegate().column(columnKey);
    }
    
    @Override
    public Set<C> columnKeySet() {
        return this.delegate().columnKeySet();
    }
    
    @Override
    public Map<C, Map<R, V>> columnMap() {
        return this.delegate().columnMap();
    }
    
    @Override
    public boolean contains(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
        return this.delegate().contains(rowKey, columnKey);
    }
    
    @Override
    public boolean containsColumn(@CheckForNull final Object columnKey) {
        return this.delegate().containsColumn(columnKey);
    }
    
    @Override
    public boolean containsRow(@CheckForNull final Object rowKey) {
        return this.delegate().containsRow(rowKey);
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        return this.delegate().containsValue(value);
    }
    
    @CheckForNull
    @Override
    public V get(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
        return this.delegate().get(rowKey, columnKey);
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V put(@ParametricNullness final R rowKey, @ParametricNullness final C columnKey, @ParametricNullness final V value) {
        return this.delegate().put(rowKey, columnKey, value);
    }
    
    @Override
    public void putAll(final Table<? extends R, ? extends C, ? extends V> table) {
        this.delegate().putAll(table);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V remove(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
        return this.delegate().remove(rowKey, columnKey);
    }
    
    @Override
    public Map<C, V> row(@ParametricNullness final R rowKey) {
        return this.delegate().row(rowKey);
    }
    
    @Override
    public Set<R> rowKeySet() {
        return this.delegate().rowKeySet();
    }
    
    @Override
    public Map<R, Map<C, V>> rowMap() {
        return this.delegate().rowMap();
    }
    
    @Override
    public int size() {
        return this.delegate().size();
    }
    
    @Override
    public Collection<V> values() {
        return this.delegate().values();
    }
    
    @Override
    public boolean equals(@CheckForNull final Object obj) {
        return obj == this || this.delegate().equals(obj);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
}
