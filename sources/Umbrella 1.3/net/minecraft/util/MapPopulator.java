/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapPopulator {
    private static final String __OBFID = "CL_00002318";

    public static Map createMap(Iterable keys, Iterable values) {
        return MapPopulator.populateMap(keys, values, Maps.newLinkedHashMap());
    }

    public static Map populateMap(Iterable keys, Iterable values, Map map) {
        Iterator var3 = values.iterator();
        for (Object var5 : keys) {
            map.put(var5, var3.next());
        }
        if (var3.hasNext()) {
            throw new NoSuchElementException();
        }
        return map;
    }
}

