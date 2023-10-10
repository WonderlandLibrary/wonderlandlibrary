// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public interface Int2IntBiMap extends Int2IntMap
{
    Int2IntBiMap inverse();
    
    int put(final int p0, final int p1);
    
    @Deprecated
    default void putAll(final Map<? extends Integer, ? extends Integer> m) {
        throw new UnsupportedOperationException();
    }
}
