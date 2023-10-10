// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.File;
import oshi.util.Util;
import com.sun.jna.platform.linux.Udev;
import java.util.HashSet;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import oshi.hardware.LogicalVolumeGroup;
import java.util.List;
import java.util.Set;
import java.util.Map;
import oshi.hardware.common.AbstractLogicalVolumeGroup;

final class LinuxLogicalVolumeGroup extends AbstractLogicalVolumeGroup
{
    private static final String BLOCK = "block";
    private static final String DM_UUID = "DM_UUID";
    private static final String DM_VG_NAME = "DM_VG_NAME";
    private static final String DM_LV_NAME = "DM_LV_NAME";
    private static final String DEV_LOCATION = "/dev/";
    
    LinuxLogicalVolumeGroup(final String name, final Map<String, Set<String>> lvMap, final Set<String> pvSet) {
        super(name, lvMap, pvSet);
    }
    
    static List<LogicalVolumeGroup> getLogicalVolumeGroups() {
        final Map<String, Map<String, Set<String>>> logicalVolumesMap = new HashMap<String, Map<String, Set<String>>>();
        final Map<String, Set<String>> physicalVolumesMap = new HashMap<String, Set<String>>();
        for (final String s : ExecutingCommand.runNative("pvs -o vg_name,pv_name")) {
            final String[] split = ParseUtil.whitespaces.split(s.trim());
            if (split.length == 2 && split[1].startsWith("/dev/")) {
                physicalVolumesMap.computeIfAbsent(split[0], k -> new HashSet()).add(split[1]);
            }
        }
        final Udev.UdevContext udev = Udev.INSTANCE.udev_new();
        try {
            final Udev.UdevEnumerate enumerate = udev.enumerateNew();
            try {
                enumerate.addMatchSubsystem("block");
                enumerate.scanDevices();
                for (Udev.UdevListEntry entry = enumerate.getListEntry(); entry != null; entry = entry.getNext()) {
                    final String syspath = entry.getName();
                    final Udev.UdevDevice device = udev.deviceNewFromSyspath(syspath);
                    if (device != null) {
                        try {
                            final String devnode = device.getDevnode();
                            if (devnode != null && devnode.startsWith("/dev/dm")) {
                                final String uuid = device.getPropertyValue("DM_UUID");
                                if (uuid != null && uuid.startsWith("LVM-")) {
                                    final String vgName = device.getPropertyValue("DM_VG_NAME");
                                    final String lvName = device.getPropertyValue("DM_LV_NAME");
                                    if (!Util.isBlank(vgName) && !Util.isBlank(lvName)) {
                                        logicalVolumesMap.computeIfAbsent(vgName, k -> new HashMap());
                                        final Map<String, Set<String>> lvMapForGroup = logicalVolumesMap.get(vgName);
                                        physicalVolumesMap.computeIfAbsent(vgName, k -> new HashSet());
                                        final Set<String> pvSetForGroup = physicalVolumesMap.get(vgName);
                                        final File slavesDir = new File(syspath + "/slaves");
                                        final File[] slaves = slavesDir.listFiles();
                                        if (slaves != null) {
                                            for (final File f : slaves) {
                                                final String pvName = f.getName();
                                                lvMapForGroup.computeIfAbsent(lvName, k -> new HashSet()).add("/dev/" + pvName);
                                                pvSetForGroup.add("/dev/" + pvName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        finally {
                            device.unref();
                        }
                    }
                }
            }
            finally {
                enumerate.unref();
            }
        }
        finally {
            udev.unref();
        }
        return logicalVolumesMap.entrySet().stream().map(e -> new LinuxLogicalVolumeGroup(e.getKey(), (Map<String, Set<String>>)e.getValue(), physicalVolumesMap.get(e.getKey()))).collect((Collector<? super Object, ?, List<LogicalVolumeGroup>>)Collectors.toList());
    }
}
