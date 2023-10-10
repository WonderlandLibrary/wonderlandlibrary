// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2ObjectSortedMap<V> extends Int2ObjectMap<V>, SortedMap<Integer, V>
{
    Int2ObjectSortedMap<V> subMap(final int p0, final int p1);
    
    Int2ObjectSortedMap<V> headMap(final int p0);
    
    Int2ObjectSortedMap<V> tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    @Deprecated
    default Int2ObjectSortedMap<V> subMap(final Integer from, final Integer to) {
        return this.subMap((int)from, (int)to);
    }
    
    @Deprecated
    default Int2ObjectSortedMap<V> headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    default Int2ObjectSortedMap<V> tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    default Integer firstKey() {
        return this.firstIntKey();
    }
    
    @Deprecated
    default Integer lastKey() {
        return this.lastIntKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
    }
    
    ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet();
    
    IntSortedSet keySet();
    
    ObjectCollection<V> values();
    
    IntComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Int2ObjectMap.Entry<V>>, FastEntrySet<V>
    {
        ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator(final Int2ObjectMap.Entry<V> p0);
    }
}
