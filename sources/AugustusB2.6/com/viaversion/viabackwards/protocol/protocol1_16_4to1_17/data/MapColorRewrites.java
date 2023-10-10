// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.data;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public final class MapColorRewrites
{
    private static final Int2IntMap MAPPINGS;
    
    public static int getMappedColor(final int color) {
        return MapColorRewrites.MAPPINGS.getOrDefault(color, -1);
    }
    
    static {
        (MAPPINGS = new Int2IntOpenHashMap()).put(236, 85);
        MapColorRewrites.MAPPINGS.put(237, 27);
        MapColorRewrites.MAPPINGS.put(238, 45);
        MapColorRewrites.MAPPINGS.put(239, 84);
        MapColorRewrites.MAPPINGS.put(240, 144);
        MapColorRewrites.MAPPINGS.put(241, 145);
        MapColorRewrites.MAPPINGS.put(242, 146);
        MapColorRewrites.MAPPINGS.put(243, 147);
        MapColorRewrites.MAPPINGS.put(244, 127);
        MapColorRewrites.MAPPINGS.put(245, 226);
        MapColorRewrites.MAPPINGS.put(246, 124);
        MapColorRewrites.MAPPINGS.put(247, 227);
    }
}
