// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.util;

import net.minecraft.client.Minecraft;
import java.util.HashMap;
import java.util.Map;

public class FrameEvent
{
    private static Map<String, Integer> mapEventFrames;
    
    static {
        FrameEvent.mapEventFrames = new HashMap<String, Integer>();
    }
    
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
                monitorexit(FrameEvent.mapEventFrames);
                return false;
            }
            FrameEvent.mapEventFrames.put(name, new Integer(i));
            monitorexit(FrameEvent.mapEventFrames);
            return true;
        }
    }
}
