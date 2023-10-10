// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class PaintingMapping
{
    private static final Int2ObjectMap<String> PAINTINGS;
    
    public static void init() {
        add("Kebab");
        add("Aztec");
        add("Alban");
        add("Aztec2");
        add("Bomb");
        add("Plant");
        add("Wasteland");
        add("Pool");
        add("Courbet");
        add("Sea");
        add("Sunset");
        add("Creebet");
        add("Wanderer");
        add("Graham");
        add("Match");
        add("Bust");
        add("Stage");
        add("Void");
        add("SkullAndRoses");
        add("Wither");
        add("Fighters");
        add("Pointer");
        add("Pigscene");
        add("BurningSkull");
        add("Skeleton");
        add("DonkeyKong");
    }
    
    private static void add(final String motive) {
        PaintingMapping.PAINTINGS.put(PaintingMapping.PAINTINGS.size(), motive);
    }
    
    public static String getStringId(final int id) {
        return PaintingMapping.PAINTINGS.getOrDefault(id, "kebab");
    }
    
    static {
        PAINTINGS = new Int2ObjectOpenHashMap<String>(26, 0.99f);
    }
}
