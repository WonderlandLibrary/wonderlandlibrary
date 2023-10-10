// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix;

import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import oshi.util.tuples.Pair;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Ls
{
    private Ls() {
    }
    
    public static Map<String, Pair<Integer, Integer>> queryDeviceMajorMinor() {
        final Map<String, Pair<Integer, Integer>> majMinMap = new HashMap<String, Pair<Integer, Integer>>();
        for (final String s : ExecutingCommand.runNative("ls -l /dev")) {
            if (!s.isEmpty() && s.charAt(0) == 'b') {
                final int idx = s.lastIndexOf(32);
                if (idx <= 0 || idx >= s.length()) {
                    continue;
                }
                final String device = s.substring(idx + 1);
                final int major = ParseUtil.getNthIntValue(s, 2);
                final int minor = ParseUtil.getNthIntValue(s, 3);
                majMinMap.put(device, new Pair<Integer, Integer>(major, minor));
            }
        }
        return majMinMap;
    }
}
