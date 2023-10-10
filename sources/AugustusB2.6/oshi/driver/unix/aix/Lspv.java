// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import oshi.util.ParseUtil;
import java.util.Collections;
import oshi.util.ExecutingCommand;
import oshi.hardware.HWPartition;
import java.util.List;
import oshi.util.tuples.Pair;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Lspv
{
    private Lspv() {
    }
    
    public static List<HWPartition> queryLogicalVolumes(final String device, final Map<String, Pair<Integer, Integer>> majMinMap) {
        final String stateMarker = "PV STATE:";
        final String sizeMarker = "PP SIZE:";
        long ppSize = 0L;
        for (final String s : ExecutingCommand.runNative("lspv -L " + device)) {
            if (s.startsWith(stateMarker)) {
                if (!s.contains("active")) {
                    return Collections.emptyList();
                }
                continue;
            }
            else {
                if (!s.contains(sizeMarker)) {
                    continue;
                }
                ppSize = ParseUtil.getFirstIntValue(s);
            }
        }
        if (ppSize == 0L) {
            return Collections.emptyList();
        }
        ppSize <<= 20;
        final Map<String, String> mountMap = new HashMap<String, String>();
        final Map<String, String> typeMap = new HashMap<String, String>();
        final Map<String, Integer> ppMap = new HashMap<String, Integer>();
        for (final String s2 : ExecutingCommand.runNative("lspv -p " + device)) {
            final String[] split = ParseUtil.whitespaces.split(s2.trim());
            if (split.length >= 6 && "used".equals(split[1])) {
                final String name = split[split.length - 3];
                mountMap.put(name, split[split.length - 1]);
                typeMap.put(name, split[split.length - 2]);
                final int ppCount = 1 + ParseUtil.getNthIntValue(split[0], 2) - ParseUtil.getNthIntValue(split[0], 1);
                ppMap.put(name, ppCount + ppMap.getOrDefault(name, 0));
            }
        }
        final List<HWPartition> partitions = new ArrayList<HWPartition>();
        for (final Map.Entry<String, String> entry : mountMap.entrySet()) {
            final String mount = "N/A".equals(entry.getValue()) ? "" : entry.getValue();
            final String name2 = entry.getKey();
            final String type = typeMap.get(name2);
            final long size = ppSize * ppMap.get(name2);
            final Pair<Integer, Integer> majMin = majMinMap.get(name2);
            final int major = (majMin == null) ? ParseUtil.getFirstIntValue(name2) : majMin.getA();
            final int minor = (majMin == null) ? ParseUtil.getFirstIntValue(name2) : majMin.getB();
            partitions.add(new HWPartition(name2, name2, type, "", size, major, minor, mount));
        }
        return partitions;
    }
}
