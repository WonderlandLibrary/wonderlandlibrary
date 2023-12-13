// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.sound;

import java.util.HashMap;

public class Effect
{
    private static final HashMap<Integer, Integer> effects;
    
    public static int getOldId(final int id) {
        return Effect.effects.getOrDefault(id, id);
    }
    
    static {
        (effects = new HashMap<Integer, Integer>()).put(1003, 1002);
        Effect.effects.put(1005, 1003);
        Effect.effects.put(1006, 1003);
        Effect.effects.put(1007, 1003);
        Effect.effects.put(1008, 1003);
        Effect.effects.put(1009, 1004);
        Effect.effects.put(1010, 1005);
        Effect.effects.put(1011, 1006);
        Effect.effects.put(1012, 1006);
        Effect.effects.put(1013, 1006);
        Effect.effects.put(1014, 1006);
        Effect.effects.put(1015, 1007);
        Effect.effects.put(1016, 1008);
        Effect.effects.put(1017, 1008);
        Effect.effects.put(1018, 1009);
        Effect.effects.put(1019, 1010);
        Effect.effects.put(1020, 1011);
        Effect.effects.put(1021, 1012);
        Effect.effects.put(1022, 1012);
        Effect.effects.put(1023, 1013);
        Effect.effects.put(1024, 1014);
        Effect.effects.put(1025, 1015);
        Effect.effects.put(1026, 1016);
        Effect.effects.put(1027, 1017);
        Effect.effects.put(1028, 1018);
        Effect.effects.put(1029, 1020);
        Effect.effects.put(1030, 1021);
        Effect.effects.put(1031, 1022);
        Effect.effects.put(1032, -1);
        Effect.effects.put(1033, -1);
        Effect.effects.put(1034, -1);
        Effect.effects.put(1035, -1);
        Effect.effects.put(1036, 1003);
        Effect.effects.put(1037, 1006);
        Effect.effects.put(3000, -1);
        Effect.effects.put(3001, -1);
    }
}
