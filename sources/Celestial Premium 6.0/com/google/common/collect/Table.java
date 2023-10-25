/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 *  com.google.errorprone.annotations.CompatibleWith
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface Table<R, C, V> {
    public boolean contains(@Nullable @CompatibleWith(value="R") Object var1, @Nullable @CompatibleWith(value="C") Object var2);

    public boolean containsRow(@Nullable @CompatibleWith(value="R") Object var1);

    public boolean containsColumn(@Nullable @CompatibleWith(value="C") Object var1);

    public boolean containsValue(@Nullable @CompatibleWith(value="V") Object var1);

    public V get(@Nullable @CompatibleWith(value="R") Object var1, @Nullable @CompatibleWith(value="C") Object var2);

    public boolean isEmpty();

    public int size();

    public boolean equals(@Nullable Object var1);

    public int hashCode();

    public void clear();

    @Nullable
    @CanIgnoreReturnValue
    public V put(R var1, C var2, V var3);

    public void putAll(Table<? extends R, ? extends C, ? extends V> var1);

    @Nullable
    @CanIgnoreReturnValue
    public V remove(@Nullable @CompatibleWith(value="R") Object var1, @Nullable @CompatibleWith(value="C") Object var2);

    public Map<C, V> row(R var1);

    public Map<R, V> column(C var1);

    public Set<Cell<R, C, V>> cellSet();

    public Set<R> rowKeySet();

    public Set<C> columnKeySet();

    public Collection<V> values();

    public Map<R, Map<C, V>> rowMap();

    public Map<C, Map<R, V>> columnMap();

    public static interface Cell<R, C, V> {
        @Nullable
        public R getRowKey();

        @Nullable
        public C getColumnKey();

        @Nullable
        public V getValue();

        public boolean equals(@Nullable Object var1);

        public int hashCode();
    }
}

