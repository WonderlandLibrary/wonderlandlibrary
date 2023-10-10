// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data;

import java.lang.reflect.Field;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.NamedSoundRewriter;
import java.util.HashMap;
import java.util.Map;

public class NamedSoundMapping
{
    private static final Map<String, String> SOUNDS;
    
    public static String getOldId(String sound1_13) {
        if (sound1_13.startsWith("minecraft:")) {
            sound1_13 = sound1_13.substring(10);
        }
        return NamedSoundMapping.SOUNDS.get(sound1_13);
    }
    
    static {
        SOUNDS = new HashMap<String, String>();
        try {
            final Field field = NamedSoundRewriter.class.getDeclaredField("oldToNew");
            field.setAccessible(true);
            final Map<String, String> sounds = (Map<String, String>)field.get(null);
            sounds.forEach((sound1_12, sound1_13) -> NamedSoundMapping.SOUNDS.put(sound1_13, sound1_12));
        }
        catch (NoSuchFieldException | IllegalAccessException ex3) {
            final ReflectiveOperationException ex2;
            final ReflectiveOperationException ex = ex2;
            ex.printStackTrace();
        }
    }
}
