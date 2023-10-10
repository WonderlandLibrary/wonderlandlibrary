/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapPopulator {
    public static <K, V> Map<K, V> createMap(Iterable<K> keys, Iterable<V> values) {
        return MapPopulator.populateMap(keys, values, Maps.newLinkedHashMap());
    }

    public static <K, V> Map<K, V> populateMap(Iterable<K> keys, Iterable<V> values, Map<K, V> map) {
        Iterator<V> iterator = values.iterator();
        Iterator<K> iterator2 = keys.iterator();
        while (true) {
            if (!iterator2.hasNext()) {
                if (!iterator.hasNext()) return map;
                throw new NoSuchElementException();
            }
            K k = iterator2.next();
            map.put(k, iterator.next());
        }
    }
}

