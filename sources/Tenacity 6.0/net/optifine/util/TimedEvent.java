// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.util.HashMap;
import java.util.Map;

public class TimedEvent
{
    private static Map<String, Long> mapEventTimes;
    
    public static boolean isActive(final String name, final long timeIntervalMs) {
        synchronized (TimedEvent.mapEventTimes) {
            final long i = System.currentTimeMillis();
            Long olong = TimedEvent.mapEventTimes.get(name);
            if (olong == null) {
                olong = new Long(i);
                TimedEvent.mapEventTimes.put(name, olong);
            }
            final long j = olong;
            if (i < j + timeIntervalMs) {
                return false;
            }
            TimedEvent.mapEventTimes.put(name, new Long(i));
            return true;
        }
    }
    
    static {
        TimedEvent.mapEventTimes = new HashMap<String, Long>();
    }
}
