// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux;

import oshi.util.ParseUtil;
import java.util.Iterator;
import oshi.util.ExecutingCommand;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Lshw
{
    private Lshw() {
    }
    
    public static String queryModel() {
        final String modelMarker = "product:";
        for (final String checkLine : ExecutingCommand.runNative("lshw -C system")) {
            if (checkLine.contains(modelMarker)) {
                return checkLine.split(modelMarker)[1].trim();
            }
        }
        return null;
    }
    
    public static String querySerialNumber() {
        final String serialMarker = "serial:";
        for (final String checkLine : ExecutingCommand.runNative("lshw -C system")) {
            if (checkLine.contains(serialMarker)) {
                return checkLine.split(serialMarker)[1].trim();
            }
        }
        return null;
    }
    
    public static String queryUUID() {
        final String uuidMarker = "uuid:";
        for (final String checkLine : ExecutingCommand.runNative("lshw -C system")) {
            if (checkLine.contains(uuidMarker)) {
                return checkLine.split(uuidMarker)[1].trim();
            }
        }
        return null;
    }
    
    public static long queryCpuCapacity() {
        final String capacityMarker = "capacity:";
        for (final String checkLine : ExecutingCommand.runNative("lshw -class processor")) {
            if (checkLine.contains(capacityMarker)) {
                return ParseUtil.parseHertz(checkLine.split(capacityMarker)[1].trim());
            }
        }
        return -1L;
    }
}
