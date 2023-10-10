// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.internal;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

public class Maps
{
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
    
    public static <K, V> Map<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }
    
    public static <T> Map<T, T> newHashMap(final T... array) {
        final Map<T, T> hashMap = newHashMap();
        for (int i = 0; i < array.length; i += 2) {
            hashMap.put(array[i], array[i + 1]);
        }
        return hashMap;
    }
}
