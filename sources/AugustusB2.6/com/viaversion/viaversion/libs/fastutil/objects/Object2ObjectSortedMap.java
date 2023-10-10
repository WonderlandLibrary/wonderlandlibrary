// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ObjectSortedMap<K, V> extends Object2ObjectMap<K, V>, SortedMap<K, V>
{
    Object2ObjectSortedMap<K, V> subMap(final K p0, final K p1);
    
    Object2ObjectSortedMap<K, V> headMap(final K p0);
    
    Object2ObjectSortedMap<K, V> tailMap(final K p0);
    
    default ObjectSortedSet<Map.Entry<K, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
    }
    
    ObjectSortedSet<Object2ObjectMap.Entry<K, V>> object2ObjectEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    ObjectCollection<V> values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K, V> extends ObjectSortedSet<Object2ObjectMap.Entry<K, V>>, FastEntrySet<K, V>
    {
        ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> fastIterator();
        
        ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> fastIterator(final Object2ObjectMap.Entry<K, V> p0);
    }
}
