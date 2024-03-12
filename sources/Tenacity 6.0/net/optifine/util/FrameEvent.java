// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import java.util.Map;

public class FrameEvent
{
    private static Map<String, Integer> mapEventFrames;
    
    public static boolean isActive(final String name, final int frameInterval) {
        synchronized (FrameEvent.mapEventFrames) {
            final int i = Minecraft.getMinecraft().entityRenderer.frameCount;
            Integer integer = FrameEvent.mapEventFrames.get(name);
            if (integer == null) {
                integer = new Integer(i);
                FrameEvent.mapEventFrames.put(name, integer);
            }
            final int j = integer;
            if (i > j && i < j + frameInterval) {
                return false;
            }
            FrameEvent.mapEventFrames.put(name, new Integer(i));
            return true;
        }
    }
    
    static {
        FrameEvent.mapEventFrames = new HashMap<String, Integer>();
    }
}
