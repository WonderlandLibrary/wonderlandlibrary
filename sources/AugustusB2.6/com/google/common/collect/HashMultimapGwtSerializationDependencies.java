// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
abstract class HashMultimapGwtSerializationDependencies<K, V> extends AbstractSetMultimap<K, V>
{
    HashMultimapGwtSerializationDependencies(final Map<K, Collection<V>> map) {
        super(map);
    }
}
