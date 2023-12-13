// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Set;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface RowSortedTable<R, C, V> extends Table<R, C, V>
{
    SortedSet<R> rowKeySet();
    
    SortedMap<R, Map<C, V>> rowMap();
}
