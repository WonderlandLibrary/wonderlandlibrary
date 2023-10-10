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
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2IntSortedMap extends Int2IntMap, SortedMap<Integer, Integer>
{
    Int2IntSortedMap subMap(final int p0, final int p1);
    
    Int2IntSortedMap headMap(final int p0);
    
    Int2IntSortedMap tailMap(final int p0);
    
    int firstIntKey();
    
    int lastIntKey();
    
    @Deprecated
    default Int2IntSortedMap subMap(final Integer from, final Integer to) {
        return this.subMap((int)from, (int)to);
    }
    
    @Deprecated
    default Int2IntSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    default Int2IntSortedMap tailMap(final Integer from) {
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
    default ObjectSortedSet<Map.Entry<Integer, Integer>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Integer>>)this.int2IntEntrySet();
    }
    
    ObjectSortedSet<Int2IntMap.Entry> int2IntEntrySet();
    
    IntSortedSet keySet();
    
    IntCollection values();
    
    IntComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Int2IntMap.Entry>, FastEntrySet
    {
        ObjectBidirectionalIterator<Int2IntMap.Entry> fastIterator();
        
        ObjectBidirectionalIterator<Int2IntMap.Entry> fastIterator(final Int2IntMap.Entry p0);
    }
}
