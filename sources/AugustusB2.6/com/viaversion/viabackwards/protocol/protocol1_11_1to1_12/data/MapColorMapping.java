// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_11_1to1_12.data;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public class MapColorMapping
{
    private static final Int2IntMap MAPPING;
    
    public static int getNearestOldColor(final int color) {
        return MapColorMapping.MAPPING.getOrDefault(color, color);
    }
    
    static {
        (MAPPING = new Int2IntOpenHashMap(64, 0.99f)).defaultReturnValue(-1);
        MapColorMapping.MAPPING.put(144, 59);
        MapColorMapping.MAPPING.put(145, 56);
        MapColorMapping.MAPPING.put(146, 56);
        MapColorMapping.MAPPING.put(147, 45);
        MapColorMapping.MAPPING.put(148, 63);
        MapColorMapping.MAPPING.put(149, 60);
        MapColorMapping.MAPPING.put(150, 60);
        MapColorMapping.MAPPING.put(151, 136);
        MapColorMapping.MAPPING.put(152, 83);
        MapColorMapping.MAPPING.put(153, 83);
        MapColorMapping.MAPPING.put(154, 80);
        MapColorMapping.MAPPING.put(155, 115);
        MapColorMapping.MAPPING.put(156, 39);
        MapColorMapping.MAPPING.put(157, 39);
        MapColorMapping.MAPPING.put(158, 36);
        MapColorMapping.MAPPING.put(159, 47);
        MapColorMapping.MAPPING.put(160, 60);
        MapColorMapping.MAPPING.put(161, 61);
        MapColorMapping.MAPPING.put(162, 62);
        MapColorMapping.MAPPING.put(163, 137);
        MapColorMapping.MAPPING.put(164, 108);
        MapColorMapping.MAPPING.put(165, 108);
        MapColorMapping.MAPPING.put(166, 109);
        MapColorMapping.MAPPING.put(167, 111);
        MapColorMapping.MAPPING.put(168, 112);
        MapColorMapping.MAPPING.put(169, 113);
        MapColorMapping.MAPPING.put(170, 114);
        MapColorMapping.MAPPING.put(171, 115);
        MapColorMapping.MAPPING.put(172, 118);
        MapColorMapping.MAPPING.put(173, 107);
        MapColorMapping.MAPPING.put(174, 107);
        MapColorMapping.MAPPING.put(175, 118);
        MapColorMapping.MAPPING.put(176, 91);
        MapColorMapping.MAPPING.put(177, 45);
        MapColorMapping.MAPPING.put(178, 46);
        MapColorMapping.MAPPING.put(179, 47);
        MapColorMapping.MAPPING.put(180, 85);
        MapColorMapping.MAPPING.put(181, 44);
        MapColorMapping.MAPPING.put(182, 27);
        MapColorMapping.MAPPING.put(183, 84);
        MapColorMapping.MAPPING.put(184, 83);
        MapColorMapping.MAPPING.put(185, 83);
        MapColorMapping.MAPPING.put(186, 83);
        MapColorMapping.MAPPING.put(187, 84);
        MapColorMapping.MAPPING.put(188, 84);
        MapColorMapping.MAPPING.put(189, 71);
        MapColorMapping.MAPPING.put(190, 71);
        MapColorMapping.MAPPING.put(191, 87);
        MapColorMapping.MAPPING.put(192, 107);
        MapColorMapping.MAPPING.put(193, 139);
        MapColorMapping.MAPPING.put(194, 43);
        MapColorMapping.MAPPING.put(195, 107);
        MapColorMapping.MAPPING.put(196, 111);
        MapColorMapping.MAPPING.put(197, 111);
        MapColorMapping.MAPPING.put(198, 111);
        MapColorMapping.MAPPING.put(199, 107);
        MapColorMapping.MAPPING.put(200, 112);
        MapColorMapping.MAPPING.put(201, 113);
        MapColorMapping.MAPPING.put(202, 113);
        MapColorMapping.MAPPING.put(203, 115);
        MapColorMapping.MAPPING.put(204, 116);
        MapColorMapping.MAPPING.put(205, 117);
        MapColorMapping.MAPPING.put(206, 107);
        MapColorMapping.MAPPING.put(207, 119);
    }
}
