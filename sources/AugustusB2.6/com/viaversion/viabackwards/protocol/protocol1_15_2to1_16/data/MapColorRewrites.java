// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public final class MapColorRewrites
{
    private static final Int2IntMap MAPPINGS;
    
    public static int getMappedColor(final int color) {
        return MapColorRewrites.MAPPINGS.getOrDefault(color, -1);
    }
    
    static {
        (MAPPINGS = new Int2IntOpenHashMap()).put(208, 113);
        MapColorRewrites.MAPPINGS.put(209, 114);
        MapColorRewrites.MAPPINGS.put(210, 114);
        MapColorRewrites.MAPPINGS.put(211, 112);
        MapColorRewrites.MAPPINGS.put(212, 152);
        MapColorRewrites.MAPPINGS.put(213, 83);
        MapColorRewrites.MAPPINGS.put(214, 83);
        MapColorRewrites.MAPPINGS.put(215, 155);
        MapColorRewrites.MAPPINGS.put(216, 143);
        MapColorRewrites.MAPPINGS.put(217, 115);
        MapColorRewrites.MAPPINGS.put(218, 115);
        MapColorRewrites.MAPPINGS.put(219, 143);
        MapColorRewrites.MAPPINGS.put(220, 127);
        MapColorRewrites.MAPPINGS.put(221, 127);
        MapColorRewrites.MAPPINGS.put(222, 127);
        MapColorRewrites.MAPPINGS.put(223, 95);
        MapColorRewrites.MAPPINGS.put(224, 127);
        MapColorRewrites.MAPPINGS.put(225, 127);
        MapColorRewrites.MAPPINGS.put(226, 124);
        MapColorRewrites.MAPPINGS.put(227, 95);
        MapColorRewrites.MAPPINGS.put(228, 187);
        MapColorRewrites.MAPPINGS.put(229, 155);
        MapColorRewrites.MAPPINGS.put(230, 184);
        MapColorRewrites.MAPPINGS.put(231, 187);
        MapColorRewrites.MAPPINGS.put(232, 127);
        MapColorRewrites.MAPPINGS.put(233, 124);
        MapColorRewrites.MAPPINGS.put(234, 125);
        MapColorRewrites.MAPPINGS.put(235, 127);
    }
}
