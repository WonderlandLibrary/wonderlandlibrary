// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import com.google.common.collect.Maps;
import java.util.Map;

public class MapPopulator
{
    private static final String __OBFID = "CL_00002318";
    
    public static Map createMap(final Iterable keys, final Iterable values) {
        return populateMap(keys, values, Maps.newLinkedHashMap());
    }
    
    public static Map populateMap(final Iterable keys, final Iterable values, final Map map) {
        final Iterator var3 = values.iterator();
        for (final Object var5 : keys) {
            map.put(var5, var3.next());
        }
        if (var3.hasNext()) {
            throw new NoSuchElementException();
        }
        return map;
    }
}
