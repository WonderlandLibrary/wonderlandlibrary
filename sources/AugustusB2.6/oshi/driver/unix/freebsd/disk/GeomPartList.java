// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.freebsd.disk;

import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import java.util.HashMap;
import oshi.hardware.HWPartition;
import java.util.List;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class GeomPartList
{
    private static final String GEOM_PART_LIST = "geom part list";
    private static final String STAT_FILESIZE = "stat -f %i /dev/";
    
    private GeomPartList() {
    }
    
    public static Map<String, List<HWPartition>> queryPartitions() {
        final Map<String, String> mountMap = Mount.queryPartitionToMountMap();
        final Map<String, List<HWPartition>> partitionMap = new HashMap<String, List<HWPartition>>();
        String diskName = null;
        List<HWPartition> partList = new ArrayList<HWPartition>();
        String partName = null;
        String identification = "unknown";
        String type = "unknown";
        String uuid = "unknown";
        long size = 0L;
        String mountPoint = "";
        final List<String> geom = ExecutingCommand.runNative("geom part list");
        for (String line : geom) {
            line = line.trim();
            if (line.startsWith("Geom name:")) {
                if (diskName != null && !partList.isEmpty()) {
                    partitionMap.put(diskName, partList);
                    partList = new ArrayList<HWPartition>();
                }
                diskName = line.substring(line.lastIndexOf(32) + 1);
            }
            if (diskName != null) {
                if (line.contains("Name:")) {
                    if (partName != null) {
                        final int minor = ParseUtil.parseIntOrDefault(ExecutingCommand.getFirstAnswer("stat -f %i /dev/" + partName), 0);
                        partList.add(new HWPartition(identification, partName, type, uuid, size, 0, minor, mountPoint));
                        partName = null;
                        identification = "unknown";
                        type = "unknown";
                        uuid = "unknown";
                        size = 0L;
                    }
                    final String part = line.substring(line.lastIndexOf(32) + 1);
                    if (part.startsWith(diskName)) {
                        partName = part;
                        identification = part;
                        mountPoint = mountMap.getOrDefault(part, "");
                    }
                }
                if (partName == null) {
                    continue;
                }
                final String[] split = ParseUtil.whitespaces.split(line);
                if (split.length < 2) {
                    continue;
                }
                if (line.startsWith("Mediasize:")) {
                    size = ParseUtil.parseLongOrDefault(split[1], 0L);
                }
                else if (line.startsWith("rawuuid:")) {
                    uuid = split[1];
                }
                else {
                    if (!line.startsWith("type:")) {
                        continue;
                    }
                    type = split[1];
                }
            }
        }
        if (diskName != null) {
            if (partName != null) {
                final int minor2 = ParseUtil.parseIntOrDefault(ExecutingCommand.getFirstAnswer("stat -f %i /dev/" + partName), 0);
                partList.add(new HWPartition(identification, partName, type, uuid, size, 0, minor2, mountPoint));
            }
            if (!partList.isEmpty()) {
                partList = partList.stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)HWPartition::getName)).collect((Collector<? super Object, ?, List<HWPartition>>)Collectors.toList());
                partitionMap.put(diskName, partList);
            }
        }
        return partitionMap;
    }
}
