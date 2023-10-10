// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashSet;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import oshi.hardware.LogicalVolumeGroup;
import java.util.List;
import java.util.Set;
import java.util.Map;
import oshi.hardware.common.AbstractLogicalVolumeGroup;

final class MacLogicalVolumeGroup extends AbstractLogicalVolumeGroup
{
    private static final String DISKUTIL_CS_LIST = "diskutil cs list";
    private static final String LOGICAL_VOLUME_GROUP = "Logical Volume Group";
    private static final String PHYSICAL_VOLUME = "Physical Volume";
    private static final String LOGICAL_VOLUME = "Logical Volume";
    
    MacLogicalVolumeGroup(final String name, final Map<String, Set<String>> lvMap, final Set<String> pvSet) {
        super(name, lvMap, pvSet);
    }
    
    static List<LogicalVolumeGroup> getLogicalVolumeGroups() {
        final Map<String, Map<String, Set<String>>> logicalVolumesMap = new HashMap<String, Map<String, Set<String>>>();
        final Map<String, Set<String>> physicalVolumesMap = new HashMap<String, Set<String>>();
        String currentVolumeGroup = null;
        boolean lookForVGName = false;
        boolean lookForPVName = false;
        for (final String line : ExecutingCommand.runNative("diskutil cs list")) {
            if (line.contains("Logical Volume Group")) {
                lookForVGName = true;
            }
            else if (lookForVGName) {
                final int indexOf = line.indexOf("Name:");
                if (indexOf < 0) {
                    continue;
                }
                currentVolumeGroup = line.substring(indexOf + 5).trim();
                lookForVGName = false;
            }
            else if (line.contains("Physical Volume")) {
                lookForPVName = true;
            }
            else if (line.contains("Logical Volume")) {
                lookForPVName = false;
            }
            else {
                final int indexOf = line.indexOf("Disk:");
                if (indexOf < 0) {
                    continue;
                }
                if (lookForPVName) {
                    physicalVolumesMap.computeIfAbsent(currentVolumeGroup, k -> new HashSet()).add(line.substring(indexOf + 5).trim());
                }
                else {
                    logicalVolumesMap.computeIfAbsent(currentVolumeGroup, k -> new HashMap()).put(line.substring(indexOf + 5).trim(), Collections.emptySet());
                }
            }
        }
        return logicalVolumesMap.entrySet().stream().map(e -> new MacLogicalVolumeGroup(e.getKey(), (Map<String, Set<String>>)e.getValue(), physicalVolumesMap.get(e.getKey()))).collect((Collector<? super Object, ?, List<LogicalVolumeGroup>>)Collectors.toList());
    }
}
