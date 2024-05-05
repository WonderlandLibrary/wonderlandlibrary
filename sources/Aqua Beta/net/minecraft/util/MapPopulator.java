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
        Iterator iterator = values.iterator();
        for (Object k : keys) {
            map.put(k, iterator.next());
        }
        if (iterator.hasNext()) {
            throw new NoSuchElementException();
        }
        return map;
    }
}
