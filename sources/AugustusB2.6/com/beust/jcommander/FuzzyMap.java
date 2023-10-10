// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

import com.beust.jcommander.internal.Maps;
import java.util.Iterator;
import java.util.Map;

public class FuzzyMap
{
    public static <V> V findInMap(final Map<? extends IKey, V> map, final IKey key, final boolean b, final boolean b2) {
        if (b2) {
            return findAbbreviatedValue(map, key, b);
        }
        if (b) {
            return map.get(key);
        }
        final Iterator<? extends IKey> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            final IKey key2;
            if ((key2 = (IKey)iterator.next()).getName().equalsIgnoreCase(key.getName())) {
                return map.get(key2);
            }
        }
        return null;
    }
    
    private static <V> V findAbbreviatedValue(final Map<? extends IKey, V> map, final IKey obj, final boolean b) {
        final String name = obj.getName();
        final Map<Object, V> hashMap = Maps.newHashMap();
        final Iterator<? extends IKey> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            final IKey key;
            final String name2 = (key = (IKey)iterator.next()).getName();
            final boolean b2;
            if (b2 = ((b && name2.startsWith(name)) || (!b && name2.toLowerCase().startsWith(name.toLowerCase())))) {
                hashMap.put(name2, map.get(key));
            }
        }
        if (hashMap.size() > 1) {
            throw new ParameterException("Ambiguous option: " + obj + " matches " + hashMap.keySet());
        }
        V next;
        if (hashMap.size() == 1) {
            next = hashMap.values().iterator().next();
        }
        else {
            next = null;
        }
        return next;
    }
    
    interface IKey
    {
        String getName();
    }
}
