package net.optifine.util;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class FrameEvent {
    private static Map<String, Integer> mapEventFrames = new HashMap();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isActive(String name, int frameInterval) {
        Map<String, Integer> map = mapEventFrames;
        synchronized (map) {
            int j;
            int i = Minecraft.getMinecraft().entityRenderer.frameCount;
            Integer integer = (Integer)mapEventFrames.get((Object)name);
            if (integer == null) {
                integer = new Integer(i);
                mapEventFrames.put((Object)name, (Object)integer);
            }
            if (i > (j = integer.intValue()) && i < j + frameInterval) {
                return false;
            }
            mapEventFrames.put((Object)name, (Object)new Integer(i));
            return true;
        }
    }
}
